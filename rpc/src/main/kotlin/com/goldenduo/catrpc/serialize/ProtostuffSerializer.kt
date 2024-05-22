package com.goldenduo.catrpc.serialize

import io.protostuff.LinkedBuffer
import io.protostuff.ProtostuffIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import org.objenesis.Objenesis
import org.objenesis.ObjenesisStd
import java.util.concurrent.ConcurrentHashMap

class ProtostuffSerializer : ISerializer {
    private val cachedSchema: ConcurrentHashMap<Class<*>, Schema<*>> = ConcurrentHashMap<Class<*>, Schema<*>>()
    private val objenesis: Objenesis = ObjenesisStd(true)
    private val cacheLocal = object : ThreadLocal<LinkedBuffer>() {
        override fun initialValue(): LinkedBuffer {
            return LinkedBuffer.allocate()
        }
    }

    private fun <T> getSchema(cls: Class<out T>): Schema<T> {
        // for thread-safe
        @Suppress("UNCHECKED_CAST")
        return cachedSchema.computeIfAbsent(cls) {
            RuntimeSchema.createFrom(it)
        } as Schema<T>
    }

    override fun serialize(obj: Any): ByteArray {
        val clazz = obj::class.java
        val buffer = cacheLocal.get()
        buffer.clear()
        val schema = getSchema(clazz)
        return ProtostuffIOUtil.toByteArray(obj, schema, buffer)

    }

    override fun <T : Any> deserialize(bytes: ByteArray,offset:Int,length:Int, clazz: Class<T>): T {
        val obj=objenesis.newInstance(clazz)
        val schema=getSchema(clazz)
        ProtostuffIOUtil.mergeFrom(bytes,offset,length,obj,schema)
        return obj
    }




}