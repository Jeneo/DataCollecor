package com.quartz.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Unit test for simple App.
 */

public class AppTest {

	public void testApp() {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入第二串字符：");
		String secStr = null;
		while (true) {
			try {
				secStr = bf.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("你输入的是" + secStr);
		}
	}
}
