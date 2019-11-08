package com.core.pattern;

public final class LatestInterpreterExpression extends PatternCommand implements InterpreterExpression {
	private String[] defCommands;

	public LatestInterpreterExpression(String[] defCommands) {
		super();
		this.defCommands = defCommands;
	}

	@Override
	public boolean Matches(String expression) {
		boolean matches = false;
		matches = expression.indexOf(patternCommandName) >= 0;
		return matches;
	}

	@Override
	public String toInterpreterExpression(String expression) {
		String content = expression;
		for (String string : defCommands) {
			content = content.replace(string, "");
		}
		return content;
	}

}
