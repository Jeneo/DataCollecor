package com.core.pattern;

public class Pattern {
	public static final String PATH = "path";
	public static final String FILE = "file";

	private String type;
	private String content;
	private boolean pathLatest;
	private boolean maxLatest;
	private boolean regex;
	private int fileCounting;
	private String value;

	public Pattern(String type, String content) {
		super();
		this.type = type;
		setContent(content);
	}

	public Pattern(String type, String content, boolean pathLatest, boolean maxLatest, boolean regex, String value,
			int fileCounting) {
		this(type, content);
		this.pathLatest = pathLatest;
		this.maxLatest = maxLatest;
		this.regex = regex;
		this.value = value;
		this.fileCounting = fileCounting;
	}

	public boolean isPathLatest() {
		return pathLatest;
	}

	public void setPathLatest(boolean pathLatest) {
		this.pathLatest = pathLatest;
	}

	public boolean isMaxLatest() {
		return maxLatest;
	}

	public void setMaxLatest(boolean maxLatest) {
		this.maxLatest = maxLatest;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Pattern() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		content = content.replaceAll("(^[/\\\\]*|[/\\\\]*$)", "");// Ìæ»»×Ö·û´®Ç°ºóÐ±¸Ü
		this.content = content;
	}

	@Override
	public String toString() {
		return "Pattern [type=" + type + ", content=" + content + ", pathLatest=" + pathLatest + ", maxLatest="
				+ maxLatest + ", Regex=" + regex + ", value=" + value + ",fileCounting=" + fileCounting + "]";
	}

	public int getFileCounting() {
		return fileCounting;
	}

	public void setFileCounting(int fileCounting) {
		this.fileCounting = fileCounting;
	}

}
