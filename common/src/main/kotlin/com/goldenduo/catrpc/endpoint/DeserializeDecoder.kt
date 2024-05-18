package com.goldenduo.catrpc.endpoint

import com.goldenduo.catrpc.utils.info
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder

class DeserializeDecoder(private val endPoint: EndPoint, private val cacheSize:Int=1024*256) : MessageToMessageDecoder<ByteBuf>(){
    private val cache=object:ThreadLocal<ByteArray>(){
        override fun initialValue(): ByteArray {
            return ByteArray(cacheSize)
        }
    }
    override fun decode(ctx: ChannelHandlerContext, inBuf: ByteBuf, out: MutableList<Any>) {

        val typeIndex=inBuf.readShort().toInt()
        val clazz=endPoint.typeScanner.findType(typeIndex)

        val cacheSize=inBuf.readableBytes()
        inBuf.readBytes(cache.get(),0,cacheSize)
        val obj=endPoint.serializer.deserialize(cache.get(),0,cacheSize,clazz)
        out.add(obj)
        info("${endPoint.type} deserial ${obj}")
    }


}