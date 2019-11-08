package com.core.util;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



//import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


/**
 * 
 * 类说明 sftp工具类
 * 
 */

public class SftpHelper {
	private String host;
	private int port;
	private String username;
	private String password;
	private String privateKey;

	public SftpHelper(String username, String password, String host, int port) {

		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	public SftpHelper(String username, String host, int port, String privateKey) {

		this.username = username;
		this.host = host;
		this.port = port;
		this.privateKey = privateKey;
	}

	public SftpHelper() {
	}

	public void upload(String remoteFileName, String locaFileName) throws Exception {
		ChannelSftp sftp;
		sftp = connect();
		try {
			upload(sftp, remoteFileName, locaFileName);

		} finally {
			logout(sftp);
		}
	}

	public void download(String remoteFileName, String locaFileName) throws Exception {
		ChannelSftp sftp;
		sftp = connect();
		try {
			download(sftp, remoteFileName, locaFileName);
		} finally {
			logout(sftp);
		}

	}

	private void logout(ChannelSftp sftp) throws Exception {
		Session session = null;
		if (sftp != null) {
			if (sftp.isConnected()) {
				session = sftp.getSession();
				sftp.disconnect();
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}

	private void download(ChannelSftp sftp, String remoteFileName, String locaFileName) throws Exception {
		OutputStream output = null;
		try {
			output = new FileOutputStream(locaFileName);
			sftp.get(remoteFileName, output);
		} finally {
			if (output != null)
				output.close();
		}
	}

	private void upload(ChannelSftp sftp, String remoteFileName, String locaFileName) throws Exception {
		InputStream input = null;
		try {
			input = new FileInputStream(locaFileName);
			sftp.put(input, remoteFileName);
		} finally {
			if (input != null)
				input.close();
		}

	}

	private ChannelSftp connect() throws JSchException {
		Session session;
		try {
			JSch jsch = new JSch();
			if (privateKey != null) {
				jsch.addIdentity(privateKey);// 设置私钥
			}
			session = jsch.getSession(username, host, port);
			if (password != null) {
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			return (ChannelSftp) channel;
		} finally {

		}
	}

}
