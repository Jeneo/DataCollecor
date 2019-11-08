package com.collector.server;

import com.core.util.DButil;

public class Notity {
	private static void smsNotityIfExceptoin(String tel, String msg) {
		msg = msg.replaceAll("'", "");
		if (DButil.dbserver == null)
			return;
		try {
			DButil.execSql("exec [NQIINFODB].[dbo].[Proc_SendSms] '" + tel + "','" + msg + "'");
		} catch (Exception e) {
			System.out.println("eeeee-----------------------");
			e.printStackTrace();
		}

	}

	public static void smsNotityIfExceptoin(String msg) {
		String[] tels = new String[] { "13714654312" };
		for (String string : tels) {
			smsNotityIfExceptoin(string, msg);
		}
	}

}
