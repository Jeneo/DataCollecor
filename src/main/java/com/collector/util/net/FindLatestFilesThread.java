package com.collector.util.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class FindLatestFilesThread extends FindFilesBasic implements IStepRunnable, Callable<Boolean> {
	private Lock lock = new ReentrantLock();
	private List<List<ServerInfoDtcoll>> llsServsTemp = new ArrayList<List<ServerInfoDtcoll>>();
	@Override
	@SetpRunnable(value = "FindLatestFilesThread", paras = { Parameter.SERVERINFO_DCT_COLLS_DOWNLOAD })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		setLock(lock);
		llsServsTemp.addAll(getLlsServs());
		CompletionService<Boolean> service = new ExecutorCompletionService<>(Parameter.getExecutorService());
		int count = llsServsTemp.size();
		for (int i = 0; i < count; i++) {
			service.submit(this);
		}
		for (int i = 0; i < count; i++) {
			service.take().get();
		}
		saveParameter();
		return true;
	}

	@Override
	public Boolean call() throws Exception {
		List<ServerInfoDtcoll> list = null;
		try {
			lock.lock();
			if (!llsServsTemp.isEmpty())
				list = llsServsTemp.remove(0);
		} finally {
			lock.unlock();
		}
		if (list == null) {
			return true;
		}
		FindLatestFile(list);
		return true;

	}
}
