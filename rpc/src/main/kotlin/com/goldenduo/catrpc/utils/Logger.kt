package com.goldenduo.catrpc.utils

import com.goldenduo.catrpc.BuildConfig
import io.netty.buffer.ByteBuf

fun info(msg:Any?){
   if (BuildConfig.INFO) println(msg.toString())
}
fun debug(msg:Any?){
    if (BuildConfig.DEBUG) println(msg.toString())
}
fun <T> T.debugLog(prefix:String=""):T{
    debug("${prefix}${this}")
    return this
}
fun <T> T.infoLog(prefix:String=""):T{
    debug("${prefix}${this}")
    return this
}

fun calcTimeMills(prefix:String="",job:()->Unit){
    val last=System.currentTimeMillis()
    job()
    debug("${prefix} cost ${System.currentTimeMillis()-last}ms")
}
fun ByteBuf.peek():String{
    val bytes=ByteArray(readableBytes())
    markReaderIndex()
    readBytes(bytes)
    resetReaderIndex()
    return bytes.joinToString()
}