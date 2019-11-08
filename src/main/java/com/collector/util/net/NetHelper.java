package com.collector.util.net;

import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.collector.entity.ArgsFilesContent;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.entity.ServerInfo;
import com.core.pattern.DownloadMatchesFiles;
import com.core.pattern.FileContent;
import com.core.pattern.FilePatternManagerControllerImpl;
import com.core.pattern.FtpCommandControllerImpl;
import com.core.pattern.InterpreterContextPatternImpl;
import com.core.util.FtpHelper;
import com.core.util.IFtpHelperOnActivate;

public class NetHelper implements IFtpHelperOnActivate {
	private ServerInfoDtcoll serverInfoDtcoll;
	private int type;

	public NetHelper(ServerInfoDtcoll serverInfoDtcoll, int type) {
		super();
		this.serverInfoDtcoll = serverInfoDtcoll;
		this.type = type;
	}

	public NetHelper() {
		super();

	}

	public ServerInfoDtcoll getServerInfoDtcoll() {
		return serverInfoDtcoll;
	}

	public void setServerInfoDtcoll(ServerInfoDtcoll serverInfoDtcoll) {
		this.serverInfoDtcoll = serverInfoDtcoll;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private List<FileContent> ListPatternFilesFtp() throws Exception {
		FtpHelper ftpHelp = new FtpHelper(serverInfoDtcoll.getServerInof().getIp(), 21,
				serverInfoDtcoll.getServerInof().getUser(), serverInfoDtcoll.getServerInof().getPwd());
		@SuppressWarnings("unchecked")
		List<FileContent> files = (List<FileContent>) ftpHelp.execCommand(this);
		return files;
	}

	private List<FileContent> ListPatternFilesSftp() throws Exception {

		return null;
	}

	@Override
	public Object OnActivateForDo(FTPClient ftpclient) throws Exception {
		FilePatternManagerControllerImpl filePatternManagerImpl = new FilePatternManagerControllerImpl();
		filePatternManagerImpl.setInterpreterContextPattern(new InterpreterContextPatternImpl());
		filePatternManagerImpl.setPattern(serverInfoDtcoll.getServPath());
		filePatternManagerImpl.parseMatchPaths();
//		System.out.println(filePatternManagerImpl.toString());
		FtpCommandControllerImpl ftpCommandControllerImpl = new FtpCommandControllerImpl(ftpclient);
		DownloadMatchesFiles downloadMatchesFiles = new DownloadMatchesFiles(ftpCommandControllerImpl,
				filePatternManagerImpl);
		List<FileContent> files = downloadMatchesFiles.doFileFilter();
		return files;
	}

	public List<FileContent> ListPatternFilesCotent() throws Exception {
		if (this.type == ServerInfo.FTP)
			return ListPatternFilesFtp();
		else if (this.type == ServerInfo.SFTP)
			return ListPatternFilesSftp();
		return null;
	}

	public ArgsFilesContent ListPatternServerFilesContent() throws Exception {
		List<FileContent> fileContents = ListPatternFilesCotent();
		ArgsFilesContent serverFilesContent = new ArgsFilesContent();
		serverFilesContent.setServerInfo(serverInfoDtcoll.getServerInof());
		serverFilesContent.addFileContentAll(fileContents);
		return serverFilesContent;
	}
}
