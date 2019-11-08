package com.core.pattern;

import java.util.ArrayList;
import java.util.List;

public class DownloadMatchesFiles {
	private CommandController commandController;
	private FilePatternManagerController filePatternManagerController;

	private List<FileContent> matchesFilesContent = new ArrayList<FileContent>();
	private List<String> matchesPath = new ArrayList<String>();
	private String presentPath;
	private Pattern nextPattern;

	public DownloadMatchesFiles(CommandController commandController, FilePatternManagerController patternController) {
		super();
		this.commandController = commandController;
		this.filePatternManagerController = patternController;
	}

	public DownloadMatchesFiles() {
		super();
	}

	private void addMatchesPaths() throws Exception {
//		String nextPath = nextPattern.getContent();
//		System.out.println("nextPath=" + nextPath);
		if (!nextPattern.isRegex()) {
			matchesPath.add(presentPath + "/" + nextPattern.getContent());
			return;
		}
		String regex = nextPattern.getValue();
//		System.out.println("regex=" + regex);
		for (FileContent fileContent : commandController.listFile(presentPath)) {
			if (fileContent.isFile())
				continue;
			if (fileContent.getFileName().matches(regex)) {
				matchesPath.add(presentPath + "/" + fileContent.getFileName());
			}
		}
	}

	private void addMatchesFiles() throws Exception {
		List<FileContent> tempMatchesFilesContent = new ArrayList<FileContent>();
		String regex = nextPattern.getValue();
		List<FileContent> fileContents = commandController.listFile(this.presentPath);
		for (FileContent fileContent : fileContents) {
			if (!fileContent.isFile())
				continue;
			if (nextPattern.isRegex() && fileContent.getFileName().matches(regex)) {
				tempMatchesFilesContent.add(fileContent);
			} else if (regex.equals(fileContent.getFileName()))
				tempMatchesFilesContent.add(fileContent);
		}
		if (nextPattern.isPathLatest()) {
			FileContent fileContent = FileContent.compareFileContentsLatest(tempMatchesFilesContent);
			if (fileContent != null)
				matchesFilesContent.add(fileContent);
		} else {
			matchesFilesContent.addAll(tempMatchesFilesContent);
		}
	}

	private int getChildPathIndex() {
		int count = -1;
		for (int i = 0; i < presentPath.length(); i++) {
			if (presentPath.charAt(i) == '/')
				count++;
		}
		return count;
	}

	private void commadMaxLatest() {
		if (!nextPattern.isMaxLatest())
			return;
		FileContent fileContent = FileContent.compareFileContentsLatest(matchesFilesContent);
		matchesFilesContent.clear();
		if (fileContent != null)
			matchesFilesContent.add(fileContent);

	}

	private void commandFileCounting() {
		if (nextPattern.getFileCounting() == 0)
			return;
		if (nextPattern.getFileCounting() == matchesFilesContent.size())
			return;
		matchesFilesContent.clear();
//		System.out.println("文件数目counting不等于" + nextPattern.getFileCounting());
	}

	private void addMatchesFileWhenRootIsFile() throws Exception {
		nextPattern = filePatternManagerController.getRootPattern();
		presentPath = "/";
		addMatchesFiles();
	}

	private void addMatchesFileWhenRootIsRegexPath() throws Exception {
		nextPattern = filePatternManagerController.getRootPattern();
		presentPath = "/";
		addMatchesPaths();
	}

	public List<FileContent> doFileFilter() throws Exception {
		matchesFilesContent.clear();
		matchesPath.clear();
		if (filePatternManagerController.getRootPattern().getType().equals(Pattern.FILE)) {
			addMatchesFileWhenRootIsFile();
		} else {
			if (filePatternManagerController.getRootPattern().isRegex()) {
				addMatchesFileWhenRootIsRegexPath();
			} else {
				matchesPath.add(filePatternManagerController.getRootPattern().getValue());
			}
		}

		while (!matchesPath.isEmpty()) {
			String path = matchesPath.remove(0);
//			System.out.println("准备change到" + path);
			presentPath = commandController.changePath(path);
//			changePattern();
//			System.out.println("切换成功当前路径:" + presentPath + ",当前路径深度=" + getChildPathIndex());
			nextPattern = filePatternManagerController.getNextPattern(getChildPathIndex());
//			System.out.println(
//					"presentPattern=" + presentPattern + System.lineSeparator() + "nextPattern=" + nextPattern);
			if (nextPattern == null)
				break;
			if (nextPattern.getType().equals(Pattern.FILE)) {
				addMatchesFiles();
			} else {
				addMatchesPaths();
			}
		}

		commadMaxLatest();
		commandFileCounting();
		return matchesFilesContent;
	}

	public CommandController getCommandController() {
		return commandController;
	}

	public void setCommandController(CommandController commandController) {
		this.commandController = commandController;
	}

	public FilePatternManagerController getFilePatternManagerController() {
		return filePatternManagerController;
	}

	public void setFilePatternManagerController(FilePatternManagerController filePatternManagerController) {
		this.filePatternManagerController = filePatternManagerController;
	}

}
