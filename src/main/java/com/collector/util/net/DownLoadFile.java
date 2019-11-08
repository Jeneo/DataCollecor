package com.collector.util.net;



import java.util.Map;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;


public class DownLoadFile extends DownLoadBasic implements IStepRunnable {
	@Override
	@SetpRunnable(value = "下载文件")
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		checkFolderPath();
		addLocalFileContents();
		downLoad();
		saveParameter();
		return true;
	}

}
