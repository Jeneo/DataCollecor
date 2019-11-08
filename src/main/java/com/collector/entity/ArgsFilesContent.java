package com.collector.entity;

import java.util.ArrayList;
import java.util.List;

import com.core.pattern.FileContent;

public class ArgsFilesContent {
	private ServerInfo serverInof;
	private final List<FileContent> fileContents = new ArrayList<FileContent>();

	public List<FileContent> getFileContents() {
		return fileContents;
	}

	public ArgsFilesContent() {
		super();
	}

	public ArgsFilesContent(ServerInfo serverInof) {
		super();
		this.setServerInfo(serverInof);
	}

	public boolean addFileContent(FileContent fileContent) {
		return this.fileContents.add(fileContent);

	}

	public boolean addFileContentAll(List<FileContent> fileContents) {
		return this.fileContents.addAll(fileContents);
	}

	public ServerInfo getServerInfo() {
		return serverInof;
	}

	public void setServerInfo(ServerInfo serverInof) {
		this.serverInof = serverInof;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < fileContents.size(); i++) {
			stringBuilder.append(i).append("=").append(fileContents.get(i).toString()).append(System.lineSeparator());
		}
		;
		return "ServerFilesContent [serverInof=" + serverInof + ", fileContents=" + System.lineSeparator()
				+ stringBuilder + "]";
	}

}
