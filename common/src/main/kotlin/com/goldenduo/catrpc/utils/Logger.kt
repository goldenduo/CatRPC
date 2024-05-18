package com.goldenduo.catrpc.utils

import com.goldenduo.catrpc.BuildConfig
import io.netty.buffer.ByteBuf

fun info(msg:Any?){
   if (BuildConfig.INFO) println(msg.toString())
}
fun debug(msg:Any?){
    if (BuildConfig.DEBUG) println(msg.toString())
}

fun ByteBuf.peek():String{
    val bytes=ByteArray(readableBytes())
    markReaderIndex()
    readBytes(bytes)
    resetReaderIndex()
    return bytes.joinToString()
}