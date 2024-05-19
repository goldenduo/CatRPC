package com.goldenduo.catrpc.endpoint

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import java.net.InetSocketAddress

open class CatServer(private val endPoint: EndPoint=EndPoint()) : AutoCloseable {
    private var bossGroup: EventLoopGroup
    private var workerGroup: EventLoopGroup
    private var bootstrap: ServerBootstrap
    private var channel: Channel? = null
    @Volatile
    private var serverControllerFactory: (() -> ServerController)? = null

    init {
        endPoint.type= EndPoint.Type.Server
        // inline epoll-variable
        val isEpoll = Epoll.isAvailable()


        // get runtime processors for thread-size
        val cores = Runtime.getRuntime().availableProcessors()


        // Check for eventloop-groups
        this.bossGroup = if (isEpoll) EpollEventLoopGroup(2 * cores) else NioEventLoopGroup(2 * cores)
        this.workerGroup = if (isEpoll) EpollEventLoopGroup(10 * cores) else NioEventLoopGroup(10 * cores)


        // Create ServerBootstrap
        this.bootstrap = ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(if (isEpoll) EpollServerSocketChannel::class.java else NioServerSocketChannel::class.java)
            .childHandler(ServerInitializer())
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.IP_TOS, 24)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.SO_REUSEADDR, true)


        // Check for extra epoll-options
        if (isEpoll) {
            bootstrap
                .childOption(EpollChannelOption.EPOLL_MODE, EpollMode.LEVEL_TRIGGERED)
                .option(EpollChannelOption.TCP_FASTOPEN, 3)
                .option(EpollChannelOption.SO_REUSEPORT, true)
        }

    }

    fun start(port: Int) {
        // Start the server and wait for socket to be bind to the given port
        channel = bootstrap.bind(InetSocketAddress(port)).sync().channel()
    }

    fun setController(factory: (() -> ServerController)?) {
        serverControllerFactory = factory
    }

    override fun close() {
        // close server-channel
        channel?.close()

        // shutdown eventloop-groups
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }

    inner class ServerInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(ch: SocketChannel) {
            val pipe = ch.pipeline()
                //from server,out
                .addLast(LengthFieldPrepender(endPoint.lengthFieldLength))
                .addLast(SerializeEncoder(endPoint))



                //from socket,IN

                .addLast(LengthFieldBasedFrameDecoder(1024 * 1024, 0, endPoint.lengthFieldLength,0,endPoint.lengthFieldLength))
                .addLast(DeserializeDecoder(endPoint))
            serverControllerFactory?.let { serverControllerFactory->
                pipe.addLast(serverControllerFactory())
            }


        }

    }
}