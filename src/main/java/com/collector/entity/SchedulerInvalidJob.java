package com.collector.entity;

public class SchedulerInvalidJob {
	private String trigger_name;
	private String trigger_group;
	private String job_name;
	private String job_group;
	public String getTrigger_name() {
		return trigger_name;
	}
	public void setTrigger_name(String trigger_name) {
		this.trigger_name = trigger_name;
	}
	public String getTrigger_group() {
		return trigger_group;
	}
	public void setTrigger_group(String trigger_group) {
		this.trigger_group = trigger_group;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getJob_group() {
		return job_group;
	}
	public void setJob_group(String job_group) {
		this.job_group = job_group;
	}
	@Override
	public String toString() {
		return "SchedulerInvalidJobBO [trigger_name=" + trigger_name + ", trigger_group=" + trigger_group
				+ ", job_name=" + job_name + ", job_group=" + job_group + "]";
	}
	
}
