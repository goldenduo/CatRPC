package com.goldenduo.catrpc.endpoint

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import java.net.InetSocketAddress


class CatClient(private val endPoint: EndPoint= EndPoint()) : AutoCloseable {
    private var group: EventLoopGroup
    private var boostrap: Bootstrap
    private var channel: Channel? = null
    @Volatile
    private var controllerFactory:(()->ClientController?)?=null
    init {
        endPoint.type= EndPoint.Type.Client
        val isEpoll = Epoll.isAvailable()
        // get runtime processors for thread-size
        val cores = Runtime.getRuntime().availableProcessors()
        // Check for eventloop-group
        group = if (isEpoll) EpollEventLoopGroup(10 * cores) else NioEventLoopGroup(10 * cores)
        boostrap = prepareBoostrap(group)
    }

    /**
     * setController should be called before connect
     */
    fun setController(factory: (() -> ClientController)?) {
        controllerFactory = factory
    }
    /**
     * Return if the client is connected or not
     */
    fun isConnected(): Boolean {
        val channel = channel
        return channel != null && channel.isOpen && channel.isActive
    }

    /**
     * Write the given object to the channel. This will be processed async
     *
     * @param
     */
    fun send(obj: Any) {
        send(obj, false)
    }

    /**
     * Write the given object to the channel.
     *
     * @param object
     * @param sync
     */
    fun send(obj: Any, sync: Boolean=true): Boolean {
        val channel = channel
        if (channel != null && isConnected()) {
            if (sync) {
                channel.writeAndFlush(obj).sync()
            } else {
                if (channel.eventLoop() == null || channel.eventLoop().inEventLoop()) {
                    channel.writeAndFlush(obj)
                } else {
                    channel.eventLoop().execute { channel.writeAndFlush(obj) }
                }
            }
            return true
        }
        return false
    }

    fun reconnect(host: String, port: Int): Boolean {
        closeChannel()
        return connect(host, port)
    }

    fun closeChannel(): Boolean {
        val channel = channel
        if (channel != null && isConnected()) {

            channel.close().sync()
            return true
        }
        return false
    }

    /**
     * Close the channel.
     */
    override fun close() {
        closeChannel()
        group.shutdownGracefully()
    }

    /**
     * Connects the client to the given host and port
     */
    fun connect(host: String, port: Int): Boolean {
        // Close the Channel if it's already connected
        if (isConnected()) {
            return false
        } else {
            // Start the client and wait for the connection to be established.
            channel = boostrap.connect(InetSocketAddress(host, port)).sync().channel()
            return true
        }

    }

    private fun prepareBoostrap(eventLoopGroup: EventLoopGroup): Bootstrap {
        // Create Bootstrap
        val bootstrap = Bootstrap()
            .group(eventLoopGroup)
            .channel(if (Epoll.isAvailable()) EpollSocketChannel::class.java else NioSocketChannel::class.java)
            .handler(ClientInitializer())
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)

        // Check for extra epoll-options
        if (Epoll.isAvailable()) {
            bootstrap
                .option(EpollChannelOption.EPOLL_MODE, EpollMode.LEVEL_TRIGGERED)
                .option(EpollChannelOption.TCP_FASTOPEN_CONNECT, true)
        }
        return bootstrap
    }

    private inner class ClientInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(ch: SocketChannel) {
            val pipeline = ch.pipeline()
                //from client,out
                .addLast(LengthFieldPrepender(endPoint.lengthFieldLength))
                .addLast(SerializeEncoder(endPoint))

                //from socket,in
                .addLast(LengthFieldBasedFrameDecoder(1024 * 1024, 0, endPoint.lengthFieldLength,0, endPoint.lengthFieldLength))
                .addLast(DeserializeDecoder(endPoint))
            controllerFactory?.let{controllerFactory->
                pipeline.addLast(controllerFactory())
            }


        }

    }
}