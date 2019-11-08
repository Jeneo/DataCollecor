package com.core.pattern;

public interface InterpreterExpression {
	boolean Matches(String expression);
	String toInterpreterExpression(String expression);
}
