package com.core.pattern;


public class PatternCommandConsts {
	public static final String COMMAND_PATH_LATEST = "-pl";
	public static final String COMMAND_MAX_LATEST = "-ml";
	public static final String COMMAND_MAX_COUNT = "$Count";
	public static final String COMMAND_TIME = "$Time";
	public static final String COMMAND_REGEX = "$Regex";
	public static final char COMMAND_PARSE_START = '{';
	public static final char COMMAND_PARSE_END = '}';
	public static final char COMMAND_PARSE_KEY = '$';

	public static final String DEF_COMMAND_GZ = "$Regex{^MML.*\\.tar\\.gz}";
	public static final String DEF_COMMAND_YYYYMMDD = "$Regex{.*$Time{yyyyMMdd}.*}";
	


	
}
