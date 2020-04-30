package per.qy.sdt.client.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import per.qy.sdt.client.model.Transaction;
import per.qy.sdt.client.util.GsonUtil;

public class TransactionManagerClient {

    private static volatile Channel channel;

    private static void createSocketChannel() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new ClientHandler())
                                    .addLast(new StringEncoder());
                        }
                    })
                    .remoteAddress("127.0.0.1", 9999);
            channel = bootstrap.connect().sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Channel getSocketChannel() {
        if (channel == null) {
            synchronized (TransactionManagerClient.class) {
                if (channel == null) {
                    createSocketChannel();
                }
            }
        }
        return channel;
    }

    public static void sendTransaction(Transaction transaction) {
        getSocketChannel().writeAndFlush(GsonUtil.toJson(transaction));
    }
}
