package com.goldenduo.catrpc.rpc

import com.goldenduo.catrpc.endpoint.CatServer
import com.goldenduo.catrpc.endpoint.ServerController
import com.goldenduo.catrpc.rpc.remote.IRemote
import com.goldenduo.catrpc.rpc.types.RemoteRequest
import com.goldenduo.catrpc.rpc.types.RemoteResponse
import com.goldenduo.catrpc.types.Unhandled
import com.goldenduo.catrpc.utils.info
import io.netty.channel.ChannelHandlerContext

open class RpcServer(implementObjs:Set<IRemote> = emptySet()) : CatServer() {
    private val remoteScanner = RemoteScanner( endPoint = endPoint,implementObjs)

    init {
        setController {
            object : ServerController() {

                override fun handle(ctx:ChannelHandlerContext, obj: Any, type: Class<*>): Any {
                    return if (obj is RemoteRequest) {
                        val invokeInfo = remoteScanner.findInvokeInfo(obj.methodId)
                        val contextAware=remoteScanner.findChannelContextAware(obj.methodId)
                        try {
                            contextAware?.channelHandlerContext=ctx
                            val returnObj = invokeInfo.method.invoke(invokeInfo.obj, *obj.parameters)
                            contextAware?.channelHandlerContext=null
                            RemoteResponse(
                                obj.requestId, returnObj, null
                            )
                        } catch (e: Exception) {
                            RemoteResponse(
                                obj.requestId, null, e.stackTraceToString()
                            )
                        }
                    } else {
                        info("An unhandled RPC type was encountered: ${type.name}")
                        Unhandled()
                    }

                }
            }
        }
    }
}