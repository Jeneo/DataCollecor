package com.collector.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.collector.service.impl.SchedulerJobServiceImpl;
import com.collector.util.Parameter;

public class App {
	public static String path;
	public static String pathLog;
	public static final String LOG_CONFIG_PATH = "/config/server.log.xml";
	public static final String USAGE = "Expected Parameters: \r\n" + "\t-cmd1 - jobInitialize 初始化\r\n"
			+ "\t-cmd2 - jobSynchronized 同步\r\n" + "\t-info - loadSchedulerJobInfo() 读取当前任务信息\r\n"
			+ "\t-shutdown - shutdown() 关闭任务池\r\n" + "\t-reStart - reStart() 重新开启任务池\r\n"
			+ "\t-start - start() 启动任务池\r\n" + "\t-clean - clean() 清除任务池\r\n" + "\t-run - 运行某个任务  退出\r\n"
			+ "\t-closeapp - closeapp  关闭采集程序\r\n" + "\t-exit - exit  退出当前会话\r\n";

	public static boolean isExit = false;

	static {
		File root = new File(App.class.getResource("/").getPath());
		App.path = root.getPath();
		App.pathLog = path + "/Log/";
		System.setProperty("LOG_HOME", pathLog);
	}

	public static void loadLogConfig() throws FileNotFoundException {
		File logCfg = new File(path + App.LOG_CONFIG_PATH);
		ConfigurationSource source = new ConfigurationSource(new FileInputStream(logCfg), logCfg);
		Configurator.initialize(null, source);
	}

	public static void main(String[] args) throws Exception {
		// Initializ logconfig
		loadLogConfig();
		
		Logger logger = LogManager.getRootLogger();
		logger.info("日志初始化成功,准备读写ClassPathXmlApplicationContext:app.xml");
		// Initializ springConfig
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/config/app.xml");
		SchedulerFactoryBean bean = ctx.getBean(SchedulerFactoryBean.class);
		Scheduler scheduler = bean.getScheduler();
		SchedulerJobServiceImpl schedulerJobServiceImpl = ctx.getBean(SchedulerJobServiceImpl.class);

		logger.info(schedulerJobServiceImpl.loadSchedulerJobInfo());

		KillServer.killPorts(8888);
		Thread.sleep(1000);
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(8888);
			logger.info("已成功创建ServerSocket,可通过telnet 127.0.0.1 8888 运行命令");
			while (true) {
				if (isExit)
					break;
				Socket socket = ss.accept();
				try {
					poseSocket(schedulerJobServiceImpl, socket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			logger.error(e);
		}
		Parameter.getExecutorService().shutdown();// 关闭线程池，否则主线程没法正常关闭
		((ClassPathXmlApplicationContext) ctx).close();
	}

	@SuppressWarnings("static-access")
	public static void poseSocket(SchedulerJobServiceImpl schedulerJobServiceImpl, Socket socket) throws IOException {
		System.out.println("客户端:" + socket.getInetAddress().getLocalHost() + "已连接到服务器");
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端" + System.lineSeparator());
		bw.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while (true) {
			String secStr = br.readLine();
			System.out.println(secStr);
			String msg;
			try {
				msg = "执行成功;";
				if (secStr.equals("cmd1")) {
					schedulerJobServiceImpl.jobInitialize();
				} else if (secStr.equals("cmd2")) {
					schedulerJobServiceImpl.jobSynchronized();
				} else if (secStr.equals("info")) {
					msg = schedulerJobServiceImpl.loadSchedulerJobInfo().toString();
				} else if (secStr.equalsIgnoreCase("shutdown")) {
					schedulerJobServiceImpl.shutDown();
				} else if (secStr.equals("reStart")) {
					schedulerJobServiceImpl.reStart();
				} else if (secStr.equals("clean")) {
					schedulerJobServiceImpl.clean();
				} else if (secStr.equals("start")) {
					schedulerJobServiceImpl.start();
				} else if (secStr.equals("exit")) {
					break;
				} else if (secStr.trim().startsWith("run")) {
					secStr = secStr.trim();
					String jobName = secStr.substring("run".length() + 1);
					System.out.println(jobName);
					schedulerJobServiceImpl.runJob(jobName);
				} else if (secStr.equals("closeapp")) {
					isExit = true;
					break;
				} else if (secStr.equals("")) {
					msg = "";
				} else {
					msg = USAGE;
				}
			} catch (Exception e) {
				msg = e.getMessage();
				e.printStackTrace();
			}
			bw.write(msg + System.lineSeparator());
			bw.flush();
		}
		socket.close();
	}
}
