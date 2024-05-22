package com.goldenduo.catrpc.zookeeper

import com.goldenduo.catrpc.rpc.remote.IRemote

interface ISimpleRemote :IRemote{
    fun ping():String
}