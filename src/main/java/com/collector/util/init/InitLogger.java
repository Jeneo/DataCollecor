package com.collector.util.init;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.collector.server.App;
import com.collector.server.IStepRunnable;
import com.collector.util.Parameter;

/*
 * 根据任务创建日志
 * 
 * 
 */
public class InitLogger implements IStepRunnable {
	private String jobName;
	private String pathLog = App.pathLog;
	private ReentrantLock lock = new ReentrantLock();
	private final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
	private final Configuration config = ctx.getConfiguration();

	private boolean checkLoggerExists() {
		if (config.getAppender(jobName) != null || config.getLoggers().get(jobName) != null) {
			return true;
		}
		return false;
	}

	public InitLogger() {
	}

	public InitLogger(String jobName, String pathLog) {
		this();
		this.jobName = jobName;
		this.pathLog = pathLog;
	}

	public InitLogger(String jobName) {
		this();
		this.jobName = jobName;
	}

	private void doCreateLogger() {
		try {
			lock.lock();
			if (checkLoggerExists()) {
				return;
			}
			final PatternLayout layout = PatternLayout.newBuilder().withCharset(Charset.forName("UTF-8"))
					.withConfiguration(config)
					.withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n").build();
			final TriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy("1 MB");
			RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withMax("3").build();
			final RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.newBuilder()
					.setName(jobName).withImmediateFlush(true).withFileName(pathLog + jobName + ".log")
					.withFilePattern(pathLog + "/%d{yyyy-MM}/" + jobName + "-%d{yyyy-MM-dd}-%i.gz")
					.withStrategy(strategy).withPolicy(policy).setLayout(layout).build();
			appender.start();
			config.addAppender(appender);
			LoggerConfig loggerConfig = new LoggerConfig(jobName, Level.DEBUG, true);
			loggerConfig.addAppender(appender, Level.DEBUG, null);
			config.addLogger(jobName, loggerConfig);
			ctx.updateLoggers(config);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public boolean createLogger() {
		doCreateLogger();
		return checkLoggerExists();
	}

	public boolean execute(Map<String, Object> parameter) throws Exception {
		jobName = Parameter.getArgCfg(parameter);
		return createLogger();
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

}
