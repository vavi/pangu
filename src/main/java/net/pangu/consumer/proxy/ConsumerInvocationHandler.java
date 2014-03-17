package net.pangu.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

import net.pangu.common.spring.extension.ConsumerConfig;
import net.pangu.consumer.Request;
import net.pangu.consumer.ServiceRequest;
import net.pangu.consumer.transport.NetworkClient;
import net.pangu.provider.Response;

public class ConsumerInvocationHandler implements InvocationHandler {

    private static AtomicLong requestId = new AtomicLong();

    private final NetworkClient channel;

    private final ConsumerConfig consumerConfig;

    public ConsumerInvocationHandler(NetworkClient channel,
	    ConsumerConfig consumerConfig) {
	this.channel = channel;
	this.consumerConfig = consumerConfig;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
	    throws Throwable {
	Class<?>[] parameterTypes = method.getParameterTypes();
	Class<?> returnType = method.getReturnType();
	String methodName = method.getName();
	channel.connect("127.0.0.1", 8081); // FIXME 1.get data from registry 2.
					    // add load balance (shuffle and
					    // rr)3.failure
					    // torerent(retry and abandan it)
					    // 4.monitor 5.
					    // cache provider list in case
					    // registry is out of service

	Request request = new ServiceRequest();
	request.setId(requestId.getAndDecrement());
	request.setInterfaceName(consumerConfig.getInterfaceName());
	request.setMethodName(methodName);
	request.setArgs(args);
	request.setParameterTypes(parameterTypes);
	request.setReturnType(returnType);
	Response response = channel.sendRequestAndGetResponse(request);

	return response.getResult();
    }

    public ConsumerConfig getConsumerConfig() {
	return consumerConfig;
    }

}
