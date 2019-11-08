package com.collector.util.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class ParseFileHwThread extends ParseFileBasic implements IStepRunnable {
	@Override
	@SetpRunnable(value = "解析", paras = { Parameter.SERVERINFO_DCT_COLLS_UPLOAD })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		CompletionService<String[]> service = new ExecutorCompletionService<>(Parameter.getExecutorService());
		for (int i = 0; i < serverInoDtcollsForParse.size(); i++) {
			int j = i;
			List<ServerInfoDtcoll> lists = new ArrayList<ServerInfoDtcoll>();
			lists.add(serverInoDtcollsForParse.get(i));
			service.submit(new Callable<String[]>() {
				@Override
				public String[] call() throws Exception {
					return parseFiles(lists, String.valueOf(j));
				}
			});
		}
		for (int i = 0; i < serverInoDtcollsForParse.size(); i++) {
			String[] flNames = service.take().get();
			parameterAdd(Arrays.asList(flNames));
		}
		if (!checkParameterValeu()) {
			throw new Exception("文件不存在......");
		}
		saveParameter();
		return true;
	}

}
