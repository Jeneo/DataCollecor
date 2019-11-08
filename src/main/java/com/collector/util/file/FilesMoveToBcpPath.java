package com.collector.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class FilesMoveToBcpPath extends FilePoseBase implements IStepRunnable {

	@Override
	@SetpRunnable(value = "FilesMoveToBcpPath")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		Logger logger = LogManager.getLogger(Parameter.getArgCfg(parameter));
		List<ServerInfoDtcoll> files = com.collector.util.Parameter.getServerInoDtcollsForDeal(parameter);
		String bcpFilePath = Parameter.getBcpFilePath(parameter);
		List<String> wfiles = new ArrayList<String>();
		for (ServerInfoDtcoll sid : files) {
			logger.debug("move " + sid.getServFileName());
			File file = new File(sid.getLocalFileFullName());
			if (!file.renameTo(new File(bcpFilePath + file.getName())))
				throw new Exception("·FilesMoveToBcpPath faild");
			wfiles.add(file.getName());
		}
		parameter.put(Parameter.SERVERINFO_DCT_COLLS_UPLOAD, wfiles.toArray(new String[wfiles.size()]));
		return true;
	}

}
