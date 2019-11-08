package com.collector.util;

import java.util.Map;
import java.util.concurrent.locks.Lock;

import org.springframework.context.ApplicationContext;

public abstract class ParameterBasic {
	protected String paramJobName;
	protected Lock lock;
	protected Map<String, Object> parameter;
	protected ApplicationContext ctx;

	public ApplicationContext getCtx() {
		return ctx;
	}

	public void setCtx(Map<String, Object> parameter) {
		ctx = Parameter.getApplicationContext(parameter);
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
		setCtx(parameter);
	}

	public void setLogger(Map<String, Object> parameter) {

	}

	public Map<String, Object> getParameter() {
		return parameter;
	}

	protected abstract void doSaveParameter();

	public void saveParameter() {
		if (lock != null) {
			lock.lock();
			try {
				doSaveParameter();
			} finally {
				lock.unlock();
			}
		} else {
			doSaveParameter();
		}
	}

	public String getParmJobName() {
		return paramJobName;
	}

	public void setParmJobName(Map<String, Object> parameter) {
		this.paramJobName = Parameter.getArgCfg(parameter);
	}

}
