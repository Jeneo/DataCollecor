package com.collector;

import java.io.File;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@MapperScan("com.collector.mapper") // 扫描的mapper
@SpringBootApplication
public class DataCollecorApplication {
	public static String path;
	public static String pathLog;
	public static void main(String[] args) {
		File root = new File(DataCollecorApplication.class.getResource("/").getPath());
		path = root.getPath();
		pathLog = path + "/Log/";
		System.setProperty("LOG_HOME", pathLog);
		SpringApplication.run(DataCollecorApplication.class, args);
	}

}
