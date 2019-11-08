package com.collector.server;

import java.util.Map;

public interface IStepRunnable {
	 boolean execute(Map<String, Object> parameter) throws Exception;
}
