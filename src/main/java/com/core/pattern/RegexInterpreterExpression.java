package com.core.pattern;


public final class RegexInterpreterExpression extends PatternCommand implements InterpreterExpression {

	public RegexInterpreterExpression() {
		this.patternCommandName = PatternCommandConsts.COMMAND_REGEX;
		this.patternCommandStart = PatternCommandConsts.COMMAND_PARSE_START;
		this.patternCommandEnd = PatternCommandConsts.COMMAND_PARSE_END;
		this.patternCommandKey = PatternCommandConsts.COMMAND_PARSE_KEY;
	}

	@Override
	public boolean Matches(String expression) {
		return super.isMatches(expression);	
	}

	@Override
	public String toInterpreterExpression(String expression) {
		String commandContent;
		String command;
		command = obtainCommand(expression).trim();
//		System.out.println("commandTime="+commandTime);
		commandContent = obtainCommandContent(command).trim();
//		System.out.println("commandContent="+commandContent);
		return expression.replace(command, commandContent);
	}
}
