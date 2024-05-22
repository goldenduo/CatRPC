package com.goldenduo.catrpc.keeper

import com.goldenduo.catrpc.rpc.RpcServer
import com.goldenduo.catrpc.rpc.remote.KeeperRemote

class KeeperServer:RpcServer(implementObjs = setOf( KeeperRemote())) {

}