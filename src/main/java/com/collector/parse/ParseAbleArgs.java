package com.collector.parse;

import java.util.List;

import com.collector.entity.ServerInfoDtcoll;

public interface ParseAbleArgs {
	String[] parseFiles(List<ServerInfoDtcoll> sid, String path ,String number) throws Exception;
}
