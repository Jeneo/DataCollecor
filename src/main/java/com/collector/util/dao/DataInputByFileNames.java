package com.collector.util.dao;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;

import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;
import com.core.util.DButil;

public class DataInputByFileNames extends ParameterBasic implements IStepRunnable {

	@Override
	@SetpRunnable(value = "DataInputByFileNames")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		
//		System.out.println("sdfsdfsdfsdfsdfsdf");
		ApplicationContext ctx = Parameter.getApplicationContext(parameter);
		String sqlName = (String) ctx.getBean("sqlName");
		String[] serverInoDtcollsForUpload = Parameter.getServerInoDtcollsForUpload(parameter);
		String args = "";
		for (String arg : serverInoDtcollsForUpload) {
			args = args + arg + ",";
		}
		if (args.length() > 0) {
			args = args.substring(0, args.length() - 1);
		}
		sqlName = sqlName + " '" + args+"'";
		LogManager.getLogger(Parameter.getArgCfg(parameter)).debug(sqlName);
		DButil.execSql(sqlName);
		return true;
	}

	@Override
	protected void doSaveParameter() {

	}

}
