package com.quartz.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;

public class TriggerDefListener implements TriggerListener {
	Logger logger = LogManager.getLogger("quartz");
	private String propName = "triggerDefListener";

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public TriggerDefListener(String propName) {
		this.propName = propName;
	}

	public TriggerDefListener() {

	}

	@Override
	public String getName() {
		return propName;
	}

	// 触发器触发完成
	// (4) 任务完成时触发
	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		logger.info("触发器触发完成" + trigger);
	}

	// 触发器被触发
	// (1)Trigger被激发 它关联的job即将被运行
	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Trigger被激发 它关联的job即将被运行:" + System.lineSeparator()).append(trigger);
		if (trigger instanceof SimpleTrigger) {
			SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
			sb.append(trigger).append(" RepeatCount: " + simpleTrigger.getRepeatCount());
		}else if (trigger instanceof CronTrigger)
		{
			CronTrigger cronTrigger		 = (CronTrigger) trigger;
			sb.append(trigger).append(" CronExpression: " + cronTrigger.getCronExpression());

		}
		logger.info(sb.toString());
	}

	// 触发器触发失败
	// (3) 当Trigger错过被激发时执行,比如当前时间有很多触发器都需要执行，但是线程池中的有效线程都在工作，
	// 那么有的触发器就有可能超时，错过这一轮的触发。
	@Override
	public void triggerMisfired(Trigger trigger) {
		logger.info("触发器触发失败:" + trigger);
	}

	// (2)Trigger被激发 它关联的job即将被运行,先执行(1)，在执行(2) 如果返回TRUE 那么任务job会被终止
	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}
}
