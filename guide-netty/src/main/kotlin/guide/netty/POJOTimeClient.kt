package guide.netty

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.ByteToMessageDecoder

class POJOTimeClient {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val workGroup = NioEventLoopGroup()
            val bootstrap = Bootstrap()
            bootstrap.group(workGroup)
            bootstrap.channel(NioSocketChannel::class.java)
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            bootstrap.handler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(TimeDecoder(), TimeClientHandler())
                }
            })
            val sync = bootstrap.connect("127.0.0.1", 8088).sync()
            sync.channel().closeFuture().sync()
            workGroup.shutdownGracefully()
        }
    }

    class TimeDecoder : ByteToMessageDecoder() {
        override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, out: MutableList<Any>) {
            if (input.readableBytes() < 4) {
                return
            }
            val unixTime = UNIXTime()
            unixTime.date = input.readUnsignedInt()
            out.add(unixTime)
        }
    }

    class TimeClientHandler : ChannelInboundHandlerAdapter() {
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            println(msg as UNIXTime)
            ctx.close()
        }

        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }
}
