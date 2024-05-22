package com.goldenduo.catrpc.rpc.types

import com.goldenduo.catrpc.types.CatMessage
import java.io.Serializable

data class RemoteResponse(
    val requestId: Long,
    val data:Any?,
    val exception:String?
) : CatMessage
