package com.collector.parse.hw.mmltask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.parse.ParseAbleArgs;

public class HW_5G_Alarm implements ParseAbleArgs {
	@Override
	public String[] parseFiles(List<ServerInfoDtcoll> sid, String path, String number) throws Exception {
		return DoAnalyse(sid, path ,number);
	}

	public String DoAnalyse_net(ServerInfoDtcoll serverInfoDtcoll, List<java.lang.String> tmpList) {
		String omcIpString = serverInfoDtcoll.getServerInof().getIp();
		String netElement = "";
		String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String textString = "";
		String line;
		for (int i = 0; i < tmpList.size(); i++) {
			line = tmpList.get(i);
			if (line.indexOf("网元断连!") >= 0) {
				netElement = tmpList.get(i - 2).trim();
				break;
			}
		}
		textString = omcIpString + "\t" + dt + "\t" + netElement + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t"
				+ "网元断连" + "\t" + dt + "\t" + "\t" + "\t" + "\t";
		return textString;
	}

	public List<String> DoAnalyse_alarm(ServerInfoDtcoll serverInfoDtcoll, List<java.lang.String> tmpList) {
		List<String> alarmLs = new ArrayList<String>();
		String omcIpString = serverInfoDtcoll.getServerInof().getIp();
		StringBuilder head = new StringBuilder();
		StringBuilder alarm = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		List<String> ls = new ArrayList<String>();
		String line;
		int info = 0;
		for (int i = 0; i < tmpList.size(); i++) {
			line = tmpList.get(i);
			if (line.indexOf("报文:") >= 0 && tmpList.get(i + 1).indexOf("+++") > 0) {
				String[] aa = tmpList.get(i + 1).trim().split("[\\s]+");
				head.delete(0, head.length());
				head.append(omcIpString).append("\t");
				head.append(aa[2] + " " + aa[3]).append("\t").append(tmpList.get(i - 1).trim()).append("\t");
				continue;
			}
			if (line.indexOf("ALARM  ") >= 0) {
				String[] aa = line.split(" ");
				alarm.delete(0, alarm.length());
				for (String s : aa) {
					if (s.trim().length() > 0 && !s.equals("ALARM"))
						alarm.append(s).append("\t");
				}
				info = 1;
				continue;
			}
			if (info == 1) {
				ls.add(line);
				if (line.trim().length() == 0 || line.trim().indexOf("结果个数 =") > 0) {
					Map<String, String> map = Func.getInfo(ls);
					String infoString = map.containsKey("定位信息") ? map.get("定位信息") : "";
					if (infoString.indexOf("NR小区名称") > 0) {
						Map<String, String> map1 = Func.getInfo(infoString);
						if (map1.containsKey("NR小区名称"))
							map.put("小区名称", map1.get("NR小区名称"));
					}
					builder.append(map.containsKey("告警同步号") ? map.get("告警同步号") : "").append("\t");
					builder.append(map.containsKey("告警名称") ? map.get("告警名称") : "").append("\t");
					builder.append(map.containsKey("告警发生时间") ? map.get("告警发生时间") : "").append("\t");
					builder.append(map.containsKey("定位信息") ? map.get("定位信息") : "").append("\t");
					builder.append(map.containsKey("小区名称") ? map.get("小区名称") : "").append("\t");
					builder.append(map.containsKey("附加信息") ? map.get("附加信息") : "").append("\t");
					builder.delete(builder.length() - 1, builder.length());
					alarmLs.add(head.toString() + alarm.toString() + builder.toString());
					info = 0;
					builder.delete(0, builder.length());
					ls.clear();
				}
				continue;
			}
		}
		return alarmLs;
	}
	
	
	public void DoAnalyse_sid(BufferedWriter out, BufferedReader br, ServerInfoDtcoll sid) throws Exception {
		List<java.lang.String> tmpList = new ArrayList<java.lang.String>();
		int cmdTp = 0;
		String line;
		boolean isAll = true;
		while ((line = br.readLine()) != null) {
			tmpList.add(line);
			// 1 判断解析类型
			if (line.indexOf("网元断连!") >= 0) {
				cmdTp = 1;
				continue;
			} else if (line.indexOf("RETCODE = 0  执行成功") >= 0) {
				cmdTp = 2;
				continue;
			}
			// 2 判断文件是否连续
			if (line.indexOf("仍有后续报告输出") >= 0) {
				isAll = false;
				continue;
			} else if (Pattern.matches("共有[\\d]+个报告", line)) {
				isAll = true;
				continue;
			}
			// 3 根据关键字 及解析类型调用不通的解析方法
			if (line.indexOf("命令返回值:") >= 0) {
				if (cmdTp == 1) {
					String string = DoAnalyse_net(sid, tmpList);
					out.write(string + System.lineSeparator());
					cmdTp = 0;
					tmpList.clear();
					continue;
				}
			} else if (line.indexOf("---    END") >= 0) {
				if (cmdTp == 2 && isAll) {
					List<String> ls = DoAnalyse_alarm(sid, tmpList);
					for (String s : ls)
						out.write(s + System.lineSeparator());
					cmdTp = 0;
					tmpList.clear();
					continue;
				}
			}
		}
	}

	public String[] DoAnalyse(List<ServerInfoDtcoll> sid, String path) throws Exception {
		return DoAnalyse(sid,path,"");
	}
	public String[] DoAnalyse(List<ServerInfoDtcoll> sid, String path, String number) throws Exception {
		String flnameString = "5G_Alarm" + number;
		File writeName = new File(path + flnameString);
		FileWriter writer = null;
		BufferedWriter out = null;
		try {
			writer = new FileWriter(writeName);
			out = new BufferedWriter(writer);
			out.write("omcIP\t时间\t网元\t告警流水号\t告警类型\t告警级别\t网元类型\t告警ID\t告警部件\t告警同步号\t告警名称\t告警时间\t定位信息\t小区名称\t附加信息"
					+ System.lineSeparator());
			for (ServerInfoDtcoll serverInfoDtcoll : sid) {
				File file = new File(serverInfoDtcoll.getLocalFileFullName()); // 文件
				InputStreamReader read = null;
				BufferedReader br = null;
				try {
					read = new InputStreamReader(new FileInputStream(file), "utf-8");
					br = new BufferedReader(read);
					DoAnalyse_sid(out, br, serverInfoDtcoll);
				} finally {
					if (br != null)
						br.close();
					if (read != null)
						read.close();
				}
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (writer != null)
				writer.close();
		}

		return new java.lang.String[] { flnameString };
	}
}