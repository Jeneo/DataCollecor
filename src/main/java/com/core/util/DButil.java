package com.core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DButil {
	public static Dbserver dbserver;
 
	public static Connection getConnection() throws Exception {
		Connection conn = null;
		Class.forName(dbserver.getDriver());
		conn = DriverManager.getConnection(dbserver.getUrl(), dbserver.getUsername(), dbserver.getPassword());
		return conn;
	}

	public static void execSql(String sql) throws Exception {
		Connection conn = null; // 和数据库取得连接
		Statement clStatement = null; // 创建statement
		System.out.println(sql);
		try { 
			conn = com.core.util.DButil.getConnection();
			clStatement = conn.createStatement();
			clStatement.execute(sql);
		} finally {
			if (clStatement != null)
				try {
					clStatement.close();
				} catch (SQLException e1) {
				}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
				}
			}
		}
	}

}
