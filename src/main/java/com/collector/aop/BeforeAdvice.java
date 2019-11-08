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
	 * @param method �������Ŀ��ķ���
	 * @param args   ���ݸ�Ŀ�귽���Ĳ���
	 * @param obj    �������Ŀ�����
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