package guide.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.MessageToByteEncoder

class POJOTimeServer {
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
                        ch.pipeline().addLast(TimeEncoder(), TimeServerHandler())
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
            val sync = bootstrap.bind(port).sync()
            sync.channel().closeFuture().sync()
        }
    }

    class TimeEncoder : MessageToByteEncoder<UNIXTime>() {
        override fun encode(ctx: ChannelHandlerContext, msg: UNIXTime, out: ByteBuf) {
            out.writeInt(msg.date.toInt())
        }
    }

    class TimeServerHandler : ChannelInboundHandlerAdapter() {
        override fun channelActive(ctx: ChannelHandlerContext) {
            val flush = ctx.writeAndFlush(UNIXTime())
            flush.addListener(ChannelFutureListener.CLOSE)
        }

        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }
}
