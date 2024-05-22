package com.goldenduo.catrpc.keeper

import com.goldenduo.catrpc.endpoint.EndPoint
import com.goldenduo.catrpc.rpc.RpcClient
import com.goldenduo.catrpc.rpc.RpcServer
import com.goldenduo.catrpc.rpc.remote.IKeeperRemote
import com.goldenduo.catrpc.rpc.remote.IRemote
import com.goldenduo.catrpc.rpc.remote.createStub

class KeeperClient(private val clientName:String,private val clientServePort:Int,implementObjs:Set<IRemote> = emptySet(),private val endPoint: EndPoint = EndPoint()):RpcClient(endPoint) {
    private val kClient=RpcClient()
    private val server=RpcServer(implementObjs)
    private val kStub= IRemote.createStub<IKeeperRemote>(kClient)
    fun connectKeeper(host: String, port: Int):Boolean{
        val connected= kClient.connect(host,port)
        if (connected){
            server.start(clientServePort)
            kStub.register(clientName,clientServePort)
        }
        return connected
    }

    fun connect(remoteName:String): Boolean {
        // Close the Channel if it's already connected
        val address= kStub.findAddress(remoteName) ?: return false
        return connect(address.host,address.port)
    }

    override fun close() {
        super.close()
        server.close()
        kClient.close()
    }
}