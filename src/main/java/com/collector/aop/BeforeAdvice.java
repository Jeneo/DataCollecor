package com.collector.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;



/**
 * before
 *
 */
public class BeforeAdvice implements MethodBeforeAdvice {
	
	/**
	 * 
	 * @param method 被代理的目标的方法
	 * @param args   传递给目标方法的参数
	 * @param obj    被代理的目标对象
	 * 
	 * @throws Throwable
	 */
	@Override
	public void before(Method method, Object[] args, Object obj) throws Throwable {
		System.out.println("========before======");
		System.out.println(method);
		System.out.println(obj);
	}	
}