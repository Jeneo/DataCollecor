package com.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.Map;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpHelper {
	private String server;
	private int port;
	private String username;
	private String password;
	private boolean isPrintCommands = false;
	private String encoding = "utf-8";
	private String fileNameEncoding = "iso-8859-1";

	private Logger logger = LogManager.getLogger(FtpHelper.class);

	public boolean PrintCommands() {
		return isPrintCommands;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isPrintCommands() {
		return isPrintCommands;
	}

	public void setPrintCommands(boolean isPrintCommands) {
		this.isPrintCommands = isPrintCommands;
	}

	public FtpHelper() {
	}

	public FtpHelper(String server, int port, String username, String password) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public Object execCommand(IFtpHelperOnActivate iFtpHelperOnActivate) throws Exception {

		return execCommand((String) null, iFtpHelperOnActivate);

	}

	public Object execCommand(String path, IFtpHelperOnActivate iFtpHelperOnActivate) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		Object object = null;
		try {
			connect(ftp);
			setProperty(ftp);
			if (path != null)
				if (!ftp.changeWorkingDirectory(path)) {
					throw new Exception("路径错误." + path);
				}
			object = iFtpHelperOnActivate.OnActivateForDo(ftp);
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
		return object;

	}

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		FTPFile[] ftpFiles = null;
		try {
			connect(ftp);
			setProperty(ftp);
			if (filter == null)
				ftpFiles = ftp.listFiles(pathname);
			else
				ftpFiles = ftp.listFiles(pathname, filter);
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
		return ftpFiles;
	}

	public FTPFile[] listPathFiles(String path, FTPFileFilter filter) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		FTPFile[] ftpFiles = null;
		try {
			connect(ftp);
			setProperty(ftp);
			if (!ftp.changeWorkingDirectory(path.toString())) {
				throw new Exception("路径错误." + path.toString());
			}
			if (filter == null)
				ftpFiles = ftp.listFiles(null);
			else
				ftpFiles = ftp.listFiles(null, filter);
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
		return ftpFiles;
	}

	public void upload(String remoteFileName, String locaFileName) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		try {
			connect(ftp);
			setProperty(ftp);
			upload(ftp, remoteFileName, locaFileName);
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
	}

	public void upload(Map<String, String> files) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		try {
			connect(ftp);
			setProperty(ftp);
			for (Map.Entry<String, String> entry : files.entrySet()) {
				String remoteFileName = entry.getKey();
				String locaFileName = entry.getValue();
				upload(ftp, remoteFileName, locaFileName);
			}
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
	}

	public void download(String remoteFileName, String locaFileName) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		try {
			connect(ftp);
			setProperty(ftp);
			download(ftp, remoteFileName, locaFileName);
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
	}

	public void download(Map<String, String> files) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		try {
			connect(ftp);
			setProperty(ftp);
			for (Map.Entry<String, String> entry : files.entrySet()) {
				String remoteFileName = entry.getKey();
				String locaFileName = entry.getValue();
				download(ftp, remoteFileName, locaFileName);
			}
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
	}

	public void mkdir(String remotePathName) throws Exception {
		FTPClient ftp = null;
		ftp = new FTPClient();
		try {
			connect(ftp);
			setProperty(ftp);
			remotePathName = new String(remotePathName.getBytes(encoding), fileNameEncoding);
			mkdir(ftp, remotePathName);
			logout(ftp);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
		}
	}

	public boolean isDirectoryExists(FTPClient ftp, String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftp.listFiles(path);
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private void mkdir(FTPClient ftp, String remotePathName) throws Exception {
		ftp.makeDirectory(remotePathName);
	}

	private void download(FTPClient ftp, String remoteFileName, String locaFileName) throws Exception {
		String logs = String.format("准备下载: %s 到 %s", remoteFileName, locaFileName);
		OutputStream output = null;
		remoteFileName = new String(remoteFileName.getBytes(encoding), fileNameEncoding);
		try {
			FTPFile[] ftpFile = ftp.listFiles(remoteFileName);
			if (ftpFile == null)
				throw new Exception(remoteFileName + " 下载文件不存在!");
			logger.debug(String.format("大小:(  %s )" + logs, FileUtil.formatFileSize(ftpFile[0].getSize())));
			output = new FileOutputStream(locaFileName);
			if (!ftp.retrieveFile(remoteFileName, output)) {
				throw new Exception(remoteFileName + " 下载失败!");
			}
		} finally {
			if (output != null)
				output.close();
		}
	}

	private void setProperty(FTPClient ftp) throws Exception {
		ftp.enterLocalPassiveMode();			// 设置被动模式
		ftp.setControlEncoding(encoding);		// 设置编码模式
		ftp.setFileType(FTP.BINARY_FILE_TYPE);// 设置二进制传输模式
	}

	private void logout(FTPClient ftp) throws Exception {
		ftp.noop();
		ftp.logout();
	}

	private void upload(FTPClient ftp, String remoteFileName, String locaFileName) throws Exception {
		String logs = String.format("准备上传: %s 到 %s", locaFileName, remoteFileName);
		InputStream input = null;
		remoteFileName = new String(remoteFileName.getBytes(encoding), fileNameEncoding);
		try {
			input = new FileInputStream(locaFileName);
			logger.debug(
					String.format("大小:(  %s )" + logs, FileUtil.formatFileSize(FileUtil.getFileSize(locaFileName))));
			ftp.storeFile(remoteFileName, input);
		} finally {
			if (input != null)
				input.close();
		}
	}

	private void connect(FTPClient ftp) throws Exception {
		if (isPrintCommands)
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
		// 设定连接超时时间
		ftp.setConnectTimeout(15 * 1000);
		int n = 0;
		while (n <= 3) {
			n++;
			ftp.enterLocalPassiveMode();
			try {
				ftp.connect(server, port);
				int reply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftp.disconnect();
					throw new ConnectException(server + " 服务器拒绝连接");
				}
			} catch (Exception e) {
				if (n > 3)
					throw new Exception(server + " 服务器拒绝连接");
			}

			if (ftp.isConnected())
				break;
			logger.debug("connect fail ,try to  connect again");
			Thread.sleep(3000);
		}
		if (!ftp.login(username, password)) {
			throw new ConnectException("用户名或密码错误:" + username + " " + password);
		}
	}

}