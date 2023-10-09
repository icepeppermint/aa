package io.aa.common.server;

import static java.util.Objects.requireNonNull;

import java.net.InetSocketAddress;
import java.net.URI;

import javax.annotation.Nullable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public final class Server {

    private final int port;
    private final Route route;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Nullable
    private Channel channel;

    Server(int port, Route route) {
        this.port = port;
        this.route = requireNonNull(route, "route");
    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

    public void start() throws InterruptedException {
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         final ChannelPipeline pipeline = ch.pipeline();
                         pipeline.addLast(new HttpServerCodec());
                         pipeline.addLast(new ChunkedWriteHandler());
                         pipeline.addLast(new HttpServerHandler(route));
                     }
                 });
        channel = bootstrap.bind(port).sync().channel();
    }

    public URI httpUri() {
        requireNonNull(channel, "channel");
        return URI.create("http://localhost:" + ((InetSocketAddress) channel.localAddress()).getPort());
    }

    public void stop() throws InterruptedException {
        try {
            if (channel != null) {
                channel.close().sync();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
