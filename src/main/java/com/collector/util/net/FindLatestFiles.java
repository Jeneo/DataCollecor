package com.collector.util.net;

import java.util.Map;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class FindLatestFiles extends FindFilesBasic implements IStepRunnable {

	@Override
	@SetpRunnable(value = "FindLatestFiles", paras = { Parameter.SERVERINFO_DCT_COLLS_DOWNLOAD })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		FindLatestFile();
		saveParameter();	
		return true;
	}
}
