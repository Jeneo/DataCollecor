package com.collector.entity;

public class ServerInfo {
	public static final int FTP = 1;
	public static final int SFTP = 2;

	private String servName;
	private String ip;
	private String user;
	private String pwd;
	public int ftpType;

	@Override
	public String toString() {
		return "ServerInof [servName=" + servName + ", ip=" + ip + ", user=" + user + ", pwd=" + pwd + ", ftpType="
				+ ftpType + "]";
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getFtpType() {
		return ftpType;
	}

	public void setFtpType(int ftpType) {
		this.ftpType = ftpType;
	}

	public String getServName() {
		return servName;
	}

	public void setServName(String servName) {
		this.servName = servName;
	}

	public ServerInfo(String servName, String ip, String user, String pwd, int ftpType) {
		super();
		this.servName = servName;
		this.ip = ip;
		this.user = user;
		this.pwd = pwd;
		this.ftpType = ftpType;
	}

	public ServerInfo() {
		super();

	}

}
