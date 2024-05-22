package com.goldenduo.catrpc.rpc.remote

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext

interface IChannelContextAware {
    /**
     * It only exists in IRemote RPC method, or will be null
     */
    var channelHandlerContext:ChannelHandlerContext?

}