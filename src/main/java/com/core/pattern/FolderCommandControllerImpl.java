package com.core.pattern;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FolderCommandControllerImpl implements CommandController {

	@Override
	public String changePath(String path) throws Exception {
		File file = new File(path);
		if (!file.exists() || file.isFile()) {
			System.out.println("路径不存在" + path);
			throw new Exception("路径不存在");
		}
		String curPath = file.getPath().replace("\\", "/");
		System.out.println(curPath);
		if (curPath.endsWith("/") || curPath.endsWith("\\")) {
			return curPath;
		} else {
			return curPath + "/";
		}

	}

	@Override
	public List<FileContent> listFile(String path) throws Exception {
		System.out.println("listFile(String path)="+path);
		File file = new File(path);
		List<FileContent> fileContents = new ArrayList<FileContent>();
		for (File fl : file.listFiles()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(fl.lastModified()));
			fileContents
					.add(new FileContent(fl.getName(), fl.getParent().replace("\\", "/"), fl.length(), calendar, fl.isFile()));
		}
		;
		return fileContents;

	}

}
