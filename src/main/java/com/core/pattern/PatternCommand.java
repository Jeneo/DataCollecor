package com.core.pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternCommand {
	protected char patternCommandStart ;
	protected char patternCommandEnd ;
	protected char patternCommandKey ;
	protected String patternCommandName;
	

	public char getPatternCommandStart() {
		return patternCommandStart;
	}


	public void setPatternCommandStart(char patternCommandStart) {
		this.patternCommandStart = patternCommandStart;
	}


	public char getPatternCommandEnd() {
		return patternCommandEnd;
	}


	public void setPatternCommandEnd(char patternCommandEnd) {
		this.patternCommandEnd = patternCommandEnd;
	}


	public char getPatternCommandKey() {
		return patternCommandKey;
	}


	public void setPatternCommandKey(char patternCommandKey) {
		this.patternCommandKey = patternCommandKey;
	}


	public String getPatternCommandName() {
		return patternCommandName;
	}


	public void setPatternCommandName(String patternCommandName) {
		this.patternCommandName = patternCommandName;
	}


	protected  boolean isMatchBrackets(String content) {
		// �����������ŵĶ�Ӧ��ϵ
		Map<Character, Character> bracket = new HashMap<>();
//		bracket.put(')', '(');
//		bracket.put(']', '[');
//		bracket.put('}', '{');
		bracket.put(patternCommandEnd, patternCommandKey);
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < content.length(); i++) {
			Character temp = content.charAt(i);// ��ת�����ַ�
			// �Ƿ�Ϊ������
			if (bracket.containsValue(temp)) {
				stack.push(temp);
				// �Ƿ�Ϊ������
			} else if (bracket.containsKey(temp)) {
				if (stack.isEmpty()) {
					return false;
				}
				// ����������ƥ��,��ȡջ��Ԫ����ƥ�����ջ
				if (stack.peek() == bracket.get(temp)) {
					stack.pop();
				} else {
					return false;
				}
			}
		}
		return stack.isEmpty() ? true : false;
	}

	
	protected boolean isMatches(String content)
	{
		return content.indexOf(patternCommandName + patternCommandStart) >= 0 && isMatchBrackets(content);
	}
	
	protected boolean containCommand(String content) {
		return content.indexOf(patternCommandName + patternCommandStart) >= 0	;
	}
	protected  String obtainCommand(String content) {
		String regex = String.format("\\%s\\%s([^%s]*)\\%s", patternCommandName, patternCommandStart, patternCommandEnd,
				patternCommandEnd);
//		System.out.println(regex);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	protected  String obtainCommandContent(String content) {
		String regex = String.format("\\%s([^%s]*)\\%s", patternCommandStart, patternCommandEnd, patternCommandEnd);
//		System.out.println("regex=" + regex);
//		System.out.println("content===================" + content);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String value = matcher.group();
//			System.out.println("value===================" + value);
			return value.substring(1, value.length() - 1);
		}
		return null;
	}
	
	
}
