package com.goldenduo.catrpc.rpc.remote

import com.goldenduo.catrpc.rpc.RpcClient
import com.goldenduo.catrpc.rpc.RpcConfig
import com.goldenduo.catrpc.rpc.types.RemoteRequest
import io.netty.channel.ChannelHandlerContext
import java.lang.reflect.Proxy
import java.util.concurrent.atomic.AtomicLong

interface IRemote{
    companion object
}




val requestIncreaseId = AtomicLong(0)
val emptyRcpConfig= RpcConfig()
inline fun <reified T : IRemote> IRemote.Companion.createStub(client: RpcClient): T {
    val proxy= Proxy.newProxyInstance(
        T::class.java.classLoader, arrayOf(T::class.java)
    ) { proxy, method, args ->
        val request = RemoteRequest(
            requestIncreaseId.getAndIncrement(),
            client.remoteScanner.findMethodId(method),
            args?: emptyArray(),
        )
        request.returnType=method.returnType
        val rpcConfig = method.getAnnotation(RpcConfig::class.java)?: emptyRcpConfig
        client.call(request, rpcConfig)
    }
    return proxy as T
}


