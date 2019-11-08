package com.core.pattern;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpCommandControllerImpl implements CommandController {
	private FTPClient ftpClient;

	public FtpCommandControllerImpl() {
	}

	public FtpCommandControllerImpl(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	@Override
	public String changePath(String path) throws Exception {
		path=path.replaceAll("^(/)*", "/");		//如果开头出现多个/ 统一替换成一个/
		if (!path.startsWith("/"))
			path = "/" + path;
		ftpClient.changeWorkingDirectory(path);
		String workingDirectory = path ;
//		System.out.println("workingDirectory="+workingDirectory);
		return workingDirectory;
	}

	@Override
	public List<FileContent> listFile(String path) throws Exception {
		path=path.replaceAll("^(/)*", "/");		//如果开头出现多个/ 统一替换成一个/
		if (!path.startsWith("/"))
			path = "/" + path;
		List<FileContent> fileContents = new ArrayList<FileContent>();
		FTPFile[] ftpFiles = ftpClient.listFiles();
		if (path.equals("/")) {
			path = "";
		}
		if (ftpFiles != null)
			for (FTPFile ftpFile : ftpFiles) {
				fileContents.add(new FileContent(ftpFile.getName(), path, ftpFile.getSize(), ftpFile.getTimestamp(),
						ftpFile.isFile()));
			}
		return fileContents;
	}

}
