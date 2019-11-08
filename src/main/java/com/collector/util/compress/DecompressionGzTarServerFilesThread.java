package com.collector.util.compress;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class DecompressionGzTarServerFilesThread extends DecompressionGzTarServerFilesBasic implements IStepRunnable {


	@Override
	@SetpRunnable(value = "½âÑ¹ÎÄ¼þ", paras = { Parameter.SERVERINFO_DCT_COLLS_PARSE })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		CompletionService<Boolean> service = new ExecutorCompletionService<>(Parameter.getExecutorService());
		for (ServerInfoDtcoll sid : serverInoDtcollsForDeal) {
			service.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					unGzTarFile(sid);
					return true;
				}
			});
		}
		for (int i = 0; i < serverInoDtcollsForDeal.size(); i++) {
			service.take().get();
		}
		saveParameter();
		return true;
	}

}
