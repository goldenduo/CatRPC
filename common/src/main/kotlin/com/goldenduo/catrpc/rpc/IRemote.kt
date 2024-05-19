package com.goldenduo.catrpc.rpc

import java.lang.reflect.Proxy
import java.util.concurrent.atomic.AtomicLong

interface IRemote
interface ICommonRemote:IRemote {
    fun ping(): String
}

class CommonRemote : ICommonRemote {
    override fun ping(): String {
        return "pong"
    }
}

val requestIncreaseId = AtomicLong(0)
inline fun <reified T : IRemote> T.createStub(client: RpcCatClient): T {
    return Proxy.newProxyInstance(
        T::class.java.classLoader, arrayOf(T::class.java)
    ) { proxy, method, args ->
        val request = RemoteRequest(
            requestIncreaseId.getAndIncrement(),
            T::class.java.name,
            method.name,
            method.parameterTypes,
            args?: emptyArray(),
        )
        request.returnType=method.returnType
        val rpcConfig = method.getAnnotation(RpcConfig::class.java)
        client.call(request, rpcConfig)
    } as T
}



