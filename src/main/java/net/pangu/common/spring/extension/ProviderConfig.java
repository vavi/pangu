package net.pangu.common.spring.extension;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.pangu.common.util.BeanHolder;
import net.pangu.provider.transport.netty.NettyProviderrHandlerInitializer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SuppressWarnings("rawtypes")
public class ProviderConfig<T> implements ApplicationListener {
    private T ref;
    private transient ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
	return applicationContext;
    }

    public T getRef() {
	return ref;
    }

    public void setRef(T ref) {
	this.ref = ref;
    }

    private String interfaceName;

    public String getInterfaceName() {
	return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
	this.interfaceName = interfaceName;
    }

    public void onApplicationEvent(ApplicationEvent event) {
	if (ContextRefreshedEvent.class.getName().equals(
		event.getClass().getName())) {

	    publish();
	}
    }

    private void publish() {
	BeanHolder.register(this.interfaceName, this.ref);

	// FIXME 这里仅仅启动NettyServer
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .childHandler(new NettyProviderrHandlerInitializer());

	    try {// FIXME
		b.bind(8081).sync().channel().closeFuture().sync();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	} finally {
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

    // public void setBeanName(String name) {
    // this.beanName = name;
    //
    // }
    //
    // public void setApplicationContext(ApplicationContext applicationContext)
    // throws BeansException {
    // this.applicationContext = applicationContext;
    //
    // }

    // public void destroy() throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // public void afterPropertiesSet() throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
}
