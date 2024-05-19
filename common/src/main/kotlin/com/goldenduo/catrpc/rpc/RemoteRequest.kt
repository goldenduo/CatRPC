package com.goldenduo.catrpc.rpc

import java.io.Serializable

data class RemoteRequest(
    val requestId: Long,
    val className: String,
    val methodName: String,
    var parameterTypes: Array<Class<*>> = emptyArray(),
    var parameters: Array<Any> = emptyArray(),

): Serializable {
    @Transient
    lateinit var returnType:Class<*>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteRequest

        if (requestId != other.requestId) return false
        if (className != other.className) return false
        if (methodName != other.methodName) return false
        if (!parameterTypes.contentEquals(other.parameterTypes)) return false
        if (!parameters.contentEquals(other.parameters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = requestId.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + methodName.hashCode()
        result = 31 * result + parameterTypes.contentHashCode()
        result = 31 * result + parameters.contentHashCode()
        return result
    }
}
