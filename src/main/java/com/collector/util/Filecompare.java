package com.collector.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.collector.entity.ArgsFilesContent;
import com.collector.entity.ServerInfoDtcoll;

public class Filecompare {

	public static String GetfileTime(String s) {
		String flString = s;
		flString = flString.substring(flString.length() - 22, flString.length());
		flString = flString.substring(0, 4) + "-" + flString.substring(4, 6) + "-" + flString.substring(6, 8) + " "
				+ flString.substring(9, 11) + ":" + flString.substring(11, 13) + ":" + flString.substring(13, 15);
		return flString;
	}

	public static ServerInfoDtcoll FilecompareForLast(List<ServerInfoDtcoll> lfs) throws Exception {
		ServerInfoDtcoll maxServerInfoDtcoll = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date dt = null;
		for (ServerInfoDtcoll lf : lfs) {
			String flString = lf.getServFileName();
			java.util.Date dt1;
			flString = GetfileTime(flString);
			if (dt == null) {
				maxServerInfoDtcoll = lf;
				dt = format.parse(flString);
			}
			dt1 = format.parse(flString);
			if (dt1.getTime() > dt.getTime()) {
				dt = dt1;
				maxServerInfoDtcoll = lf;
			}
		}
		return maxServerInfoDtcoll;
	}

	public static ArgsFilesContent FilecompareLatest(List<ArgsFilesContent> lfs) throws Exception {
		ArgsFilesContent maxServerFilesContent = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date dt = null;
		java.util.regex.Pattern pattern = Pattern.compile("20(\\d{6})[_]*\\d{4,6}");
		for (ArgsFilesContent lf : lfs) {
			if (lf.getFileContents().size() == 0)
				continue;
			String fl = lf.getFileContents().get(0).getFileName();
			java.util.Date dt1;
			Matcher matcher = pattern.matcher(fl);
			if (!matcher.find())
				continue;
			fl = matcher.group();
			fl = fl.replace("_", "");
			fl = fl.substring(0, 4) + "-" + fl.substring(4, 6) + "-" + fl.substring(6, 8) + " "
					+ fl.substring(8, 10) + ":" + fl.substring(10,12) + ":" + fl.substring(12, 14);
			if (dt == null) {
				maxServerFilesContent = lf;
				dt = format.parse(fl);
			}
			dt1 = format.parse(fl);
			if (dt1.getTime() > dt.getTime()) {
				dt = dt1;
				maxServerFilesContent = lf;
			}
		}
		return maxServerFilesContent;
	}

}
