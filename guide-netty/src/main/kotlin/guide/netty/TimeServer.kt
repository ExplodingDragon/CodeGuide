package guide.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

class TimeServer {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = 8088
            val bossGroup = NioEventLoopGroup()
            val workGroup = NioEventLoopGroup()
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(TimeServerHandler())
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
            val sync = bootstrap.bind(port).sync()
            sync.channel().closeFuture().sync()
        }
    }

    class TimeServerHandler : ChannelInboundHandlerAdapter() {
        override fun channelActive(ctx: ChannelHandlerContext) {
            val buffer = ctx.alloc().buffer(4)
            buffer.writeInt((System.currentTimeMillis() / 1000L + 2208988800L).toInt())
            val flush = ctx.writeAndFlush(buffer)
            flush.addListener(object : ChannelFutureListener {
                override fun operationComplete(future: ChannelFuture) {
                    assert(flush == future)
                    ctx.close()
                }
            })
        }

        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }
}
