package com.collector.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collector.entity.SchedulerInvalidJob;
import com.collector.entity.SchedulerJob;
import com.collector.mapper.SchedulerJobMapper;
import com.collector.service.SchedulerJobService;
import com.quartz.support.QuartzJobUtil;
import com.quartz.support.SchedulerInvalidJobWrapper;
import com.quartz.support.SchedulerJobWrapper;

@Service("schedulerJobServiceImpl")
public class SchedulerJobServiceImpl implements SchedulerJobService {

	@Autowired
	private SchedulerJobMapper schedulerJobMapper;

	public SchedulerJobMapper getSchedulerJobMapper() {
		return schedulerJobMapper;
	}

	public void setSchedulerJobMapper(SchedulerJobMapper schedulerJobMapper) {
		this.schedulerJobMapper = schedulerJobMapper;
	}

	@Override
	public void jobInitialize() throws Exception {
		QuartzJobUtil.cleanJobs();
		List<SchedulerJob> schedulerJobBOs = schedulerJobMapper.loadDefJob();
		for (SchedulerJob schedulerJobBO : schedulerJobBOs) {
			if (schedulerJobBO.getJobState() == SchedulerJob.SCHEDULER_DISABLE_STATE) {
				continue;
			}
			new SchedulerJobWrapper(schedulerJobBO).add();
		}
	}

	@Override
	public void jobSynchronized() throws Exception {
		deleInvalidJob();
		synchronizedJob();
	}

	public void deleInvalidJob() throws Exception {
		SchedulerInvalidJobWrapper schedulerInvalidJobWrapper = new SchedulerInvalidJobWrapper();
		List<SchedulerInvalidJob> schedulerInvalidJobBOs = schedulerJobMapper.loadInvalidJob();
		for (SchedulerInvalidJob schedulerInvalidJobBO : schedulerInvalidJobBOs) {
			schedulerInvalidJobWrapper.setSchedulerInvalidJobBO(schedulerInvalidJobBO);
			schedulerInvalidJobWrapper.deleJob();
		}
	}

	public void synchronizedJob() throws Exception {
		SchedulerJobWrapper schedulerJobWrapper = new SchedulerJobWrapper();
		List<SchedulerJob> schedulerJobBOs = schedulerJobMapper.loadDefJob();
		for (SchedulerJob schedulerJobBO : schedulerJobBOs) {
			schedulerJobWrapper.setSchedulerJobBO(schedulerJobBO);
			schedulerJobWrapper.synchronizedJob();
		}
	}

	@Override
	public StringBuffer loadSchedulerJobInfo() throws Exception {
		return QuartzJobUtil.loadSchedulerJob();
	}

	@Override
	public void shutDown() throws Exception {
		QuartzJobUtil.shutdownJobs();

	}

	@Override
	public void reStart() throws Exception {
		QuartzJobUtil.shutdownJobs();
		QuartzJobUtil.startJobs();
	}

	@Override
	public void clean() throws Exception {
		QuartzJobUtil.cleanJobs();
		
	}

	@Override
	public void start() throws Exception {
		QuartzJobUtil.startJobs();
		
	}

	@Override
	public void runJob(String jobName) throws Exception {		
		SchedulerJob schedulerJobBO=schedulerJobMapper.loadDefJobByName(jobName);
		if (schedulerJobBO==null)
			throw new Exception("job not exists");
		SchedulerJobWrapper schedulerJobWrapper = new SchedulerJobWrapper(schedulerJobBO);
		schedulerJobWrapper.run();
	}
}