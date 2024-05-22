package com.goldenduo.catrpc.endpoint

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

abstract class ServerController:SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg:Any) {
        val type=msg::class.java
        val result= handle(ctx,msg, type)
        ctx.channel().writeAndFlush(result)
    }
    abstract fun handle(ctx:ChannelHandlerContext,obj:Any,type:Class<*>):Any

}