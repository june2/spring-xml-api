package com.blackboard.consulting.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blackboard.consulting.service.ContentItemServiceImpl;
import com.blackboard.consulting.service.GradeColumnServiceImpl;
import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.service.QueryUpdateService;
import com.blackboard.consulting.util.CommonUtil;

import blackboard.data.content.Content;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import net.sf.json.JSONObject;

/**
 * @author June
 * @since 2016.01.19
 */
@Controller
public class CommonController {

	@Resource(name = "GradeColumnServiceImpl")
	private GradeColumnServiceImpl gradeService;

	@Resource(name = "contentServiceImpl")
	private ContentItemServiceImpl contentService;

	@Resource(name = "QueryExecutionStringServiceImpl")
	private QueryExecutionService<String> qesString;

	@Resource(name = "QueryExecutionListServiceImpl")
	private QueryExecutionService<List<Map<String, Object>>> qesList;

	@Resource(name = "QueryUpdateService")
	private QueryUpdateService qus;

	private static final Logger mLog = LoggerFactory.getLogger(CommonController.class);


	/**
	 * select Online Content Type 
	 * @param param
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = { "/app/selectLocation" })
	public String selectLocation(@RequestParam Map<String, Object> param, Model model) throws Exception {

		Context ctx = ContextManagerFactory.getInstance().getContext();
		CommonUtil util = CommonUtil.getInstance();

		// Get B2 configuration
		Map<String, Object> resultMap = util.getValues("config/props/config.properties");
		mLog.debug("indexPage param ::: " + resultMap);

		param.put("course_id", util.getPk(param.get("course_id").toString()));
		param.put("userId", ctx.getUser().getBatchUid());

		// Get course user role
		String result = qesString.executeQuery("common/selectUserCrsRole", param);
		mLog.debug("indexPage result :: " + result);

		// get Course Toc.
		List<Content> tocList = contentService.getToc(ctx.getCourseId());

		model.addAttribute("crsBatchUid", ctx.getCourse().getBatchUid());
		model.addAttribute("tocList", tocList);
		model.addAttribute("course_id", ctx.getCourse().getCourseId());
		model.addAttribute("configMap", resultMap);
		model.addAttribute("role", result);

		return "common/selectLocation";
	}

	/**
	 * Get week menus by course id and selected toc(menu) name
	 * @param crsId, menu
	 * @return JSON
	 * @throws PersistenceException
	 * @throws KeyNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value = "/app/getWeekList")
	public void getWeekList(@RequestParam Map<String, Object> param, HttpServletRequest request,
			HttpServletResponse response, Model model) throws KeyNotFoundException, PersistenceException, IOException {

		mLog.debug("call ::: getWeeksList ::  " + param);
		List<Map<String, Object>> weekList = contentService.getWeek(param.get("crsId").toString(),
				param.get("menu").toString());

		JSONObject json = new JSONObject();
		json.put("weekList", weekList);

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}

	/**
	 * Check if selected user's grade exist in custom table or not.
	 * @param course pk1, user id
	 * @return JSON
	 * @throws IOException
	 */
	@RequestMapping(value = "/app/existGradeData")
	public void existGradeData(@RequestParam Map<String, Object> param, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		mLog.debug("call ::: existGradeData ::  " + param);

		List<Map<String, Object>> resultList = qesList.executeQuery("common/selectExistGrade", param);
		boolean result = false;
		if (resultList.size() > 0)
			result = true;

		mLog.debug("call ::: resultList ::  " + resultList);

		mLog.debug("call ::: result ::  " + result);
		JSONObject json = new JSONObject();
		json.put("result", result);

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}

	/**
	 * Check if selected user's grade exist in custom table or not.
	 * @param course pk1, user id
	 * @return JSON
	 * @throws IOException
	 */
	@RequestMapping(value = "/app/existContentLogData")
	public void existContentLogData(@RequestParam Map<String, Object> param, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {

		mLog.debug("call ::: existContentLogData ::  " + param);

		List<Map<String, Object>> resultList = qesList.executeQuery("common/selectExistLog", param);
		boolean result = false;
		if (resultList.size() > 0)
			result = true;

		mLog.debug("call ::: resultList ::  " + resultList);

		JSONObject json = new JSONObject();
		json.put("result", result);
		if (resultList.size() > 0) {
			json.put("user_id", resultList.get(0).get("user_id"));
			json.put("online_content_pk1", resultList.get(0).get("online_content_pk1"));
		}
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}

}
