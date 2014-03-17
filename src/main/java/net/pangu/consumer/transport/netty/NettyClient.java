package net.pangu.consumer.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.pangu.consumer.Request;
import net.pangu.consumer.transport.NetworkClient;
import net.pangu.provider.Response;

public class NettyClient implements NetworkClient {

    private Channel channel;

    private static EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) {

	try {
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)
		    .handler(new NettyConsumerHandlerInitializer());

	    channel = b.connect(host, port).sync().channel();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    Thread.currentThread().interrupt();
	}

    }

    public Response sendRequestAndGetResponse(Request request) {
	ResponseFuture responseFuture = new ResponseFuture(request);

	// FIXME
	ChannelFuture future = channel.writeAndFlush(responseFuture);
	if (future.isSuccess()) {
	    // log debug
	} else {
	    if (null != future.cause())
		System.out.println(future.cause());
	}
	return responseFuture.get();
    }
}
