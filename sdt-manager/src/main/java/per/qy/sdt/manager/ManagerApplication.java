package per.qy.sdt.manager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import per.qy.sdt.manager.handler.ServerHandler;
import per.qy.sdt.manager.model.TransactionGroup;

import java.util.HashMap;
import java.util.Map;

public class ManagerApplication {

    public static final Map<String, TransactionGroup> TRANSACTION_GROUPS = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("sdt manager start!");
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new ServerHandler())
                                    .addLast(new StringEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("sdt manager stop");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
