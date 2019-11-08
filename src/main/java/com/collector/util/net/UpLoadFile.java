package com.collector.util.net;


import java.util.Map;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;

public class UpLoadFile extends UpLoadFileBasic implements IStepRunnable {
	
	@Override
	@SetpRunnable(value = "uploadFiles")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		upLoad();
		return true;
	}
}
