package com.core.pattern;

public interface InterpreterContextPattern {
        boolean isPathLatest(String content);
        boolean isMaxLatest(String content);
        boolean isRegex(String content);
        int fileCounting(String content);
        String interpreterExpression(String content);
}
