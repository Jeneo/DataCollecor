package com.collector.util.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.collector.entity.ServerInfoDtcoll;
import com.collector.parse.ParseAbleArgs;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;

public class ParseFileBasic extends ParameterBasic {
	protected List<ServerInfoDtcoll> serverInoDtcollsForParse;
	protected ParseAbleArgs parseAbleArgs;
	protected final List<String> serverInoDtcollsForUpload = new ArrayList<>();
	protected String bcpFilePath;

	public List<ServerInfoDtcoll> getServerInoDtcollsForParse() {
		return serverInoDtcollsForParse;
	}

	public void setServerInoDtcollsForParse(Map<String, Object> parameter) {
		this.serverInoDtcollsForParse = Parameter.getServerInoDtcollsForParse(parameter);
	}

	public ParseAbleArgs getParseAbleArgs() {
		return parseAbleArgs;
	}

	public void setParseAbleArgs(Map<String, Object> parameter) {
		this.parseAbleArgs = (ParseAbleArgs) ctx.getBean("Parser");
	}

	public List<String> getServerInoDtcollsForUpload() {
		return serverInoDtcollsForUpload;
	}

	public String getBcpFilePath() {
		return bcpFilePath;
	}

	public void setBcpFilePath(Map<String, Object> parameter) {
		this.bcpFilePath = Parameter.getBcpFilePath(parameter);
	}

	protected String[] parseFiles(List<ServerInfoDtcoll> lists, String number) throws Exception {
		return parseAbleArgs.parseFiles(lists, bcpFilePath, "." + number);
	}

	protected void parameterAdd(List<String> flNames) {
		serverInoDtcollsForUpload.addAll(flNames);
	}

	protected boolean checkParameterValeu() {
		return serverInoDtcollsForUpload.size() == serverInoDtcollsForParse.size();
	}

	@Override
	protected void doSaveParameter() {
		parameter.put(Parameter.SERVERINFO_DCT_COLLS_UPLOAD,
				serverInoDtcollsForUpload.toArray(new String[serverInoDtcollsForUpload.size()]));
	}

	@Override
	public void setParameter(Map<String, Object> parameter) {
		super.setParameter(parameter);
		setBcpFilePath(parameter);
		setParseAbleArgs(parameter);
		setServerInoDtcollsForParse(parameter);
	}

}
