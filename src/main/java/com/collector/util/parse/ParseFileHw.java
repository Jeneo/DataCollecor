package com.collector.util.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.server.IStepRunnable;
import com.collector.server.SetpRunnable;
import com.collector.util.Parameter;

public class ParseFileHw extends ParseFileBasic implements IStepRunnable {
	@Override
	@SetpRunnable(value = "解析", paras = { Parameter.SERVERINFO_DCT_COLLS_UPLOAD })
	public boolean execute(Map<String, Object> parameter) throws Exception {
		setParameter(parameter);
		List<ServerInfoDtcoll> lists = new ArrayList<ServerInfoDtcoll>();
		for (int i = 0; i < serverInoDtcollsForParse.size(); i++) {
			lists.add(serverInoDtcollsForParse.get(i));
		}
		String[] flNames = parseFiles(lists, "csv");
		parameterAdd(Arrays.asList(flNames));
		saveParameter();
		return true;
	}

}
