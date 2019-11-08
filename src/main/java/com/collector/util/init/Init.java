package com.collector.util.init;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;
import com.core.util.DButil;
import com.core.util.Dbserver;
import com.core.util.FileUtil;

public class Init implements IStepRunnable {

	@Override
	@SetpRunnable(value = "初始化", paras = { Parameter.SOURCEPATH, Parameter.BCPFILEPATH, Parameter.BACKUPPATH })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		ApplicationContext ctx = Parameter.getApplicationContext(parameter);
		DButil.dbserver = (Dbserver) ctx.getBean("Dbserver");

		String bcpFilePath = Parameter.getRootPath(parameter) + "/bcpFile/";
		String sourceFilePath = Parameter.getRootPath(parameter) + "/source/";
		String backUpFilePath = Parameter.getRootPath(parameter) + "/backup/";

		FileUtil.emptyPath(bcpFilePath);
		FileUtil.emptyPath(sourceFilePath);
		FileUtil.createDirSmart(bcpFilePath);
		FileUtil.createDirSmart(sourceFilePath);
		FileUtil.createDirSmart(backUpFilePath);

		parameter.put(Parameter.BCPFILEPATH, bcpFilePath);
		parameter.put(Parameter.SOURCEPATH, sourceFilePath);
		parameter.put(Parameter.BACKUPPATH, backUpFilePath);

		return true;
	}

}
