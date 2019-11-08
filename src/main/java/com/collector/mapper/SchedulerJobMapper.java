package com.collector.mapper;

import java.util.List;

import com.collector.entity.SchedulerInvalidJob;
import com.collector.entity.SchedulerJob;


public interface SchedulerJobMapper {
	 List<SchedulerJob> loadDefJob();	 
	 SchedulerJob loadDefJobById(int id);
	 SchedulerJob loadDefJobByName(String jobName); 
	 void updateVersionById(int id);
	 List<SchedulerInvalidJob> loadInvalidJob();	 
	 
}