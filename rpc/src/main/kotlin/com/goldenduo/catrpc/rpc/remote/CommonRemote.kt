package com.goldenduo.catrpc.rpc.remote

class CommonRemote: ICommonRemote {
    override fun ping(): String {
        return "pong"
    }

}