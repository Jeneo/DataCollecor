package com.collector.util.compress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;

public class DecompressionGzTarServerFilesBasic extends ParameterBasic {
	protected Logger logger;
	protected final List<ServerInfoDtcoll> serverInoDtcollsForParse = new ArrayList<ServerInfoDtcoll>();

	public List<ServerInfoDtcoll> getServerInoDtcollsForDeal() {
		return serverInoDtcollsForDeal;
	}

	public void setServerInoDtcollsForDeal(Map<String, Object> parameter) {
		this.serverInoDtcollsForDeal = Parameter.getServerInoDtcollsForDeal(parameter);
	}

	public List<ServerInfoDtcoll> getServerInoDtcollsForParse() {
		return serverInoDtcollsForParse;
	}

	protected List<ServerInfoDtcoll> serverInoDtcollsForDeal;

	@Override
	public void setParameter(Map<String, Object> parameter) {
		super.setParameter(parameter);
		setServerInoDtcollsForDeal(parameter);
		logger=LogManager.getLogger(Parameter.getArgCfg(parameter));
	}

	protected void unGzTarFile() throws Exception {
		for (ServerInfoDtcoll sid : serverInoDtcollsForDeal) {
			unGzTarFile(sid);
		}
	}

	protected void unGzTarFile(ServerInfoDtcoll sid) throws Exception {
		logger.debug("开始解压.." + sid.getLocalFileFullName());
		String fileString = GzTarHw.unGzTarFile(sid.getLocalFileFullName(), sid.getLocalPath());
		if (fileString != null && fileString.length() > 0) {
			sid.setLocalFileName(fileString);
			serverInoDtcollsForParse.add(sid);
		}
	}

	@Override
	protected void doSaveParameter() {
		parameter.put(Parameter.SERVERINFO_DCT_COLLS_PARSE, serverInoDtcollsForParse);

	}

}
