package com.collector.util.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.collector.entity.ArgsFilesContent;
import com.collector.entity.ServerInfo;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;
import com.core.pattern.FileContent;
import com.core.util.FileUtil;
import com.core.util.FtpHelper;

public class DownLoadBasic extends ParameterBasic {
	protected final List<ArgsFilesContent> servs = new ArrayList<>();
	protected String sourcePath;
	protected final List<ServerInfoDtcoll> localFileContents = new ArrayList<ServerInfoDtcoll>();

	protected String getSetSourcePath() {
		return sourcePath;
	}

	protected void setSourcePath(Map<String, Object> parameter) {
		this.sourcePath = Parameter.getSourcePath(parameter);
	}

	protected void addServs(Map<String, Object> parameter) {
		;
		List<ArgsFilesContent> servs = Parameter.getServerInoDtcollsForDownLoad(parameter);
		addServs(servs);
	}

	protected void addServs(List<ArgsFilesContent> servs) {
		this.servs.clear();
		this.servs.addAll(servs);
	}

	protected List<ArgsFilesContent> getServs() {
		return servs;
	}

	@Override
	public void setParameter(Map<String, Object> parameter) {
		super.setParameter(parameter);
		setSourcePath(parameter);
		addServs(parameter);
	}

	protected void checkFolderPath() throws IOException {
		for (ArgsFilesContent argsFilesContent : servs) {
			String localPath = getLocalPath(argsFilesContent.getServerInfo());
			FileUtil.createDirSmart(localPath);
		}
	}

	protected String getLocalPath(ServerInfo serverInfo) {
		return sourcePath + serverInfo.getIp() + "/";
	}

	protected ServerInfoDtcoll getServerInfoDtcoll(ServerInfo serverInfo, FileContent fileContent) {
		ServerInfoDtcoll serverInfoDtcoll = new ServerInfoDtcoll();
		serverInfoDtcoll.setServerInof(serverInfo);
		serverInfoDtcoll.setServPath(fileContent.getPath());
		serverInfoDtcoll.setServFileName(fileContent.getFileFullName());
		serverInfoDtcoll.setLocalPath(getLocalPath(serverInfo));
		serverInfoDtcoll.setLocalFileName(fileContent.getFileName());
		return serverInfoDtcoll;
	}

	protected void addLocalFileContents() {
		for (ArgsFilesContent argsFilesContent : servs) {
			addLocalFileContents(argsFilesContent);
		}
	}

	protected void addLocalFileContents(ArgsFilesContent argsFilesContent) {
		List<FileContent> filesContents = argsFilesContent.getFileContents();
		for (FileContent fileContent : filesContents) {
			ServerInfoDtcoll serverInfoDtcoll = getServerInfoDtcoll(argsFilesContent.getServerInfo(), fileContent);
			if (lock == null)
				localFileContents.add(serverInfoDtcoll);
			else {
				try {
					lock.lock();
					localFileContents.add(serverInfoDtcoll);
				} finally {
					lock.unlock();
				}
			}

		}
	}

	protected void downLoad() throws Exception {
		for (ArgsFilesContent argsFilesContent : servs) {
			downLoad(argsFilesContent);
		}
	}

	protected void downLoad(ArgsFilesContent argsFilesContent) throws Exception {
		Map<String, String> map = new HashMap<>();
		List<FileContent> filesContents = argsFilesContent.getFileContents();
		ServerInfo serverInfo = argsFilesContent.getServerInfo();
		for (FileContent fileContent : filesContents) {
			map.put(fileContent.getFileFullName(), getLocalPath(serverInfo) + fileContent.getFileName());
		}
		FtpHelper ftpHelp = new FtpHelper(serverInfo.getIp(), 21, serverInfo.getUser(), serverInfo.getPwd());
		ftpHelp.download(map);
	}

	@Override
	protected void doSaveParameter() {
		parameter.put(Parameter.SERVERINFO_DCT_COLLS_DEAL, localFileContents);

	}

}
