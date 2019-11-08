package com.core.pattern;

public final class CountInterpreterExpression extends PatternCommand implements InterpreterExpression {
	public CountInterpreterExpression() {
		this.patternCommandName = PatternCommandConsts.COMMAND_MAX_COUNT;
		this.patternCommandStart = PatternCommandConsts.COMMAND_PARSE_START;
		this.patternCommandEnd = PatternCommandConsts.COMMAND_PARSE_END;
		this.patternCommandKey = PatternCommandConsts.COMMAND_PARSE_KEY;
	}

	private String parseCountExpression(String commandContent) {
		return commandContent;
	}

	@Override
	public boolean Matches(String expression) {
		return super.isMatches(expression);
	}

	@Override
	public String toInterpreterExpression(String expression) {
		String command = obtainCommand(expression).trim();
		return expression.replace(command, "").trim();
	}

	public String InterpreterfileCounting(String expression) {
		String command = obtainCommand(expression).trim();
		String commandContent = obtainCommandContent(command).trim();
		String commandChangeIntoCount= parseCountExpression(commandContent);
		return commandChangeIntoCount;
	}

}
