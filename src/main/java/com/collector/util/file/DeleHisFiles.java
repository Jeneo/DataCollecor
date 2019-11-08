package com.collector.util.file;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;
import com.core.util.FileUtil;

public class DeleHisFiles extends FilePoseBase implements IStepRunnable {
	@Override
	@SetpRunnable(value = "DeleHisFiles")
	public boolean execute(Map<String, Object> parameter) {
		Logger logger = LogManager.getLogger(Parameter.getArgCfg(parameter));
		String fileBackUpPath = Parameter.getBackUpPath(parameter);
		ApplicationContext ctx = Parameter.getApplicationContext(parameter);
		int saveHour = (int) ctx.getBean("SaveHour");

		List<File> list = FileUtil.showTimeoutFile(fileBackUpPath, saveHour * 60 * 60 * 1000, false);
		for (File file : list) {
			try {
				if (file.delete())
					logger.debug("ɾ��" + file.getName());
			} catch (Exception e) {
				logger.error("deleHis", e);
			}

		}
		return true;
	}
}
