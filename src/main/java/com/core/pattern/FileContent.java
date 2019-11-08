package com.core.pattern;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class FileContent {
	private String fileName;
	private String path;
	private long size;
	private Calendar date;
	private boolean file;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public FileContent(String fileName, String path, long size, Calendar date, boolean file) {
		super();
		this.fileName = fileName;
		this.path = path;
		this.size = size;
		this.date = date;
		this.file = file;
	}

	public FileContent() {
		super();
	}

	public String getFileFullName() {
		return path + "/" + fileName;
	}

	@Override
	public String toString() {
		return "FileContent [fileName=" + fileName + ", path=" + path + ", size=" + size + ", date="
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.date.getTime()) + ",  getFileFullName()="
				+ getFileFullName() + "]";
	}

	public static FileContent compareFileContentsLatest(List<FileContent> fileContents) {
		FileContent maxFileContent = null;
		for (int i = 0, size = fileContents.size(); i < size; i++) {
			FileContent fileContent = fileContents.get(i);
			if (i == 0 || fileContent.getDate().after(maxFileContent.getDate())) {
				maxFileContent = fileContent;
			}
		}
		return maxFileContent;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

}
