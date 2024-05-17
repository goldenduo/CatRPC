package com.goldenduo.catrpc.scanner

import java.io.Serializable
import java.lang.reflect.Method

data class CatResponse(
    val requestId: Long,
    val data:Any?,
    val exception:String
) :Serializable
