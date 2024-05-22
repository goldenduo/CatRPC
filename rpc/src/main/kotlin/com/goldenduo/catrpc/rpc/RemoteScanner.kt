package com.goldenduo.catrpc.rpc

import com.goldenduo.catrpc.endpoint.EndPoint
import com.goldenduo.catrpc.rpc.remote.IChannelContextAware
import com.goldenduo.catrpc.rpc.remote.IRemote
import com.goldenduo.catrpc.rpc.remote.RemoteConfig
import com.goldenduo.catrpc.utils.debugLog
import com.goldenduo.catrpc.utils.toMD5
import org.reflections.Reflections
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

class RemoteScanner(val endPoint:EndPoint,val predinefinedObjs:Set<IRemote> = emptySet()) {
    private val reflections = Reflections()


    private val methodCache = ConcurrentHashMap<String, InvokeInfo>()
    //for server
    private val channelContextAwareCache = ConcurrentHashMap<String, IChannelContextAware>()
    //for client
    private val idCache = ConcurrentHashMap<Method, String>()

    init {
        autoScan()
    }


    fun findChannelContextAware(id: String): IChannelContextAware? {

        return channelContextAwareCache[id]
    }
    fun findInvokeInfo(id: String): InvokeInfo {
        return methodCache[id]?:throw IllegalArgumentException("${endPoint.type}:Searching for ${id} but only ${methodCache.map {it.value }.joinToString() } exist")
    }

    fun findMethodId(method: Method): String {
        return idCache[method]?:throw IllegalArgumentException("${endPoint.type}:Searching for ${method.declaringClass.simpleName} but only ${idCache.map {it.key.declaringClass.simpleName }.distinct().joinToString() } exist")
    }

    fun putInvokeObject(methodId: String, obj: Any) {
        methodCache[methodId] = methodCache[methodId]!!.copy(obj = obj)
    }

    private fun autoScan(){
        val allRemotes = reflections.getSubTypesOf(IRemote::class.java)


        val remoteImplements = allRemotes.filter {
            !it.isInterface
        }
        scan(remoteImplements)
    }

    private fun scan(implementClasses:Iterable<Class<out IRemote>>) {

        implementClasses.map { implementClass ->
            implementClass to implementClass.interfaces.filter {
                IRemote::class.java.isAssignableFrom(it)
            }.first()
        }
            .flatMap { (implementClass, interfaceClass) ->
                interfaceClass.methods.toList().map { interfaceMethod ->
                    implementClass to interfaceMethod
                }
            }.forEach { (implementClass, method) ->
                val key =
                    "${implementClass.name}-${method.name}-${method.parameterTypes.map { it.name }.joinToString()}".toMD5()

                if (endPoint.type==EndPoint.Type.Server) {
                    val obj =if (predinefinedObjs.isEmpty()){
                        val config=implementClass.getAnnotation(RemoteConfig::class.java)?:RemoteConfig()
                        if (config.auto) {
                            implementClass.newInstance()
                        }else{
                            null
                        }
                    }else {
                        predinefinedObjs.filter { obj ->
                            implementClasses.any {
                                it.isInstance(obj)
                            }
                        }.firstOrNull()
                    }

                    if (obj!=null) {
                        methodCache[key] = InvokeInfo(obj, method)
                        if (obj is IChannelContextAware) {
                            channelContextAwareCache[key] = obj
                        }
                    }
                }else {
                    idCache[method] = key
                }
            }

    }
}

