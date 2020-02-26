package netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyLongChannel {

    private static Channel channel;
    private static NioEventLoopGroup group;

    public NettyLongChannel() {
    }

    public static boolean initNetty() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast("handler", new ChatClientHandler());

                        }
                    });
            //120.79.178.226  192.168.43.75
            ChannelFuture future = bootstrap.connect("120.79.178.226", 8089).sync();
            channel = future.channel();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public static void sendAndReflash(String head, String payload) {

        channel.writeAndFlush(head + "|" + payload + "\r\n");

    }

   /* public static void main(String[] args) {
        NettyLongChannel.initNetty();

        NettyLongChannel.sendAndReflash("84safas6ewf");
    }*/

    public static void sendMsg(String ProtoType,String json) {
        channel.writeAndFlush(ProtoType + "|" + json + "\r\n");
    }

    public static void nettyshoutdown() {
        group.shutdownGracefully();
    }
}