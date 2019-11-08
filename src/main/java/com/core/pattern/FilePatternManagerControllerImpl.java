package com.core.pattern;

import java.util.ArrayList;
import java.util.List;

public class FilePatternManagerControllerImpl implements FilePatternManagerController {
	private String pattern;
	private InterpreterContextPattern interpreterContextPattern;

	private List<Pattern> patternManager = new ArrayList<Pattern>();

	public FilePatternManagerControllerImpl(String pattern, InterpreterContextPattern interpreterContextPattern) {
		super();
		this.interpreterContextPattern = interpreterContextPattern;
		setPattern(pattern);
	}

	public FilePatternManagerControllerImpl() {
		super();
	}

	private Pattern addPath(String patternType, String path) {
		Pattern pattern = new Pattern(patternType, path);
		pattern.setRegex(interpreterContextPattern.isRegex(pattern.getContent()));
		pattern.setMaxLatest(interpreterContextPattern.isMaxLatest(pattern.getContent()));
		pattern.setPathLatest(interpreterContextPattern.isPathLatest(pattern.getContent()));
		pattern.setValue(interpreterContextPattern.interpreterExpression(pattern.getContent()));
		pattern.setFileCounting(interpreterContextPattern.fileCounting(pattern.getContent()));
		patternManager.add(pattern);
		return pattern;
	}

	private Pattern addPath(String patternType, StringBuffer stringBuffer) {
		if (stringBuffer.length() == 0)
			return null;
		if ("/".equals(stringBuffer.toString()) || "\\".equals(stringBuffer.toString()))
			return null;
		return addPath(patternType, stringBuffer.toString());
	}

	private boolean isKeyWord(char c) {
		return c == '\\' || c == '/';
	}

	private boolean commandMatchs(char key) {
		return key == PatternCommandConsts.COMMAND_PARSE_KEY;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < patternManager.size(); i++) {
			stringBuilder.append(i).append("=").append(patternManager.get(i).toString()).append(System.lineSeparator());
		}
		;

		return "FilePatternManager [" + System.lineSeparator() + "pattern=" + pattern //
				+ System.lineSeparator() + "patternManager" + System.lineSeparator() + stringBuilder //
				+ "]";
	}

	public void parseMatchPaths() {
		StringBuffer stringBuffer = new StringBuffer();
		int j = 0;
		boolean matches = false;
		boolean end = true;
		patternManager.clear();
		for (int i = 0; i < pattern.length(); i++) {
			char c = pattern.charAt(i);
			stringBuffer.append(c);
			if (isKeyWord(pattern.charAt(i))) {
				if (end) {
					addPath(Pattern.PATH, stringBuffer);
					stringBuffer.delete(0, stringBuffer.length());
				}
				continue;
			}
			if (commandMatchs(c)) {
				matches = true;
				j = 0;
				end = false;
				continue;
			}
			if (matches && c == PatternCommandConsts.COMMAND_PARSE_START) {
				j++;
				continue;
			}
			if (matches && c == PatternCommandConsts.COMMAND_PARSE_END) {
				j--;
				if (j == 0) {
					end = true;
				}
				continue;
			}
		}
		addPath(Pattern.FILE, stringBuffer);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		pattern = pattern.trim().replaceAll("(^[/\\\\]*|[/\\\\]*$)", "");// 替换字符串前后斜杠
		this.pattern = pattern.trim();
	}

	public List<Pattern> getPatternManager() {
		return patternManager;
	}

	public void setPatternManager(List<Pattern> patternManager) {
		this.patternManager = patternManager;
	}

	public InterpreterContextPattern getInterpreterContextPattern() {
		return interpreterContextPattern;
	}

	public void setInterpreterContextPattern(InterpreterContextPattern interpreterContextPattern) {
		this.interpreterContextPattern = interpreterContextPattern;
	}

	@Override
	public Pattern getPattern(int i) {
		return patternManager.get(i);
	}

	@Override
	public Pattern getRootPattern() {
		return patternManager.get(0);
	}

	@Override
	public Pattern getNextPattern(int i) {
		if ((i + 1) > patternManager.size())
			return null;
		return patternManager.get(i + 1);
	}
}
