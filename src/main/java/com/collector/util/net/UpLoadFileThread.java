package com.collector.util.net;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class UpLoadFileThread extends UpLoadFileBasic implements IStepRunnable {

	@Override
	@SetpRunnable(value = "uploadFiles")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		CompletionService<Boolean> service = new ExecutorCompletionService<>(Parameter.getExecutorService());
		for (String fl : serverInoDtcollsForUpload) {
			service.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					upLoad(fl);
					return true;
				}
			});
		}
		for (int i = 0; i < serverInoDtcollsForUpload.length; i++) {
			service.take().get();
		}
		return true;
	}
}
