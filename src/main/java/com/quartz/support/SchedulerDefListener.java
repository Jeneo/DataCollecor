package com.quartz.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;


public class SchedulerDefListener implements SchedulerListener {
	Logger logger = LogManager.getLogger("quartz");

	@Override
	public void jobScheduled(Trigger trigger) {
		String jobName = trigger.getJobKey().getName();
		logger.info(jobName + "部署");
	}

	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		logger.info(triggerKey + "卸载");
	}

	@Override
	public void triggerFinalized(Trigger trigger) {
		logger.info("触发完成" + trigger.getJobKey().getName());
	}

	@Override
	public void triggerPaused(TriggerKey triggerKey) {
		logger.info(triggerKey + "暂停");
	}

	@Override
	public void triggersPaused(String triggerGroup) {
		logger.info("trigger group " + triggerGroup + "暂停");
	}

	@Override
	public void triggerResumed(TriggerKey triggerKey) {
		logger.info(triggerKey + "从暂停中恢复");
	}

	@Override
	public void triggersResumed(String triggerGroup) {
		logger.info("trigger group " + triggerGroup + "从暂停中恢复");
	}

	@Override
	public void jobAdded(JobDetail jobDetail) {
		logger.info(jobDetail.getKey() + "增加");
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		logger.info(jobKey + "删除");
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		logger.info(jobKey + "暂停");
	}

	@Override
	public void jobsPaused(String jobGroup) {
		logger.info("job group " + jobGroup + "暂停");
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		logger.info(jobKey + "从暂停上恢复");
	}

	@Override
	public void jobsResumed(String jobGroup) {
		logger.info("job group " + jobGroup + "从暂停上恢复");
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		logger.error(msg, cause.getUnderlyingException());
		logger.info("正常运行期间产生一个严重错误");
	}

	@Override
	public void schedulerInStandbyMode() {
		logger.info("Scheduler处于StandBy模式");
	}

	@Override
	public void schedulerStarted() {
		logger.info("scheduler开启完成");
	}

	@Override
	public void schedulerStarting() {
		logger.info("scheduler正在开启");
	}

	@Override
	public void schedulerShutdown() {
		logger.info("scheduler停止");
	}

	@Override
	public void schedulerShuttingdown() {
		logger.info("scheduler正在停止");
	}

	@Override
	public void schedulingDataCleared() {
		logger.info("Scheduler中的数据被清除");
	}
}
