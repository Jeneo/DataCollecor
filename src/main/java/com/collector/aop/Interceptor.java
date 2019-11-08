package com.collector.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.collector.server.Notity;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class Interceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {

		Date dtBegin = new Date();
		Object object = null;
		Method[] methods = arg0.getThis().getClass().getDeclaredMethods();
		SetpRunnable sr = null;
		Method methodExec = null;
		for (Method method : methods) {
			if (method.getName().equals("execute"))
				methodExec = method;
		}

		sr = methodExec.getAnnotation(SetpRunnable.class);
		String step = null;
		String[] paras = null;
		if (sr != null) {
			paras = sr.paras();
			step = sr.value();
		}
		if (step == null || step.equals(""))
			step = arg0.getThis().getClass().toString();

		@SuppressWarnings("unchecked")
		Map<String, Object> parameter = (Map<String, Object>) arg0.getArguments()[0];
		Logger logger = LogManager.getLogger(Parameter.getArgCfg(parameter));
		try {
			logger.debug(step + "开始..");
			object = arg0.proceed();

//			if (paras != null) {
//				for (String string : paras) {
//					if (parameter.containsKey(string))
//						DataCollectionlogger.debug(string + "=" + parameter.get(string).toString());
//				}
//			}
			Date dtEnd = new Date();
			logger.debug(
					String.format("%s结束(共用%.3f秒)..%n", step, (double) (dtEnd.getTime() - dtBegin.getTime()) / 1000));
			;
		} catch (Exception e) {
			String msg = String.format("%s %s %s 执行失败", Parameter.getArgCfg(parameter), methodExec.toString(), step);
			logger.error(msg);
			logger.error(e, e.fillInStackTrace());
//			Notity.smsNotityIfExceptoin(msg + e.getMessage());
			object = false;
		}
		return object;
	}
}
