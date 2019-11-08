package com.collector.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.collector.server.App;
import com.collector.server.IStepRunnable;
import com.collector.server.ParameterMapProxy;
import com.collector.util.init.InitLogger;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SingleJob extends QuartzJobBean {
	private String Version;
	private String param;

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String className = param;
		String Path = App.path + "/File/" + className;
		Logger logger = LogManager.getLogger(className);

		if (!new InitLogger(className).createLogger())
			logger.warn("日志创建失败:" + className);

		logger = LogManager.getLogger(className);
		Date dtBegin = new Date();
		logger.debug("==============================================");
		ParameterMapProxy<String, Object> parameterMapProxy = new ParameterMapProxy<String, Object>(
				new HashMap<String, Object>());
		Map<String, Object> map = parameterMapProxy.ParameterMapProxyInstance();
		parameterMapProxy.putSysArgs(Parameter.ARGCFG, className);
		parameterMapProxy.putSysArgs(Parameter.ROOTPATH, Path);

		String[] contextFileNames = { "/beanCfg/" + "Server.xml", "/beanCfg/" + className + ".xml" };
		logger.debug("config info:" + Arrays.toString(contextFileNames));
		ApplicationContext ctx = new ClassPathXmlApplicationContext(contextFileNames);
		parameterMapProxy.putSysArgs("ApplicationContext", ctx);
//		if (1==1)return;
		boolean bRun = true;
		@SuppressWarnings("unchecked")
		ArrayList<Object> alRunnable = (ArrayList<Object>) ctx.getBean("stepRunnable");
		for (Object object : alRunnable) {
			if (com.collector.server.IStepRunnable.class.isAssignableFrom(object.getClass())) {
				IStepRunnable iStepRunnable = (IStepRunnable) object;
				System.out.println("开始加载" + iStepRunnable.toString());
			} else {
				bRun = false;
				logger.warn(object.getClass() + " 必须实现" + com.collector.server.IStepRunnable.class + "接口");
			}
		}

		if (bRun) {
			logger.debug("开始执行 .....");
			for (Object object : alRunnable) {
				IStepRunnable iStepRunnable = (IStepRunnable) object;
				boolean result;
				try {
					result = iStepRunnable.execute(map);
				} catch (Exception e) {
					logger.error(iStepRunnable.getClass().toString(), e);
					result = false;
				}
				if (!result)
					break;
			}
		}
		Date dtEnd = new Date();
		logger.debug(String.format("结束 (共用%.3f秒)......", (double) (dtEnd.getTime() - dtBegin.getTime()) / 1000));
		((AbstractApplicationContext) ctx).close();
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "SingleJob [Version=" + Version + ", param=" + param + "]";
	}

}
