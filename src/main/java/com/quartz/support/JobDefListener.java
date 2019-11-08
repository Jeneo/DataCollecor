package com.quartz.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class JobDefListener implements JobListener {
	Logger logger = LogManager.getLogger("quartz");
	private String propName = "JobDefListener";


	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public JobDefListener(String propName) {
		this.propName = propName;
	}

	public JobDefListener() {

	}

	@Override
	public String getName() {
		return propName;
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		logger.info("Job即将被执行:" + context.getJobDetail().getKey().getName());

	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("Job被trigger忽略:" + context.getJobDetail().getKey().getName());

	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (jobException == null)
			logger.info("Job执行完成:" + context.getJobDetail().getKey().getName() + " 没有出错");
		else {
			logger.info("Job执行完成:" + context.getJobDetail().getKey().getName() + " 出错");
			logger.error(jobException);
		}

	}

}
