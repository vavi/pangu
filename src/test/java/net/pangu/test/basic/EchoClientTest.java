package net.pangu.test.basic;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EchoClientTest {

    @Test
    public void testEcho() {
	ApplicationContext context = new ClassPathXmlApplicationContext(
		"pangu-consumer-spring.xml");

	HelloService hello = context.getBean("hello", HelloService.class);

	String result = hello.say("Hi,I'm Client.");

	Assert.assertEquals("Server Echo:Hi,I'm Client.", result);
    }
}
