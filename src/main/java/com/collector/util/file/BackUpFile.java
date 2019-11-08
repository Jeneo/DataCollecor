package com.collector.util.file;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class BackUpFile extends FilePoseBase implements IStepRunnable {
	@Override
	@SetpRunnable(value = "BackUpFile")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		Logger logger = LogManager.getLogger(Parameter.getArgCfg(parameter));
		List<ServerInfoDtcoll> backUpFiles = com.collector.util.Parameter.getServerInoDtcollsForDeal(parameter);
		String backUpPath = Parameter.getBackUpPath(parameter);
		for (ServerInfoDtcoll sid : backUpFiles) {
			logger.debug("����" + sid.getServerInof().getIp() + " " + sid.getServFileFullName());
			File sourceFile = new File(sid.getLocalFileFullName());
			File targetFile = new File(backUpPath + sid.getServerInof().getIp() + "_" + sid.getLocalFileName());
			if (!targetFile.exists()) {
				Files.copy(sourceFile.toPath(), targetFile.toPath());
			}

		}
		return true;
	}
}
