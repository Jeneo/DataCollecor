package com.quartz.support;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

import com.collector.entity.SchedulerInvalidJob;

public class SchedulerInvalidJobWrapper {
	private SchedulerInvalidJob schedulerInvalidJobBO;
	private JobKey jobKey = null;
	private TriggerKey triggerKey = null;

	private void init() {
		jobKey = new JobKey(schedulerInvalidJobBO.getJob_name(), schedulerInvalidJobBO.getJob_group());
		triggerKey = new TriggerKey(schedulerInvalidJobBO.getTrigger_name(), schedulerInvalidJobBO.getTrigger_group());
	}

	public SchedulerInvalidJobWrapper() {
	}

	public SchedulerInvalidJobWrapper(SchedulerInvalidJob schedulerInvalidJobBO) {
		this.setSchedulerInvalidJobBO(schedulerInvalidJobBO);
		init();
	}

	public SchedulerInvalidJob getSchedulerInvalidJobBO() {
		return schedulerInvalidJobBO;
	}

	public void setSchedulerInvalidJobBO(SchedulerInvalidJob schedulerInvalidJobBO) {
		this.schedulerInvalidJobBO = schedulerInvalidJobBO;
		init();
	}

	public void deleJob() throws Exception {
		QuartzJobUtil.removeJob(getJobKey(), getTriggerKey());
	}

	public JobKey getJobKey() {
		return jobKey;
	}

	public TriggerKey getTriggerKey() {
		return triggerKey;
	}

}
