package com.collector.aop;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;



public class ThrowAdvice implements ThrowsAdvice{
	public void afterThrowing(Method method, Object[] args, Object target,Throwable ex) 
	{
		System.out.println("========Throwable ex==============================");
//		System.out.println(method);
//		System.out.println("target="+target);		
		System.out.println("========Throwable ex      end   ==============================");
	}
	
	
}
