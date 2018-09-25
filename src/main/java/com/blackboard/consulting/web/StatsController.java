package com.blackboard.consulting.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blackboard.consulting.service.ContentItemServiceImpl;
import com.blackboard.consulting.service.GradeColumnServiceImpl;
import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.service.QueryUpdateService;
import com.blackboard.consulting.util.CommonUtil;

import blackboard.data.course.CourseMembership;
import blackboard.data.course.CourseMembership.Role;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

@Controller
public class StatsController {

	@Resource(name = "GradeColumnServiceImpl")
	private GradeColumnServiceImpl gradeService;

	@Resource(name = "contentServiceImpl")
	private ContentItemServiceImpl contentService;

	@Resource(name = "QueryExecutionStringServiceImpl")
	private QueryExecutionService<String> qesString;

	@Resource(name = "QueryExecutionListServiceImpl")
	private QueryExecutionService<List<Map<String, Object>>> qesList;

	@Resource(name = "QueryExecutionMapServiceImpl")
	private QueryExecutionService<Map<String, Object>> qesMap;
	
	@Resource(name = "QueryUpdateService")
	private QueryUpdateService qus;

	private static final Logger mLog = LoggerFactory.getLogger(StatsController.class);

	/**
	 * Check current user's role if user is instructor, get the all student's grade
	 * in course. if user is student, get the student's grade in course
	 * @param course_id, userId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/view", method = RequestMethod.GET)
	public String statsView(@RequestParam Map<String, Object> param, 
						  HttpServletRequest request, 
						  Model model) throws Exception {

		mLog.debug("statsView param ::: " + param);
		
		//check user role
		Context ctx = ContextManagerFactory.getInstance().getContext();
		CourseMembership cm = CourseMembershipDbLoader.Default.getInstance()
				.loadByCourseAndUserId(ctx.getCourse().getId(), ctx.getUser().getId());
		
		// when role is student, redirect to user-view page, 
		if(cm.getRole().equals(Role.STUDENT)) {
			return "redirect:view-user?course_id=" + param.get("course_id") + "&user_id="+ ctx.getUser().getBatchUid();
		}else {
			param.put("course_pk1", CommonUtil.getInstance().getPk(param.get("course_id").toString()));
			
			List<Map<String, Object>> resultList = qesList.executeQuery("stats/selectStatsView", param);
			Map<String, Object> resultMap = qesMap.executeQuery("stats/selectTargetCourse", param);
			
			model.addAttribute("resultList", resultList);
			model.addAttribute("resultMap", resultMap);
			model.addAttribute("cutoff", CommonUtil.getInstance().getValue("config/props/config.properties", "cutoff"));
			
			mLog.debug("statsView resultList  ::: " + resultList);
			mLog.debug("statsView resultMap  ::: " + resultMap);
			
			return "stats";
		}// end if
	}

	
	/**
	 * Get the selected student's grade in course
	 * @param course_id,  selectedUserId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/view-user", method = RequestMethod.GET)
	public String statsViewUser(@RequestParam Map<String, Object> param, 
								 HttpServletRequest request, 
								 Model model) throws Exception {

		mLog.debug("statsViewUser param ::: " + param);
		
		param.put("course_pk1", CommonUtil.getInstance().getPk(param.get("course_id").toString()));
		List<Map<String, Object>> resultList = qesList.executeQuery("stats/selectStatsViewUser", param);
		model.addAttribute("resultList", resultList);
		
		mLog.debug("statsViewUser resultList ::: " + resultList);
		
		return "stats-user";
	}
	
	
	/**
	 * Get the selected student's grade in course
	 * @param course_id,  selectedUserId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/view-user-detail", method = RequestMethod.GET)
	public String statsViewUserDetail(@RequestParam Map<String, Object> param, 
								 HttpServletRequest request, 
								 Model model) throws Exception {

		mLog.debug("statsViewUser param ::: " + param);
		
		List<Map<String, Object>> resultList = qesList.executeQuery("stats/selectStatsViewUserDetail", param);
		model.addAttribute("resultList", resultList);
		
		mLog.debug("statsViewUser resultList ::: " + resultList);
		
		return "stats-user-detail";
	}
}
