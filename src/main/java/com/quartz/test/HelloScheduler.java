package com.quartz.test;

import java.util.logging.LogManager;

import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;



public class HelloScheduler {
	public static void main(String[] args) throws SchedulerException {
		Logger log=org.apache.logging.log4j.LogManager.getLogger();
		log.debug("begin");
		// 创建一个jobDetail的实例，将该实例与HelloJob Class绑定
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("myJob").build();
		// 创建一个Trigger触发器的实例，定义该job立即执行，并且每2秒执行一次，一直执行
		SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(4).repeatForever()).build();
		// 创建schedule实例
		StdSchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();
		scheduler.clear();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scheduler.shutdown();
	}
}