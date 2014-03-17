package net.pangu.common.spring.extension;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import net.pangu.common.exception.PanguRuntimeException;
import net.pangu.consumer.proxy.ConsumerInvocationHandler;
import net.pangu.consumer.transport.NetworkClient;
import net.pangu.consumer.transport.netty.NettyClient;

import org.springframework.beans.factory.FactoryBean;

public class ConsumerConfig implements FactoryBean<Object>, Serializable {

    private static final long serialVersionUID = -5969085421064508957L;
    private String id;
    private String interfaceName;

    public String getInterfaceName() {
	return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
	this.interfaceName = interfaceName;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Object getObject() throws Exception {
	ClassLoader clazzLoader = Thread.currentThread()
		.getContextClassLoader();

	Class<?> intefaceClazz = null;
	try {
	    intefaceClazz = Class.forName(interfaceName, false, clazzLoader);
	} catch (ClassNotFoundException e) {
	    throw new PanguRuntimeException(e);
	}

	// TODO add cache and get channel by config
	NetworkClient channel = new NettyClient();
	ConsumerInvocationHandler invocationHandler = new ConsumerInvocationHandler(
		channel, this);
	Class<?>[] intefaceClazzArray = new Class[] { intefaceClazz };

	Object consumerProxy = Proxy.newProxyInstance(clazzLoader,
		intefaceClazzArray, invocationHandler);
	return consumerProxy;
    }

    public Class<?> getObjectType() {
	// TODO
	try {
	    return Class.forName(interfaceName).getClass();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public boolean isSingleton() {
	return true;
    }

}
