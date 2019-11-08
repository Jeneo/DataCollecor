package com.quartz.support;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import com.collector.entity.SchedulerJob;

public class SchedulerJobWrapper {
	public static final String DEFAULT_JOB_GROUP = "JobDef";
	public static final String DEFAULT_TRIGGER_GROUP = "TriggerDef";

	public static final String MAP_KEY_PARAM = "param";
	public static final String MAP_KEY_VERSION = "version";
	private SchedulerJob schedulerJobBO;
	private JobKey jobKey = null;
	private TriggerKey triggerKey = null;
	private JobDataMap jobDataMap = new JobDataMap();

	private void init() {
		jobKey = new JobKey(schedulerJobBO.getJobName(), SchedulerJobWrapper.DEFAULT_JOB_GROUP);
		triggerKey = new TriggerKey(schedulerJobBO.getJobName(), SchedulerJobWrapper.DEFAULT_TRIGGER_GROUP);
		getJobDataMap().put(MAP_KEY_PARAM, schedulerJobBO.getParam());
		getJobDataMap().put(MAP_KEY_VERSION, schedulerJobBO.getVersion());
	}

	public SchedulerJobWrapper() {
	}

	public SchedulerJobWrapper(SchedulerJob schedulerJobBO) {
		this.setSchedulerJobBO(schedulerJobBO);
		init();
	}

	public SchedulerJob getSchedulerJobBO() {
		return schedulerJobBO;
	}

	public void setSchedulerJobBO(SchedulerJob schedulerJobBO) {
		this.schedulerJobBO = schedulerJobBO;
		init();
	}

	public JobKey getJobKey() {
		return jobKey;
	}

	public TriggerKey getTriggerKey() {
		return triggerKey;
	}

	public JobDataMap getJobDataMap() {
		return jobDataMap;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Job> getCls() throws ClassNotFoundException {
		return (Class<? extends Job>) Class.forName(schedulerJobBO.getJobClassPath());
	}

	public void run() throws Exception{
		QuartzJobUtil.runOnceJob(jobKey);
	}
	public void add() throws Exception {
		QuartzJobUtil.addJob(getJobKey(), getTriggerKey(), getCls(), getSchedulerJobBO().getCronExpression(),
				getSchedulerJobBO().getDescription(), jobDataMap);
	}

	public void dele() throws Exception {
		QuartzJobUtil.removeJob(getJobKey(), getTriggerKey());
	}

	public boolean isExists() throws Exception {
		return QuartzJobUtil.ExistsJob(getJobKey(), getTriggerKey());
	}

	public boolean isVersionChange() throws Exception {
		JobDataMap jobDataMapSched = QuartzJobUtil.getJobDataMap(jobKey, triggerKey);
		return jobDataMapSched == null
				|| (jobDataMapSched.getInt(MAP_KEY_VERSION) != jobDataMap.getInt(MAP_KEY_VERSION));

	}

	public void synchronizedJob() throws Exception {
		State state = new DisabledState();
		state.post(this);
	}

	public abstract class State {
		public abstract void post(SchedulerJobWrapper schedulerJobWrapper) throws Exception;
	}

	public class DisabledState extends State {
		// �ж��Ƿ�ʧЧ,��:ɾ��,��:��������״̬
		public void post(SchedulerJobWrapper schedulerJobWrapper) throws Exception {
			if (schedulerJobWrapper.getSchedulerJobBO().getJobState() == SchedulerJob.SCHEDULER_DISABLE_STATE
					&& isExists())
				schedulerJobWrapper.dele();
			else
				new AddState().post(schedulerJobWrapper);
		}
	}

	public class AddState extends State {
		// �ж��Ƿ���Ҫ����,��:����,��:�޸�
		public void post(SchedulerJobWrapper schedulerJobWrapper) throws Exception {
			if (!schedulerJobWrapper.isExists())
				schedulerJobWrapper.add();
			else
				new ModiState().post(schedulerJobWrapper);
		}
	}
	public class ModiState extends State {
		// �ж��Ƿ���Ҫ�޸�,��:�޸�,��:����
		public void post(SchedulerJobWrapper schedulerJobWrapper) throws Exception {
			if (schedulerJobWrapper.isVersionChange()) {
				schedulerJobWrapper.dele();
				schedulerJobWrapper.add();
			}
		}
	}
}
