package com.core.pattern;

public class InterpreterContextPatternImpl implements InterpreterContextPattern {
	FilePatternManagerControllerImpl filePatternManagerImpl;
	private InterpreterExpression regexInterpreterExpression = new RegexInterpreterExpression();
	private CountInterpreterExpression countInterpreterExpression = new CountInterpreterExpression();
	private InterpreterExpression timeInterpreterExpression = new TimeInterpreterExpression();
	private InterpreterExpression latestInterpreterExpression = new LatestInterpreterExpression(
			new String[] { PatternCommandConsts.COMMAND_PATH_LATEST, PatternCommandConsts.COMMAND_MAX_LATEST });

	public InterpreterContextPatternImpl(FilePatternManagerControllerImpl filePatternManagerImpl) {
		super();
		this.filePatternManagerImpl = filePatternManagerImpl;
	}

	public InterpreterContextPatternImpl() {
		super();
	}

	@Override
	public boolean isPathLatest(String expression) {
		((LatestInterpreterExpression) latestInterpreterExpression)
				.setPatternCommandName(PatternCommandConsts.COMMAND_PATH_LATEST);
		return latestInterpreterExpression.Matches(expression);
	}

	@Override
	public boolean isMaxLatest(String expression) {
		((LatestInterpreterExpression) latestInterpreterExpression)
				.setPatternCommandName(PatternCommandConsts.COMMAND_MAX_LATEST);
		return latestInterpreterExpression.Matches(expression);
	}

	@Override
	public boolean isRegex(String expression) {
		return regexInterpreterExpression.Matches(expression);
	}

	@Override
	public String interpreterExpression(String expression) {
		String content = expression.trim();
		if (isPathLatest(content) || isMaxLatest(content)) {
			content = latestInterpreterExpression.toInterpreterExpression(content);
		}

		if (countInterpreterExpression.Matches(content)) {
			content = countInterpreterExpression.toInterpreterExpression(content);
		}
		
		if (timeInterpreterExpression.Matches(content)) {
			content = timeInterpreterExpression.toInterpreterExpression(content);
//			System.out.println("timeInterpreterExpression="+content);
		}
		if (isRegex(content)) {
			content = regexInterpreterExpression.toInterpreterExpression(content);
//			System.out.println("regexInterpreterExpression=" + content);
		}
//		System.out.println(content.toString());
		return content.trim();
	}

	@Override
	public int fileCounting(String content) {
		if(countInterpreterExpression.Matches(content))
		{
			String count=((CountInterpreterExpression)countInterpreterExpression).InterpreterfileCounting(content);
			return Integer.parseInt(count);
		}
		return 0;
	}

}
