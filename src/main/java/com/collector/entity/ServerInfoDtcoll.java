package com.collector.entity;



public class ServerInfoDtcoll {
	public ServerInfo getServerInof() {
		return serverInof;
	}

	public void setServerInof(ServerInfo serverInof) {
		this.serverInof = serverInof;
	}

	public String getServPath() {
		return servPath;
	}

	public void setServPath(String servPath) {	
		this.servPath = servPath;
	}

	public String getServFileName() {
		return servFileName;
	}

	public void setServFileName(String servFileName) {
		this.servFileName = servFileName;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		if (!localPath.endsWith("/") && !!localPath.endsWith("\\")) {
			localPath = localPath + "/";
		}
		this.localPath = localPath;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}
	
	public String getLocalFileFullName() {
		
		return localPath+localFileName;
	}

	public String getServFileFullName() {
		
		return servPath+servFileName;
	}
	
	private ServerInfo serverInof;
	private String servPath;
	private String servFileName;
	private String localPath;
	private String localFileName;

	@Override
	public String toString() {
		return "ServerInfoDtcoll [serverInof=" + serverInof + ", servPath=" + servPath + ", servFileName="
				+ servFileName + ", localPath=" + localPath + ", localFileName=" + localFileName + "]";
	}

}
