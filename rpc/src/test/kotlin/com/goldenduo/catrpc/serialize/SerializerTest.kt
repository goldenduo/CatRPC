package com.goldenduo.catrpc.serialize

import com.goldenduo.catrpc.types.TypeScanner
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.reflections.Reflections

class SerializerTest {
    private val typeScanner = TypeScanner()
    private val serializers = Reflections().getSubTypesOf(ISerializer::class.java)

    @BeforeEach
    fun setUp() {
        serializers.map { it.name }.forEach {
            println("find serializer:${it}")
        }

        typeScanner.travel { i, clazz ->
            println("find types: ${i} -> ${clazz.name}")
        }

    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun serialize() {
        serializers.forEach { it ->
            val serializer=it.newInstance() as ISerializer
            typeScanner.travel { i, clazz ->
                val origin = clazz.newInstance()
                val bytes = serializer.serialize(origin)
                val after = serializer.deserialize(bytes, clazz)
                Assertions.assertTrue(origin.equals(after)){
                    "origin:${origin}\n" +
                    "after:${after}"
                }
            }
        }
    }
}