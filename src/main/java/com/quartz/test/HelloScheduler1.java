package com.quartz.test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class HelloScheduler1 {
    public static void main(String[] args) throws SchedulerException {
        //1.创建一个jobDetail的实例，将该实例与HelloJob Class绑定
        JobDetail jobDetail = JobBuilder
                .newJob(HelloJob.class) //定义Job类为HelloJob类，真正的执行逻辑所在
                .withIdentity("myJob", "group1") //定义name 和 group
                .usingJobData("message","hello myJob1") //加入属性到jobDataMap
                .usingJobData("FloatJobValue",8.88f) //加入属性到jobDataMap
                .build();
        
        //2.创建一个Trigger触发器的实例，定义该job立即执行，并且每2秒执行一次，一直执行
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();
        //3.创建schedule实例
        StdSchedulerFactory factory = new StdSchedulerFactory();

        Scheduler scheduler = factory.getScheduler();
  
        scheduler.start(); //启动
        scheduler.scheduleJob(jobDetail,trigger); // jobDetail和trigger加入调度

    }
}