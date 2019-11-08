package com.collector.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;

import com.collector.entity.ArgsFilesContent;
import com.collector.entity.ServerInfoDtcoll;

public class Parameter {
	public static final String ROOTPATH = "RootPath";
	public static final String BACKUPPATH = "BackUpPath";
	public static final String BCPFILEPATH = "BcpFilePath";
	public static final String SOURCEPATH = "SourcePath";

	public static final String SERVERINFO_DCT_COLLS_DEAL = "ServerInoDtcollsForDeal";
	public static final String SERVERINFO_DCT_COLLS_DOWNLOAD = "ServerInoDtcollsForDownLoad";
	public static final String SERVERINFO_DCT_COLLS_PARSE = "ServerInoDtcollsForParse";
	public static final String SERVERINFO_DCT_COLLS_UPLOAD = "ServerInoDtcollsForUpload";

	public static final String ApplicationContext = "ApplicationContext";
	public static final String ARGCFG = "ArgCfg";
	
	
	private static ExecutorService executorService=Executors.newFixedThreadPool(10);
	private Parameter(){};
	
		
	public static ExecutorService getExecutorService()
	{
		return executorService;
		
	}
	public static ApplicationContext getApplicationContext(Map<String, Object> parameter) {
		return (org.springframework.context.ApplicationContext) parameter.get(Parameter.ApplicationContext);
	}

	public static String getRootPath(Map<String, Object> parameter) {
		return (String) parameter.get(Parameter.ROOTPATH);
	}

	public static String getArgCfg(Map<String, Object> parameter) {
		return (String) parameter.get(Parameter.ARGCFG);
	}

	public static String getBackUpPath(Map<String, Object> parameter) {
		return (String) parameter.get(Parameter.BACKUPPATH);
	}

	public static String getBcpFilePath(Map<String, Object> parameter) {
		return (String) parameter.get(Parameter.BCPFILEPATH);
	}

	public static String getSourcePath(Map<String, Object> parameter) {
		return (String) parameter.get(Parameter.SOURCEPATH);
	}

	public static String getDclName(Map<String, Object> parameter) {
		ApplicationContext ctx = Parameter.getApplicationContext(parameter);
		return (String) ctx.getBean("dclName");
	}

	@SuppressWarnings("unchecked")
	public static List<List<ServerInfoDtcoll>> getCollectOmcs(Map<String, Object> parameter) {
		ApplicationContext ctx = Parameter.getApplicationContext(parameter);
		return (List<List<ServerInfoDtcoll>>) ctx.getBean("collectOmcs");
	}
	
	@SuppressWarnings("unchecked")
	public static List<ServerInfoDtcoll> getServerInoDtcollsForDeal(Map<String, Object> parameter) {
		return (List<ServerInfoDtcoll>) parameter.get(Parameter.SERVERINFO_DCT_COLLS_DEAL);
	}

	@SuppressWarnings("unchecked")
	public static List<ServerInfoDtcoll> getServerInoDtcollsForParse(Map<String, Object> parameter) {
		return (List<ServerInfoDtcoll>) parameter.get(Parameter.SERVERINFO_DCT_COLLS_PARSE);
	}

	public static String[] getServerInoDtcollsForUpload(Map<String, Object> parameter) {
		return (String[]) parameter.get(Parameter.SERVERINFO_DCT_COLLS_UPLOAD);
	}

	@SuppressWarnings("unchecked")
	public static List<ArgsFilesContent> getServerInoDtcollsForDownLoad(Map<String, Object> parameter) {
		return (List<ArgsFilesContent>) parameter.get(Parameter.SERVERINFO_DCT_COLLS_DOWNLOAD);
	}
	
	
	
	
}
