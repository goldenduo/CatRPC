package com.goldenduo.catrpc.endpoint

import com.goldenduo.catrpc.utils.info
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class SerializeEncoder(private val endPoint: EndPoint):MessageToByteEncoder<Any>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Any, out: ByteBuf) {
        info("${endPoint.type} serial ${msg}")
        val typeIndex=endPoint.typeScanner.findIndex(msg::class.java)
        val data=endPoint.serializer.serialize(msg)
        out.writeShort(typeIndex)
        out.writeBytes(data)
    }

}