package com.collector.entity;

public class SchedulerJob {
	public static final int SCHEDULER_ABLE_STATE = 1;
	public static final int SCHEDULER_DISABLE_STATE = 0;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobClassPath() {
		return jobClassPath;
	}

	public void setJobClassPath(String jobClassPath) {
		this.jobClassPath = jobClassPath;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getJobState() {
		return jobState;
	}

	public void setJobState(int jobState) {
		this.jobState = jobState;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "SchedulerJobBO [id=" + id + ", jobName=" + jobName + ", jobClassPath=" + jobClassPath
				+ ", cronExpression=" + cronExpression + ", jobState=" + jobState + ", description=" + description
				+ ", param=" + param + ", version=" + version + "]";
	}

	private Integer id;
	private String jobName;
	private String jobClassPath;
	private String cronExpression;
	private Integer jobState;
	private String description;
	private String param;
	private int version;

}
