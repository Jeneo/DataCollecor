package com.collector.server;

import com.collector.util.Parameter;
import com.collector.util.SingleJob;

public class ServerDataCollector {

	public static void main(String[] args) throws Exception {
		String className;
		if (args.length == 0 || args.length != 1) {
			System.out.println("parameterClass is empty:  className   ");
			return;
		} else {
			className = args[0];
			System.out.println("parameterClass  className = " + className);
		}

		
		App.loadLogConfig();

		SingleJob singleJob =new SingleJob();
		singleJob.setParam(className);
		singleJob.executeInternal(null);
		Parameter.getExecutorService().shutdown();
	}
}
