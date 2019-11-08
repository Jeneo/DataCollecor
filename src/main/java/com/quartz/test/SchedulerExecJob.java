package com.quartz.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SchedulerExecJob implements Job {
	private static Logger logger = LoggerFactory.getLogger(SchedulerExecJob.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobName = context.getJobDetail().getKey().getName();


	}

	public static String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getAddress() {
		return "http://localhost:" + ResourceBundle.getBundle("application").getString("server.port");
	}
}
