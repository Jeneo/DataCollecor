package com.quartz.test;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;


import java.text.SimpleDateFormat;
import java.util.Date;
@DisallowConcurrentExecution
public class HelloJob implements Job {
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //打印当前的执行时间 例如 2017-11-22 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在的时间是："+ sf.format(date));
        //具体的业务逻辑
        System.out.println("开始生成任务报表 或 开始发送邮件....");
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        System.out.println("jobDetail 的name ： "+key.getName());     //打印jobDetail 的name
        System.out.println("jobDetail 的group ： "+key.getGroup());    //打印jobDetail 的group
        JobDataMap jobDetailDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String message = jobDetailDataMap.getString("message"); //
        float floatJobValue = jobDetailDataMap.getFloat("FloatJobValue");
        System.out.println("jobDataMap定义的message的值 : "+message );  //打印jobDataMap定义的message的值 
        System.out.println("jobDataMap定义的floatJobValue的值 : "+floatJobValue );   //jobDataMap定义的floatJobValue的值
        System.out.println(jobDetailDataMap);
        System.out.println(jobExecutionContext.getTrigger());
        try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
