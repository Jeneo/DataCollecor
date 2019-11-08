package com.collector;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil extends ApplicationObjectSupport {
	public static ApplicationContext context;



	@Override
	protected void initApplicationContext(ApplicationContext context) throws BeansException {
		super.initApplicationContext(context);
		SpringUtil.context = context;
//		for (String s : context.getBeanDefinitionNames()) {
//			System.out.println(s);
//		}
//        System.out.println("=============================================="+SpringUtil.context+"==============================================");
	}

	public static Object getBean(String serviceName) {
		return context.getBean(serviceName);
	}
	
	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	public static <T> T getBean(String beanName, Class<T> beanClass) {
		return context.getBean(beanName, beanClass);
	}
}