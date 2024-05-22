package com.goldenduo.catrpc.rpc

import com.goldenduo.catrpc.endpoint.CatClient
import com.goldenduo.catrpc.endpoint.ClientController
import com.goldenduo.catrpc.endpoint.EndPoint
import com.goldenduo.catrpc.rpc.exception.RpcOnewayException
import com.goldenduo.catrpc.rpc.exception.RpcRemoteException
import com.goldenduo.catrpc.rpc.exception.RpcTimeoutException
import com.goldenduo.catrpc.rpc.types.RemoteRequest
import com.goldenduo.catrpc.rpc.types.RemoteResponse
import com.goldenduo.catrpc.utils.info
import io.netty.channel.ChannelHandlerContext
import org.thavam.util.concurrent.blockingMap.BlockingHashMap
import org.thavam.util.concurrent.blockingMap.BlockingMap
import java.util.concurrent.TimeUnit

open class RpcClient(private val endPoint: EndPoint = EndPoint()) : CatClient(endPoint) {
    //need to shutdown
    @Volatile
    private lateinit var blockingMap: BlockingMap<Long, RemoteResponse>
    val remoteScanner = RemoteScanner(endPoint = endPoint)


    init {
        setController {
            object : ClientController() {
                override fun channelActive(ctx: ChannelHandlerContext) {
                    blockingMap = BlockingHashMap()
                    super.channelActive(ctx)
                }

                override fun handle(ctx: ChannelHandlerContext, obj: Any, type: Class<*>) {
                    if (obj is RemoteResponse) {
                        val lastResponse = blockingMap.put(obj.requestId, obj)
                        if (lastResponse != null) {
                            throw IllegalStateException("Detected a duplicate response ID: $lastResponse−>$obj")
                        }
                    } else {
                        info("An unhandled RPC type was encountered: ${type.name}")
                    }
                }

                override fun channelInactive(ctx: ChannelHandlerContext) {
                    blockingMap.clear()
                    super.channelInactive(ctx)
                }
            }
        }
    }

    fun call(request: RemoteRequest, config: RpcConfig): Any? {
        send(request)

        if (config.isOneWay) {
            if (request.returnType == Void.TYPE) {
                return null
            } else {
                throw RpcOnewayException()
            }
        }

        val response =
            blockingMap.take(request.requestId, config.maxTimeMillis, TimeUnit.MILLISECONDS)
                ?: throw RpcTimeoutException("No response was received for $request in ${config.maxTimeMillis}ms")

        if (response.exception != null) {
            throw RpcRemoteException(response.exception)
        }

        return response.data

    }


}