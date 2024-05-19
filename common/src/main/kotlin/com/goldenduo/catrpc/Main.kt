package com.goldenduo.catrpc

import com.goldenduo.catrpc.rpc.RemoteRequest
import com.goldenduo.catrpc.serialize.ProtostuffSerializer
import com.goldenduo.catrpc.serialize.deserialize
import org.thavam.util.concurrent.blockingMap.BlockingHashMap
import java.io.Serializable
import java.util.*


fun main() {
   val map=BlockingHashMap<String,String>()
    map.put("a","b")


    println(map.put("a","c"))
    println(map.take("a"))
    println(map.take("a"))
}
class Test{
    fun ok(){}
}


