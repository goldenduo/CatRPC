package com.goldenduo.catrpc.zookeeper

import com.goldenduo.catrpc.keeper.KeeperServer

val keeperPort=9099
fun main() {
    val keeperServer=KeeperServer()
    keeperServer.start(keeperPort)
}