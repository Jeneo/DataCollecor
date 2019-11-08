package com.core.pattern;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class TimeInterpreterExpression extends PatternCommand implements InterpreterExpression {
	public TimeInterpreterExpression() {
		this.patternCommandName = PatternCommandConsts.COMMAND_TIME;
		this.patternCommandStart = PatternCommandConsts.COMMAND_PARSE_START;
		this.patternCommandEnd = PatternCommandConsts.COMMAND_PARSE_END;
		this.patternCommandKey = PatternCommandConsts.COMMAND_PARSE_KEY;
	}

	private String parseTimeExpression(String commandContent) {
		// 分割字符串,一個或個空格
		String[] contet = commandContent.split("[\\s]*+,[\\s]*");
		if (contet == null)
			return null;
		String defDateFormat;
		int hour;
		int minute;
		defDateFormat = contet.length > 0 ? contet[0] : "yyyyMMdd";
		hour = contet.length > 1 ? Integer.parseInt(contet[1]) : 0;
		minute = contet.length > 2 ? Integer.parseInt(contet[2]) : 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defDateFormat);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, hour);
		now.add(Calendar.MINUTE, minute);
		return simpleDateFormat.format(now.getTime());
	}
	
	@Override
	public boolean Matches(String expression) {
		return super.isMatches(expression);		
	}

	@Override
	public String toInterpreterExpression(String expression) {
		String commandContent;
		String command;
		String commandChangeIntoTime;
		command = obtainCommand(expression).trim();
		commandContent = obtainCommandContent(command).trim();
//		System.out.println(commandContent);
		commandChangeIntoTime = parseTimeExpression(commandContent);
		return expression.replace(command, commandChangeIntoTime);
	}



}
