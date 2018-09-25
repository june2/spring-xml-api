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
import org.springframework.web.bind.annotation.RequestParam;

import com.blackboard.consulting.service.ContentItemServiceImpl;
import com.blackboard.consulting.service.GradeColumnServiceImpl;
import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.util.CommonUtil;
import com.panopto.services.ISessionManagement;
import com.panopto.services.ListSessionsResponse;

import blackboard.data.content.Content;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

/**
 * @author June
 * @since 2015.11.12
 */
@Controller
public class IndexController {

	private static final Logger mLog = LoggerFactory.getLogger(IndexController.class);

	@Resource(name = "GradeColumnServiceImpl")
	private GradeColumnServiceImpl gradeService;

	@Resource(name = "contentServiceImpl")
	private ContentItemServiceImpl contentService;

	@Resource(name = "QueryExecutionStringServiceImpl")
	private QueryExecutionService<String> qesString;

	ISessionManagement sessionManagement;
	ListSessionsResponse listResponse;
	
	/**
	 * If grade column isn't exist, create column in the grade center.
	 * And get current user's role in course.
	 * 
	 * @param course_id, userId
	 * @return String
	 * @throws PersistenceException 
	 * @throws KeyNotFoundException 
	 */
	@RequestMapping(value = "/index")
	public String index(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model)
			throws Exception {
		
		mLog.debug("call ::: index");
		
		Context ctx = ContextManagerFactory.getInstance().getContext();		
		
		String perTitle = "Panato 온라인 출석 진도율"; // online attendance percentage
		String statusTitle = "Panato 온라인 출석 상태"; // online attendance status

		// create gradeColumn
		if (!gradeService.initGradeCenter(ctx.getCourseId(), perTitle)) {
			gradeService.createGradeColumn(ctx.getCourse(), perTitle);
		}

		if (!gradeService.initGradeCenter(ctx.getCourseId(), statusTitle)) {
			gradeService.createGradeColumn(ctx.getCourse(), statusTitle);
		}

		CommonUtil util = CommonUtil.getInstance();

		// Get B2 configuration
		Map<String, Object> resultMap = util.getValues("config/props/config.properties");
		mLog.debug("indexPage param ::: " + resultMap);

		// param.put("course_id", util.getPk(param.get("course_id").toString()));
		param.put("userId", ctx.getUser().getBatchUid());
		param.put("crsPk1", util.getPk(param.get("course_id").toString()));

		// Get course user role
		String role = qesString.executeQuery("common/selectUserCrsRole", param);
		mLog.debug("indexPage result :: " + role);

		// get Course Toc.
		List<Content> tocList = contentService.getToc(ctx.getCourseId());

		model.addAttribute("crsBatchUid", ctx.getCourse().getBatchUid());
		model.addAttribute("tocList", tocList);
		model.addAttribute("course_id", ctx.getCourse().getCourseId());
		model.addAttribute("configMap", resultMap);
		model.addAttribute("role", role);
		
		return "index";
	}

	// index-module page
	@RequestMapping(value = "/indexModule")
	public String indexModule() throws Exception {

		mLog.debug("call ::: index-module");
		return "index-module";
	}
	
	// index-module page
	@RequestMapping(value = "/sso")
	public String sso() throws Exception {
		mLog.debug("call ::: sso.jsp");		
		return "sso";
	}
}
