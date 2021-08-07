package guide.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

class EchoServer {
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
                        ch.pipeline().addLast(EchoServerHandler())
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
            val sync = bootstrap.bind(port).sync()
            sync.channel().closeFuture().sync()
        }
    }

    class EchoServerHandler : ChannelInboundHandlerAdapter() {
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            val byteBuf = msg as ByteBuf
            while (byteBuf.isReadable) {
                ctx.writeAndFlush(byteBuf)
            }
        }

        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }
}
