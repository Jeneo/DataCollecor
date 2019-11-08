package com.core.pattern;

public interface FilePatternManagerController {
	Pattern getPattern(int i);
	Pattern getRootPattern();
	Pattern getNextPattern(int i);
}
