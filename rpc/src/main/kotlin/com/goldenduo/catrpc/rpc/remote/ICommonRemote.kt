package com.goldenduo.catrpc.rpc.remote

interface ICommonRemote: IRemote {
    fun ping(): String

}