package com.collector.parse.hw.mmltask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.collector.entity.ServerInfoDtcoll;
import com.collector.parse.ParseAbleArgs;

public class ParseMmltask implements ParseAbleArgs {
	private String keyWord1;
	private String keyWord2;
	private List<String> cols;
	private String p;
	private String fileName;

	public String getKeyWord1() {
		return keyWord1;
	}

	public void setKeyWord1(String keyWord1) {
		this.keyWord1 = keyWord1;
	}

	public String getKeyWord2() {
		return keyWord2;
	}

	public void setKeyWord2(String keyWord2) {
		this.keyWord2 = keyWord2;
	}

	public List<String> getCols() {
		return cols;
	}

	public void setCols(List<String> cols) {
		this.cols = cols;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	private List<String> parseKeyWord1(ServerInfoDtcoll serverInfoDtcoll, List<java.lang.String> tmpList) {
		String omcIpString = serverInfoDtcoll.getServerInof().getIp();
		String line;
		StringBuilder head = new StringBuilder();
		StringBuilder info = new StringBuilder();
		String[] col = null;
		List<String> anaList = new ArrayList<String>();
		String[] aa = null;
		String net = "";
		Map<String, Integer> map = new HashMap<>();
		for (int i = 0; i < cols.size(); i++)
			map.put(cols.get(i), -1);
		for (int i = 0; i < tmpList.size(); i++) {
			line = tmpList.get(i);
			if (line.indexOf("网元:") >= 0) {
				net = tmpList.get(i + 1).trim();
				continue;
			}
			if (line.indexOf(keyWord1) >= 0) {
				col = line.split("[\\s][\\s]+");// 字段含空格
				for (int j = 0; j < col.length; j++) {
					if (map.containsKey(col[j]))
						map.put(col[j], j);
				}
				line = tmpList.get(i - 7);
				aa = line.trim().split("[\\s]+");
				head.delete(0, head.length());
				head.append(omcIpString).append(p);
				head.append(aa[2] + " " + aa[3]).append(p).append(net);
				continue;
			}
			if (col == null || line.trim().length() <= 4)
				continue;
			String n = line.substring(0, 4).trim();
			if (Func.isInteger(n)) {
				if (map.containsValue(-1))
					continue;

				info.delete(0, info.length());
				aa = line.trim().split("[\\s][\\s]+");// 字段含空格
				for (String s : cols) {
					Integer index = map.get(s);
					info.append(p).append(aa[index]);
				}
				anaList.add(head.toString() + info.toString());
			}
		}
		return anaList;
	}

	private List<String> parseKeyWord2(ServerInfoDtcoll serverInfoDtcoll, List<java.lang.String> tmpList) {
		String omcIpString = serverInfoDtcoll.getServerInof().getIp();
		;
		String line;
		List<String> anaList = new ArrayList<String>();
		StringBuilder head = new StringBuilder();
		StringBuilder info = new StringBuilder();
		int n = 0;
		for (int i = 0; i < tmpList.size(); i++) {
			line = tmpList.get(i);

			if (line.indexOf("+++") > 0) {
				String[] aa = line.trim().split("[\\s]+");
				head.append(omcIpString).append(p);
				head.append(aa[2] + " " + aa[3]).append(p).append(tmpList.get(i - 2).trim());
				continue;
			}

			for (String s : cols) {
				if (line.indexOf(s + "  =") >= 0) {
					info.append(p).append(line.substring(line.indexOf("=") + 1, line.length()).trim());
					n++;
					break;
				}
			}

			if (line.indexOf("---    END") >= 0) {
				if (n == cols.size())
					anaList.add(head.toString() + info.toString());
				continue;
			}
		}
		return anaList;
	}

	private void parseContent(BufferedWriter out, BufferedReader br, ServerInfoDtcoll sid) throws Exception {
		List<java.lang.String> tmpList = new ArrayList<java.lang.String>();
		int cmdTp = 0;
		String line;
		boolean isAll = true;
		while ((line = br.readLine()) != null) {
			tmpList.add(line);
			// 1 判断解析类型
			if (line.indexOf(keyWord1) >= 0) {
				cmdTp = 1;
				continue;
			} else if (line.indexOf(keyWord2) >= 0) {
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
			if (line.indexOf("网元断连") >= 0) {
				cmdTp = 0;
				tmpList.clear();
				continue;
			}
			// 3 根据关键字 及解析类型调用不通的解析方法
			if (line.indexOf("---    END") >= 0 && isAll) {
				if (cmdTp == 1) {
					List<String> ls = parseKeyWord1(sid, tmpList);
					for (String s : ls)
						out.write(s + System.lineSeparator());

				} else if (cmdTp == 2) {
					List<String> ls = parseKeyWord2(sid, tmpList);
					for (String s : ls)
						out.write(s + System.lineSeparator());
				}
				cmdTp = 0;
				tmpList.clear();
				continue;
			}
		}
	}

	public String[] parseFiles(List<ServerInfoDtcoll> sid, String path) throws Exception {
		return parseFiles(sid, path, "");
	}

	@Override
	public String[] parseFiles(List<ServerInfoDtcoll> sid, String path, String number) throws Exception {
		String flnameString = this.fileName + number;
		File writeName = new File(path + flnameString);
		FileWriter writer = null;
		BufferedWriter out = null;
		try {
			writer = new FileWriter(writeName);
			out = new BufferedWriter(writer);
			StringBuilder head = new StringBuilder();
			head.append("omcIP" + p + "时间" + p + "网元");
			for (String s : cols) {
				head.append(p + s);
			}
			head.append(System.lineSeparator());
			out.write(head.toString());
			for (ServerInfoDtcoll serverInfoDtcoll : sid) {
				File file = new File(serverInfoDtcoll.getLocalFileFullName()); // 文件
				InputStreamReader read = null;
				BufferedReader br = null;
				try {
					read = new InputStreamReader(new FileInputStream(file), "utf-8");
					br = new BufferedReader(read);
					parseContent(out, br, serverInfoDtcoll);
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
