package com.goldenduo.catrpc.rpc.remote

interface IKeeperRemote : IRemote {
    fun register(remoteName:String,remotePort:Int)

    fun unregister(remoteName:String)

    fun findAddress(remoteName:String):Address?
}