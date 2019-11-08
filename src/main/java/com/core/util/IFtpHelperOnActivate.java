package com.core.util;



import org.apache.commons.net.ftp.FTPClient;

public interface IFtpHelperOnActivate {
	Object OnActivateForDo(FTPClient ftpclient) throws Exception;
}
