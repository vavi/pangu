package net.pangu.provider.transport.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyProviderrHandlerInitializer extends
	ChannelInitializer<SocketChannel> {

    private static final NettyProviderHandler PROVIDER_HANDLER = new NettyProviderHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();
	pipeline.addLast(new ObjectEncoder(),
		new ObjectDecoder(ClassResolvers.cacheDisabled(null)));

	pipeline.addLast("handler", PROVIDER_HANDLER);
    }
}
