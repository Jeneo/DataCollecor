package com.core.pattern;

import java.util.List;

public interface CommandController {
	String changePath(String path) throws Exception;;
	List<FileContent> listFile(String path) throws Exception;
	
}
