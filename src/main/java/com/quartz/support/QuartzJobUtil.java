package com.quartz.support;

import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.collector.SpringUtil;

public class QuartzJobUtil {

	public static Scheduler sched = SpringUtil.context.getBean(SchedulerFactoryBean.class).getScheduler();

	public static JobDetail createJobDetail(JobKey jobKey, Class<? extends Job> cls, String description,
			JobDataMap jobDataMap) {
		JobDetail jd = JobBuilder.newJob(cls).storeDurably().requestRecovery(false).withIdentity(jobKey)
				.setJobData(jobDataMap).build();// jobDetail
		if (description != null) {
			jd.getJobBuilder().withDescription(description);
		}
		return jd;
	}

	public static SimpleTrigger createTrigger(TriggerKey triggerKey, int repeatIntevalTime) {
		SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(repeatIntevalTime).repeatForever())
				.startNow().build();
		return trigger;
	}

	public static CronTrigger createTrigger(TriggerKey triggerKey, String cron) {
		TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();// 触发器
		triggerBuilder.withIdentity(triggerKey);// 触发器名,触发器组
		triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing());// 触发器执行规则
		CronTrigger trigger = (CronTrigger) triggerBuilder.build();// 创建CronTrigger对象
		return trigger;
	}

	public static Trigger createTrigger(TriggerKey triggerKey, Integer repeatIntevalTime, String cron) {
		Trigger trigger;
		if (repeatIntevalTime != null) {
			trigger = createTrigger(triggerKey, repeatIntevalTime);
		} else {
			trigger = createTrigger(triggerKey, cron);
		}
		return trigger;
	}

	public static void scheduleJobSetDefTriggerJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		sched.scheduleJob(jobDetail, trigger);// 设置调度器
//		startJobs();
	}

	public static void addJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> cls, int repeatIntevalTime,
			String description, JobDataMap jobDataMap) throws SchedulerException {
		addJob(jobKey, triggerKey, cls, repeatIntevalTime, null, description, jobDataMap);
	}

	public static void addJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> cls, String cron,
			String description, JobDataMap jobDataMap) throws SchedulerException {
		addJob(jobKey, triggerKey, cls, null, cron, description, jobDataMap);
	}

	public static void addJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> cls, Integer repeatIntevalTime,
			String cron, String description, JobDataMap jobDataMap) throws SchedulerException {
		JobDetail jobDetail = createJobDetail(jobKey, cls, description, jobDataMap);
		if (repeatIntevalTime == null && cron == null) {
			throw new SchedulerException("repeatIntevalTime or cron can't all null");
		}
		Trigger trigger = createTrigger(triggerKey, repeatIntevalTime, cron);
		scheduleJobSetDefTriggerJob(jobDetail, trigger);
	}

	public static boolean ExistsJob(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
		if (sched.getTrigger(triggerKey) == null || sched.getTrigger(triggerKey).getJobKey() == null)
			return false;
		return sched.getTrigger(triggerKey).getJobKey().equals(jobKey);
	}

	public static JobDataMap getJobDataMap(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
		if (!ExistsJob(jobKey, triggerKey))
			return null;
		return sched.getJobDetail(jobKey).getJobDataMap();
	}

	public static void modifyJobTime(TriggerKey triggerKey, String cron) throws SchedulerException {
		modifyJobTime(triggerKey, null, cron);
	}

	public void modifyJobTime(TriggerKey triggerKey, int repeatIntervalTime) throws SchedulerException {
		modifyJobTime(triggerKey, repeatIntervalTime, null);
	}

	public static void modifyJobTime(TriggerKey triggerKey, Integer repeatIntervalTime, String cron)
			throws SchedulerException {
		if (sched.getTrigger(triggerKey) == null) {
			return;
		}
		if (sched.getTrigger(triggerKey).getClass().equals(SimpleTrigger.class)) {
			long oldTime = ((SimpleTrigger) sched.getTrigger(triggerKey)).getRepeatInterval();
			if (oldTime != repeatIntervalTime) {
				/** 方式一 ：调用 rescheduleJob 开始 */
				Trigger trigger = createTrigger(triggerKey, repeatIntervalTime);
				sched.rescheduleJob(triggerKey, trigger);// 修改一个任务的触发时间
				/** 方式一 ：调用 rescheduleJob 结束 */
			}
		} else {
			String oldTime = ((CronTrigger) sched.getTrigger(triggerKey)).getCronExpression();
			if (!oldTime.equalsIgnoreCase(cron)) {
				Trigger trigger = createTrigger(triggerKey, cron);
				sched.rescheduleJob(triggerKey, trigger);// 修改一个任务的触发时间
			}
		}
		/** 方式二：先删除，然后在创建一个新的Job */
		// JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName,
		// jobGroupName));
		// Class<? extends Job> jobClass = jobDetail.getJobClass();
		// removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
		// addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
		/** 方式二 ：先删除，然后在创建一个新的Job */
	}

	public static void removeJob(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
		sched.pauseTrigger(triggerKey);// 停止触发器
		sched.unscheduleJob(triggerKey);// 移除触发器
		sched.deleteJob(jobKey);// 删除任务
	}

	public static void runOnceJob(JobKey jobKey) throws SchedulerException {
		sched.triggerJob(jobKey);
	}

	public static void startJobs() throws SchedulerException {
		if (!sched.isShutdown()) {
			sched.start();
		}
	}

	public static void shutdownJobs() throws SchedulerException {
		if (!sched.isShutdown()) {
			sched.shutdown(true);
		}
	}

	public static void cleanJobs() throws SchedulerException {
		sched.clear();
	}

	public static StringBuffer loadSchedulerJob() throws SchedulerException {
		String format = ("%-25s %-10s %-10s %-50s %-30s %-30s %-30s %s");
		String head = String.format(format, "jobname", "group", "state", "class", "cron", "nextTime", "previousTime",
				"param");
		StringBuffer sb = new StringBuffer(32);
		StringBuffer info = new StringBuffer(32);
		int count = 0;
		for (String groupName : sched.getJobGroupNames()) {
			for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				JobDetail jobDetail = sched.getJobDetail(jobKey);
				JobDataMap jbDataMap = jobDetail.getJobDataMap();
				@SuppressWarnings("unchecked")
				List<Trigger> triggers = (List<Trigger>) sched.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					String cron;
					if (trigger instanceof CronTrigger) {
						cron = ((CronTrigger) trigger).getCronExpression();
					} else {
						cron = "";
					}
					count++;
					TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
					String body = String.format(format, jobKey.getName(), jobKey.getGroup(),
							sched.getTriggerState(triggerKey), trigger.getClass(), cron, trigger.getNextFireTime(),
							trigger.getPreviousFireTime(), "");
					info.append(body);
					for (Map.Entry<String, Object> entry : jbDataMap.entrySet()) {
						info.append("{" + entry.getKey() + "=" + entry.getValue() + "}");
					}
					info.append(System.lineSeparator());
				}
			}
		}
		String sum = String.format("Job  (count : %d )", count);
		sb.append(sum).append(System.lineSeparator()).append(head).append(System.lineSeparator())
				.append(info.toString());
		return sb;

	}
}
