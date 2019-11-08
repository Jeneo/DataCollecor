package com.collector.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collector.entity.SchedulerJob;
import com.collector.mapper.SchedulerJobMapper;
import com.collector.service.SchedulerJobInfoService;

@Service("schedulerJobServiceInfoImpl")
public class SchedulerJobServiceInfoImpl implements SchedulerJobInfoService {

	@Autowired
	private SchedulerJobMapper schedulerJobMapper;

	@Override
	public List<SchedulerJob> loadDefJob() {
		return schedulerJobMapper.loadDefJob();
	}

}