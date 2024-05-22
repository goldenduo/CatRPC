package com.goldenduo.catrpc.rpc.types

import com.goldenduo.catrpc.types.CatMessage

data class RemoteRequest(
    val requestId: Long,
    val methodId: String,
    var parameters: Array<Any> = emptyArray(),
): CatMessage {
    @Transient
    lateinit var returnType:Class<*>
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteRequest

        if (requestId != other.requestId) return false
        if (methodId != other.methodId) return false
        if (!parameters.contentEquals(other.parameters)) return false
        if (returnType != other.returnType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = requestId.hashCode()
        result = 31 * result + methodId.hashCode()
        result = 31 * result + parameters.contentHashCode()
        result = 31 * result + returnType.hashCode()
        return result
    }


}

