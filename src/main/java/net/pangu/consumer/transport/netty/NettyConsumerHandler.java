package net.pangu.consumer.transport.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.pangu.provider.Response;

@Sharable
public class NettyConsumerHandler extends ChannelHandlerAdapter {
    private static final Logger logger = Logger
	    .getLogger(NettyConsumerHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	if (msg instanceof Response) {
	    ResponseFuture.doSingalWhenReceivedServerResponse((Response) msg);
	}

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
	logger.log(Level.WARNING, "Unexpected exception from downstream.",
		cause);
	ctx.close();
    }
}
