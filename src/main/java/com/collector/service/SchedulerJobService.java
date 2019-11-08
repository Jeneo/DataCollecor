package com.collector.service;

/**
 * @author jj
 *	jobInitialize() 
 *		初始化平台,清空所有任务,并重新加载QUARTZJOBINFO.dbo.USER_DEF_SCHEDULERJOB重新生成任务池
 *
 *	jobSynchronized()
 *		同步QUARTZJOBINFO.dbo.USER_DEF_SCHEDULERJOB,修改任务池信息
 *
 *	loadSchedulerJobInfo()
 *		获取任务池信息
 *	shutDown()
 *		关闭任务池
 *	reStart()
 *		重启任务池
 *	clean()
 *		清除任务池
 */
public interface SchedulerJobService {
	void jobInitialize () throws Exception;
	void jobSynchronized() throws Exception;
	StringBuffer loadSchedulerJobInfo() throws Exception;
	void shutDown() throws Exception;
	void reStart() throws Exception;
	void clean() throws Exception;
	void start() throws Exception;
	void runJob(String jobName) throws Exception;
}
