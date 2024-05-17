package com.goldenduo.catrpc.serialize

import java.io.Serializable

interface ISerializer {
    fun serialize(obj: Any): ByteArray
    fun <T : Any> deserialize(bytes: ByteArray,offset:Int,length:Int, clazz: Class<T>): T

}

inline fun <reified T : Any> ISerializer.deserialize(bytes: ByteArray): T {
    return deserialize(bytes,0,bytes.size, T::class.java)
}