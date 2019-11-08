package com.collector.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.aop.AfterReturningAdvice;




public class AfterAdivice implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		System.out.println("========after======");

		System.out.println(arg0.toString());
		System.out.println(arg1);
		System.out.println(Arrays.toString(arg2));
		System.out.println(arg3);
	}

}
