

断断续续用了几天时间，写了一个简单的粗糙的类似DUBBO的服务调用框架。 

我给框架起了个很山寨的名字，暂且命名为：pangu，中文是盘古。该名称意为开天辟地，这里表示为连接客户端和服务端的桥梁，开源在[https://github.com/vavi/pangu](https://github.com/vavi/pangu) 。

目前该框架的实现还属于toy级别的，主要表现为以下几个方面：

* 有些类，方法命名还不是很考究，类的职责有待进一步明确；存在一部分硬编码。
* 没有注册中心，服务依赖关系统计等。
* 没有拦截器，没有事件，没有javassist，传输协议和数据协议接口没有抽象，负载均衡，失败重试，等等。

本文主要记录一下该框架中的几个关键知识点，供有兴趣的人参考。

## Using Spring extension
### Custom XML tag
详细介绍 [基于Spring可扩展Schema提供自定义配置支持](http://blog.csdn.net/cutesource/article/details/5864562)，在本文中的实现请参考META-INF里面的文件以及`CustomNamespaceHandler`等核心几个类。

核心实现如下：


	public BeanDefinition parse(Element element, ParserContext parserContext) {

	RootBeanDefinition beanDefinition = new RootBeanDefinition();
	beanDefinition.setBeanClass(beanClass);
	beanDefinition.setLazyInit(false);

	if (ConsumerConfig.class.equals(beanClass)) {
	    String id = element.getAttribute("id");
	    String interfaceName = element.getAttribute("interface");

	    beanDefinition.getPropertyValues().addPropertyValue("id", id);
	    beanDefinition.getPropertyValues().addPropertyValue(
		    "interfaceName", interfaceName);

	    parserContext.getRegistry().registerBeanDefinition(id,
		    beanDefinition);

	} else if (ProviderConfig.class.equals(beanClass)) {
	    String ref = element.getAttribute("ref");
	    Object reference = new RuntimeBeanReference(ref);
	    beanDefinition.getPropertyValues().addPropertyValue("ref",
		    reference);

	    String interfaceName = element.getAttribute("interface");
	    beanDefinition.getPropertyValues().addPropertyValue(
		    "interfaceName", interfaceName);

	    parserContext.getRegistry().registerBeanDefinition(interfaceName,
		    beanDefinition);
	}

	return beanDefinition;
    }

 
### RuntimeBeanReference

在上面的代码片段中，出现了这个类:`RuntimeBeanReference`。这个的类的主要作用是根据bean名称返回bean实例的引用，避免客户端显示获取bean实例。


### FactoryBean & Dynamic Proxy

在客户端获取一个bean时，其实这个bean是个代理类。在真正进行客户端调用时，会通过动态代理机制将数据请求发送到远端服务器上面。

在本实现中，由于`ConsumerConfig`实现了`FactoryBean`接口，那么当客户端根据这个beanId获取bean实例后，Spring会自动调用该bean的`getObject()`方法。究其Spring的`FactoryBean`接口调用实现时，也是直接instanceof判断了下。


 
	public Object ConsumerConfig.getObject() throws Exception {
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
 

关于动态代理，可以见上面方法的实现。JDK动态代理实现原理见笔者的这篇介绍：[JDK动态代理学习笔记](http://my.oschina.net/geecoodeer/blog/204138)

## Netty
### Serialization
在对象序列化时，简单使用了netty自带的`ObjectEncoder`和`ObjectDecoder`。

代码片段如下：

 

    public void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();
	pipeline.addLast(new ObjectEncoder(),
		new ObjectDecoder(ClassResolvers.cacheDisabled(null)));

	pipeline.addLast("handler", CLIENT_HANDLER);
    }
 

### FutureResponse
在本文中，当客户端调用一个服务时，需要同步等待服务端的响应。而由于Netty是一个异步调用框架，一般情况下无法直接实现这种类似功能。

在参考Dubbo的实现后，使用了类似的机制。FutureResponse的代码片段如下：

 	public Response get() {
	lock.lock();
	try {
	    // TODO add timeout
	    while (!isDone()) {
		done.await();
	    }
	} catch (InterruptedException e) {
	    throw new RuntimeException(e);
	} finally {
	    lock.unlock();
	}

	return response;
    }

    public static void doSingalWhenReceivedServerResponse(Response response) {
	FutureResponse future = futures.remove(response.getId());
	if (future != null) {
	    future.assginResponseAndDoSignal(response);
	}

    }

    private boolean isDone() {
	return this.response != null;
    }

    private void assginResponseAndDoSignal(Response response) {
	lock.lock();
	try {
	    this.response = response;
	    done.signal();
	} finally {
	    lock.unlock();
	}

    }

 


## How To Run
目前这个简单的服务调用框架开源在github上，地址是[https://github.com/vavi/pangu](https://github.com/vavi/pangu) 

在执行前，可能需要把buildPath改一下，是配置文件都编译到classPath下面；不然会报Spring配置文件找不到或者XML解析错误。

在执行时，依次运行`EchoServerTest`和`EchoClientTest`即可。

Have Fun。

## Reference

* [Dubbo 源码](https://github.com/alibaba/dubbo)
* [Dubbo 官方文档](http://alibaba.github.io/dubbo-doc-static/Developer+Guide-zh.htm)
* [Dubbo源代码阅读](http://attend.iteye.com/blog/1311524)
* [如何更好地学习Dubbo源代码](http://jm-blog.aliapp.com/?p=3138)
 
