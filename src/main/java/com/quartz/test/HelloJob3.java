package com.quartz.test;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.parser.Entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.ExecuteInJTATransaction;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HelloJob3 extends QuartzJobBean {
	private String jobService;
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getJobService() {
		return jobService;
	}

	public void setJobService(String jobService) {
		this.jobService = jobService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//		System.out.println(this);		
//		 JobDataMap map = context.getJobDetail().getJobDataMap();
//		System.out.println("map.entrySet().size()=" + map.entrySet().size());
//		count++;
//		map.put("count", count);
//		for (Entry<String, Object> entry : map.entrySet()) {
//			String mapKey = entry.getKey();
//			Object mapValue = entry.getValue();
//			System.out.println(mapKey + ":" + mapValue.getClass() + ":" + mapValue);
//		}
//		System.out.println(context.getMergedJobDataMap());
//		try {
//			System.out.println(context.getScheduler().getTriggerState(context.getTrigger().getKey()));
//
//		} catch (SchedulerException e1) {
//
//			e1.printStackTrace();
//		}
		String jobName = context.getJobDetail().getKey().getName();
		String jobGroup = context.getJobDetail().getKey().getGroup();
		Logger logger = LogManager.getLogger(this.getClass());
		try {
			logger.info(System.lineSeparator() + System.lineSeparator() + jobName + "(" + jobGroup + ") is doing "
					+ this + System.lineSeparator());
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "HelloJob3 [jobService=" + jobService + ", count=" + count + ", class=" + getClass() + "]";
	}

}
