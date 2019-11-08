package com.collector.util.dao;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;

import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;
import com.core.util.DButil;


public class DataInput extends ParameterBasic  implements IStepRunnable {

	@Override
	@SetpRunnable(value = "DataInput")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		ApplicationContext ctx = Parameter.getApplicationContext(parameter);
		String sqlName = (String) ctx.getBean("sqlName");
		LogManager.getLogger(Parameter.getArgCfg(parameter)).debug(sqlName);
		DButil.execSql(sqlName);
		return true;
	}

	@Override
	protected void doSaveParameter() {
		
	}

}
