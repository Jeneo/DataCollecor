package com.collector.parse.hw.mmltask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Func {
	public static Map<String, String> getInfo(List<String> ls) {
		Map<String, String> map = new HashMap<String, String>();
		String keyLast = "";
		for (int i = 0; i < ls.size(); i++) {
			String s = ls.get(i);
			if (s.trim().equals(""))
				continue;
			String key = s.substring(0, s.indexOf("=")).trim();
			String value = s.substring(s.indexOf("=") + 1, s.length()).trim();

			if (!key.equals("")) {
				map.put(key, value);
				keyLast = key;
			} else {
				if (!value.equals("")) {
					String valueTmpString = map.get(keyLast);
					map.put(keyLast, valueTmpString + " " + value);
				}
			}
		}
		return map;
	}


	public static Map<String, String> getInfo(String ls) {
		Map<String, String> map = new HashMap<String, String>();
		String[] ssStrings = ls.split(",");
		for (String s : ssStrings) {
			s = s.trim();
			if (s.length() == 0)
				continue;
			if (s.indexOf("=") < 0)
				continue;
			String key = s.substring(0, s.indexOf("=")).trim();
			String value = s.substring(s.indexOf("=") + 1, s.length()).trim();
			if (!key.equals("")) {
				map.put(key, value);
			}
		}
		return map;
	}

	public static String replaceBlankToOneBlank(String line) {
		String regEx = "[' ']+"; // 一个或多个空格
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(line);
		return m.replaceAll(" ").trim();
	}

	public static boolean isInteger(String str) {
		try {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			return pattern.matcher(str).matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
