package com.collector.controller;

import com.collector.entity.SchedulerJob;
import com.collector.entity.User;
import com.collector.service.SchedulerJobInfoService;
import com.collector.service.SchedulerJobService;
import com.collector.service.UserService;
import com.collector.util.SingleJob;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:abc
 * @Description:
 * @params:$params$
 * @return: $returns$
 * @Date: $date$ $time$
 */

@Controller
public class IndexController {

	@Autowired
	UserService userService;

	@Autowired
	SchedulerJobInfoService schedulerJobInfoService;

	@Autowired
	SchedulerJobService schedulerJobService;
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/list";
	}

	@RequestMapping("/list")
	public String hello(Model model) {
		List<SchedulerJob> schedulerJobs = schedulerJobInfoService.loadDefJob();
		model.addAttribute("schedulerJobs", schedulerJobs);
		return "index";
	}

	@RequestMapping("toAdd")
	public String toAdd() {
		return "add";
	}

	@RequestMapping("/add")
	public String add(User user) {
		userService.add(user);
		return "redirect:/list";
	}

	@RequestMapping("/del/{userId}")
	public String del(@PathVariable String userId) {
		userService.del(userId);
		return "redirect:/list";
	}

	@RequestMapping("/edit/{userId}")
	public String edit(@PathVariable String userId, Model model) {
		User sel = userService.sel(userId);
		model.addAttribute("user", sel);
		return "edit";
	}

	@RequestMapping("/seach")
	public String seach() {
		return "seach";
	}

	@RequestMapping("/seachAll")
	public String seachAll(User user, Model model) {
		String userName = user.getUserName();
		String phone = user.getPhone();
		List<User> user1 = userService.seach(userName, phone);
		model.addAttribute("seachAll", user1);
		return "seach";
	}

	@ResponseBody
	@RequestMapping("/hello")
	public String hello() {
		Logger logger = LogManager.getLogger("quartz");
		System.out.println("你好斯蒂芬斯蒂芬斯蒂芬:" + logger.isDebugEnabled());
		logger.debug("你好斯蒂芬斯蒂芬斯蒂芬");
		return "fsdfsdfsdf";
	}

	@ResponseBody
	@RequestMapping("/invokeJob")
	public String invokeJob(HttpServletRequest request) {
		String jobName = request.getParameter("jobName");
		System.out.println("invokeJob");
		System.out.println(jobName);
		SingleJob singleJob =new SingleJob();
		singleJob.setParam(jobName);
		try {
			singleJob.executeInternal(null);
		} catch (JobExecutionException e) {
			return e.getMessage();
		}		
		return "OK";
	}

	@ResponseBody
	@RequestMapping("/JobInitializable")
	public String JobInitializable(HttpServletRequest request) {
		try {
			schedulerJobService.jobInitialize();	
		} catch (Exception e) {
			return e.getMessage();
		}
		return "初始化成功!";		
	}

	@ResponseBody
	@RequestMapping("/JobInfo")
	public String JobInfo(HttpServletRequest request) {
		try {
			return schedulerJobService.loadSchedulerJobInfo().toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@ResponseBody
	@RequestMapping("/JobClean")
	public String JobClean(HttpServletRequest request) {
		try {
			schedulerJobService.clean();	
		} catch (Exception e) {
			return e.getMessage();
		}
		return "清除成功!";		
	}
	@ResponseBody
	@RequestMapping("/JobStart")
	public String JobStart(HttpServletRequest request) {
		try {
			schedulerJobService.start();
		} catch (Exception e) {
			return e.getMessage();
		}
		return "JobStart成功!";		
	}	
}
