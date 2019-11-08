package com.collector.util.net;

import java.util.HashMap;
import java.util.Map;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.util.Parameter;
import com.collector.util.ParameterBasic;
import com.core.util.FtpHelper;

public class UpLoadFileBasic extends ParameterBasic {
	protected String[] serverInoDtcollsForUpload;

	protected ServerInfoDtcoll sid;

	protected String bcpPath;
	protected final Map<String, String> map = new HashMap<String, String>();

	public ServerInfoDtcoll getSid() {
		return sid;
	}

	public void setSid(Map<String, Object> parameter) {
		this.sid = (ServerInfoDtcoll) ctx.getBean("upLoadServer");
	}

	public String getBcpPath() {
		return bcpPath;
	}

	public void setBcpPath(Map<String, Object> parameter) {
		this.bcpPath = Parameter.getBcpFilePath(parameter);
	}

	public String[] getServerInoDtcollsForUpload() {
		return serverInoDtcollsForUpload;
	}

	public void setServerInoDtcollsForUpload(Map<String, Object> parameter) {
		this.serverInoDtcollsForUpload = Parameter.getServerInoDtcollsForUpload(parameter);
	}
	
	@Override
	public void setParameter(Map<String, Object> parameter) {
		super.setParameter(parameter);
		setSid(parameter);
		setBcpPath(parameter);
		setServerInoDtcollsForUpload(parameter);
	}	
	
	public void upLoad()throws Exception {
		map.clear();
		for (String fl : serverInoDtcollsForUpload) {
			map.put(sid.getServPath() + fl, bcpPath + fl);
		}
		FtpHelper ftpHelp = new FtpHelper(sid.getServerInof().getIp(), 21, sid.getServerInof().getUser(),
				sid.getServerInof().getPwd());
		ftpHelp.upload(map);		
	}
	
	public void upLoad(String fl) throws Exception{
		FtpHelper ftpHelp = new FtpHelper(sid.getServerInof().getIp(), 21, sid.getServerInof().getUser(),
				sid.getServerInof().getPwd());
		ftpHelp.upload(sid.getServPath() + fl, bcpPath + fl);	
	}

	@Override
	protected void doSaveParameter() {		
	}
}
