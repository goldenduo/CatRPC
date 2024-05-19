package com.goldenduo.catrpc.rpc

import java.io.Serializable

data class RemoteResponse(
    val requestId: Long,
    val data:Any?,
    val exception:String?
) :Serializable
