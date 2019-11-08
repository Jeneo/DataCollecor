package com.collector.util.compress;

import java.util.Map;

import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class DecompressionGzTarServerFiles extends DecompressionGzTarServerFilesBasic implements IStepRunnable {
	@Override
	@SetpRunnable(value = "解压", paras = { Parameter.SERVERINFO_DCT_COLLS_PARSE })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		unGzTarFile();
		saveParameter();
		return true;
	}

}
