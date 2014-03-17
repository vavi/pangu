package net.pangu.consumer.transport.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyConsumerHandlerInitializer extends
	ChannelInitializer<SocketChannel> {

    private static final NettyConsumerHandler CLIENT_HANDLER = new NettyConsumerHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();
	pipeline.addLast(new ObjectEncoder(),
		new ObjectDecoder(ClassResolvers.cacheDisabled(null)));

	pipeline.addLast("handler", CLIENT_HANDLER);
    }
}
