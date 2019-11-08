package com.collector.util.net;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.collector.entity.ArgsFilesContent;

import com.collector.server.IStepRunnable;

import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class DownLoadFileThread extends DownLoadBasic implements IStepRunnable {
	private Lock lock = new ReentrantLock();

	@Override
	@SetpRunnable(value = "обтьнд╪Ч")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setLock(lock);
		setParameter(parameter);
		checkFolderPath();
		CompletionService<Boolean> service = new ExecutorCompletionService<>(Parameter.getExecutorService());
		for (ArgsFilesContent argsFilesContent : servs) {
			service.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					addLocalFileContents(argsFilesContent);
					downLoad(argsFilesContent);
					return true;
				}
			});
		}
		for (int i = 0; i < servs.size(); i++) {
			service.take().get();
		}
		saveParameter();
		return true;
	}

}
