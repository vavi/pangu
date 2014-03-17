package net.pangu.test.basic;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

public class EchoServerTest {

    @Test
    public void testEcho() {
	ApplicationContext context = new ClassPathXmlApplicationContext(
		"pangu-provider-spring.xml");

	Object obj = context.getBean("net.pangu.test.basic.HelloService");

	Assert.notNull(obj);

    }
}
