package net.pangu.common.spring.extension;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CustomNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
	registerBeanDefinitionParser("consumer",
		new CustomBeanDefinitionParser(ConsumerConfig.class));
	registerBeanDefinitionParser("provider",
		new CustomBeanDefinitionParser(ProviderConfig.class));
    }
}