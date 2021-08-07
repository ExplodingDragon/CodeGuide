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
import java.text.SimpleDateFormat

class DecodeTimeClient {
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
            out.add(input.readBytes(4))
        }
    }

    class TimeClientHandler : ChannelInboundHandlerAdapter() {
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            val byteBuf = msg as ByteBuf
            try {
                val time = (byteBuf.readUnsignedInt() - 2208988800L) * 1000L
                println(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(time))
                ctx.close()
            } catch (e: Exception) {
                msg.release()
            }
        }

        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }
}
