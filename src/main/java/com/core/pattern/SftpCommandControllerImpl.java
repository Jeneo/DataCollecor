package com.core.pattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;


public class SftpCommandControllerImpl implements CommandController {
	private ChannelSftp sftpClient;

	public SftpCommandControllerImpl() {
	}

	public SftpCommandControllerImpl(ChannelSftp sftpClient) {
		this.sftpClient = sftpClient;
	}

	@Override
	public String changePath(String path) throws Exception {
		path=path.replaceAll("^(/)*", "/");		//如果开头出现多个/ 统一替换成一个/
		if (!path.startsWith("/"))
			path = "/" + path;
		sftpClient.cd(path);
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
//		System.out.println("path=" + path);
		Vector<?> vector = sftpClient.ls(path);
//		System.out.println("pwd=" + sftpClient.pwd());
		if (path.equals("/")) {
			path = "";
		}
		if (vector != null)
			for (Object object : vector) {
				if (object instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
					ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) object;
					if (lsEntry.getFilename().equals("."))continue;
					if (lsEntry.getFilename().equals(".."))continue;
					SftpATTRS attrs = lsEntry.getAttrs();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date(((long) attrs.getMTime()) * 1000l));
					fileContents.add(
							new FileContent(lsEntry.getFilename(), path, attrs.getSize(), calendar, !attrs.isDir()));
				}
			}
		return fileContents;
	}

}
