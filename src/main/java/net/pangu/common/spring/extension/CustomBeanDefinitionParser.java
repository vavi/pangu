package net.pangu.common.spring.extension;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class CustomBeanDefinitionParser implements BeanDefinitionParser {
    private final Class<?> beanClass;

    public CustomBeanDefinitionParser(Class<?> beanClass) {
	this.beanClass = beanClass;
    }

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
}