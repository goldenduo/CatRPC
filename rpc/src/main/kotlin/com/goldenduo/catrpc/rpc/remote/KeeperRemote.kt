package com.goldenduo.catrpc.rpc.remote

import com.goldenduo.catrpc.rpc.remote.IKeeperRemote
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
@RemoteConfig(auto=false)
class KeeperRemote:ChannelContextAware(),IKeeperRemote{
    private val portTable=ConcurrentHashMap<String,Address>()

    override fun register(remoteName: String, remotePort: Int) {
        val address=channelHandlerContext?.channel()?.remoteAddress() as InetSocketAddress

        portTable[remoteName] = Address( address.hostString,remotePort)
    }

    override fun unregister(remoteName: String) {
        portTable.remove(remoteName)
    }

    override fun findAddress(remoteName: String): Address? {
        return portTable[remoteName]
    }
}