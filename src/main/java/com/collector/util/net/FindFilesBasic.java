package com.collector.util.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.collector.entity.ArgsFilesContent;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.util.Filecompare;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;

public class FindFilesBasic extends ParameterBasic {
	private Logger logger = LogManager.getLogger(this.getClass());
	protected final List<ArgsFilesContent> serverInoDtcollsForDownLoad = new ArrayList<ArgsFilesContent>();
	protected List<List<ServerInfoDtcoll>> llsServs;

	public List<ArgsFilesContent> getServerInoDtcollsForDownLoad() {
		return serverInoDtcollsForDownLoad;
	}

	public List<List<ServerInfoDtcoll>> getLlsServs() {
		return llsServs;
	}

	public void setLlsServs(Map<String, Object> parameter) {
		this.llsServs = Parameter.getCollectOmcs(parameter);
	}

	protected ArgsFilesContent doFindFile(ServerInfoDtcoll sid) throws Exception {
		logger.debug("ServInfo:" + sid);
		NetHelper patternFilesHelpler = new NetHelper();
		patternFilesHelpler.setServerInfoDtcoll(sid);
		patternFilesHelpler.setType(sid.getServerInof().getFtpType());
		return patternFilesHelpler.ListPatternServerFilesContent();
	}

	protected void FindFile() throws Exception {
		for (List<ServerInfoDtcoll> list : llsServs) {
			List<ArgsFilesContent> lsidlDtcolls1 = new ArrayList<ArgsFilesContent>();
			for (ServerInfoDtcoll sid : list) {
				ArgsFilesContent argsFilesContent = doFindFile(sid);
				lsidlDtcolls1.add(argsFilesContent);
			}
			saveFindFile(false, list.size(), lsidlDtcolls1);
		}
	}

	protected void FindLatestFile() throws Exception {
		for (List<ServerInfoDtcoll> list : llsServs) {
			FindLatestFile(list);
		}
	}

	protected void FindLatestFile(List<ServerInfoDtcoll> list) throws Exception {
		List<ArgsFilesContent> lsidlDtcolls1 = new ArrayList<ArgsFilesContent>();
		for (ServerInfoDtcoll sid : list) {
			ArgsFilesContent argsFilesContent = doFindFile(sid);
			lsidlDtcolls1.add(argsFilesContent);
		}
		if (lock != null) {
			lock.lock();
			try {
				saveFindFile(true, list.size(), lsidlDtcolls1);
			} finally {
				lock.unlock();
			}
		} else {
			saveFindFile(true, list.size(), lsidlDtcolls1);
		}
	}

	private boolean saveFindFile(boolean isLatest, int size, List<ArgsFilesContent> argsFilesContents)
			throws Exception {
		if (!isLatest) {
			return serverInoDtcollsForDownLoad.addAll(argsFilesContents);
		}
		if (size == 1 && isLatest) {
			return serverInoDtcollsForDownLoad.add(argsFilesContents.get(0));
		}
		if (size > 1 && isLatest) {
			return serverInoDtcollsForDownLoad.add(Filecompare.FilecompareLatest(argsFilesContents));
		}
		return false;
	}

	@Override
	public void setParameter(Map<String, Object> parameter) {
		super.setParameter(parameter);
		setLlsServs(parameter);
	}

	@Override
	public void doSaveParameter() {
		parameter.put(Parameter.SERVERINFO_DCT_COLLS_DOWNLOAD, serverInoDtcollsForDownLoad);
	}
}
