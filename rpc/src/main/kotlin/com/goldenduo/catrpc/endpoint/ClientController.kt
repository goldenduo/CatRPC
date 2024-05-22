package com.goldenduo.catrpc.endpoint

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

abstract class ClientController:SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        val type=msg::class.java
        handle(ctx,msg, type)
    }
    abstract fun handle(ctx: ChannelHandlerContext,obj:Any,type:Class<*>)
}