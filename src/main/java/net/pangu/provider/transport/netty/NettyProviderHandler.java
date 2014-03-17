package net.pangu.provider.transport.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pangu.common.util.BeanHolder;
import net.pangu.consumer.Request;
import net.pangu.consumer.transport.netty.FutureResponse;
import net.pangu.provider.Response;
import net.pangu.provider.ServiceResponse;

@Sharable
@SuppressWarnings("rawtypes")
public class NettyProviderHandler extends ChannelHandlerAdapter {
    private static final Logger logger = Logger
	    .getLogger(NettyProviderHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	if (msg instanceof FutureResponse) {
	    FutureResponse future = (FutureResponse) msg;
	    Request request = future.getRequest();
	    Object bean = BeanHolder.get(request.getInterfaceName());
	    Class clazz = bean.getClass();
	    @SuppressWarnings("unchecked")
	    Method method = clazz.getDeclaredMethod(request.getMethodName(),
		    request.getParameterTypes());
	    Object result = method.invoke(bean, request.getArgs());
	    Response response = new ServiceResponse();
	    response.setId(request.getId());
	    response.setResult(result);
	    // request.get
	    ctx.write(response);
	    ctx.flush();
	} else {
	    logger.warning("Not expected message:" + msg);
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
