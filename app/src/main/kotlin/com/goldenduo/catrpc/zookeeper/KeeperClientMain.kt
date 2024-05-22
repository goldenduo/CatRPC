package com.goldenduo.catrpc.zookeeper

import com.goldenduo.catrpc.keeper.KeeperClient
import com.goldenduo.catrpc.rpc.remote.IRemote
import com.goldenduo.catrpc.rpc.remote.createStub
import com.goldenduo.catrpc.utils.debugLog
import com.goldenduo.catrpc.utils.findAvailablePort


fun main() {
    val clientA=KeeperClient("clientA", findAvailablePort(), setOf(
        object:ISimpleRemote{
            override fun ping(): String {
                return "A do"
            }
        }
    ))
    clientA.connectKeeper("127.0.0.1", keeperPort)
    val clientB=KeeperClient("clientB", findAvailablePort(),setOf(
        object:ISimpleRemote{
            override fun ping(): String {
                return "B do"
            }
        }
    ))
    clientA.connectKeeper("127.0.0.1", keeperPort)
    clientB.connectKeeper("127.0.0.1", keeperPort)
    clientA.connect("clientB")
    clientB.connect("clientA")

    IRemote.createStub<ISimpleRemote>(clientA).ping().debugLog()
    IRemote.createStub<ISimpleRemote>(clientB).ping().debugLog()



}