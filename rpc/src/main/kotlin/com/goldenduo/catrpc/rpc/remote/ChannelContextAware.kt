package com.goldenduo.catrpc.rpc.remote

import io.netty.channel.ChannelHandlerContext

open class ChannelContextAware :IChannelContextAware{
    private var _channelHandlerContext=ThreadLocal<ChannelHandlerContext>()
    override var channelHandlerContext: ChannelHandlerContext?
        get() {
            return _channelHandlerContext.get()
        }
        set(value) {
            _channelHandlerContext.set(value)
        }

}