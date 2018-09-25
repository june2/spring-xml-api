package com.blackboard.consulting.web;

import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.panopto.blackboard.PanoptoData;
import com.panopto.services.Folder;
import com.panopto.services.Session;

import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import net.sf.json.JSONObject;

/**
 * @author June
 * @since 2015.11.12
 */
@Controller
public class PanoptoController {

	private static final Logger mLog = LoggerFactory.getLogger(PanoptoController.class);

	@Resource(name = "GradeColumnServiceImpl")
	private GradeColumnServiceImpl gradeService;

	@Resource(name = "contentServiceImpl")
	private ContentItemServiceImpl contentService;

	
	@Resource(name = "QueryExecutionStringServiceImpl")
	private QueryExecutionService<String> qesString;
	
	@Resource(name="QueryUpdateService")
	private QueryUpdateService qus;
	
	// panopto-folders page
	@RequestMapping(value = "/panopto-folders", method = RequestMethod.GET)
	public String getFolders(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model)
			throws Exception {

		mLog.debug("call ::: getFolders");

		Context ctx = ContextManagerFactory.getInstance().getContext();
				
		// init PanoptoData class 
		PanoptoData ccCourse = new PanoptoData(ctx);			
		Folder folders[] = ccCourse.getFolders();	
		
		mLog.debug("============================================================================================================");
		mLog.debug("folders[] : " + folders.length);		
		mLog.debug("folders[] : " + param.get("course_id").toString());		
		mLog.debug("============================================================================================================");
		 				
		model.addAttribute("folders", folders);
		model.addAttribute("course_id", param.get("course_id").toString());
	
		return "panopto-folder";
	}
	
	// panopto-sessions page
	@RequestMapping(value = "/panopto-sessionsById", method = RequestMethod.GET)
	public String getSessionsById(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model)
			throws Exception {

		mLog.debug("call ::: getSessionsById");

		Context ctx = ContextManagerFactory.getInstance().getContext();
		
		PanoptoData ccCourse = new PanoptoData(ctx);			
			
		String folderId = param.get("folderId").toString();
		
		Session sessions[] = ccCourse.getSessions(folderId);
		
		mLog.debug("============================================================================================================");
		mLog.debug("sessions : " + sessions.length);
		mLog.debug("============================================================================================================");
				
		model.addAttribute("sessions", sessions);
		model.addAttribute("servername", ccCourse.getServerName());
		
		return "panopto-session";
	}
	
	
	// import Panopto session (video content) 
	@RequestMapping(value = "/panopto-import", method = RequestMethod.POST)
	public String importPanoptoContent(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model) throws Exception {

		mLog.debug("importPanoptoContent param ::: " +param);

		Context ctx = ContextManagerFactory.getInstance().getContext();		
		param.put("course_id", ctx.getCourse().getBatchUid());
		param.put("course_pk1", CommonUtil.getInstance().getPk(ctx.getCourse().getId().getExternalString()));	
		
		qus.executeUpdate("content/insertContent", param); 		
		
		return "redirect:imported-contents?course_id=" +ctx.getCourseId().getExternalString()+ "&menuNo=" +param.get("menuNo")+ "&weekNo=" +param.get("folder_pk1");
	}
	
	// get Panopto userId 
	@RequestMapping(value = "/panopto-sync-userId", method = RequestMethod.POST)
	public void syncPanoptoUserId(@RequestParam Map<String, Object> param, 
											HttpServletRequest request, HttpServletResponse response, 
											Model model) throws Exception {

		mLog.debug("syncPanoptoUserId param ::: " +param);

		Context ctx = ContextManagerFactory.getInstance().getContext();		
		PanoptoData soap = new PanoptoData("userManagement");
		JSONObject json = new JSONObject();
		
		String provider = CommonUtil.getInstance().getValue("config/props/config.properties", "provider");
		com.panopto.services.User user = soap.getUserBykey(provider +"\\"+ ctx.getUser().getBatchUid());
		
		if(null != user && !user.getUserId().equals("00000000-0000-0000-0000-000000000000")) {
			mLog.debug("syncPanoptoUserId :: " + user.toString());
			mLog.debug("syncPanoptoUserId :: " + user.getUserId());
			
			// Save Panopto default user info  
//			param.put("contentId", CommonUtil.getInstance().getPk(ctx.getContent().getId().toExternalString()));
			param.put("userId", ctx.getUser().getBatchUid());
			param.put("panoptoUserId", user.getUserId());
			param.put("userPk1", CommonUtil.getInstance().getPk(ctx.getUser().getId().getExternalString()));
			
			qus.executeUpdate("panopto/registerPanoptoUserId", param);
			json.put("result", true);
		}else {
			json.put("result", false);
		}
		

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}
	
	
	// import Panopto session view data (video content) 
//	@RequestMapping(value = "/panopto-views", method = RequestMethod.GET)
//	public void importPanoptoSessionViews(@RequestParam Map<String, Object> param, 
//										HttpServletRequest request, HttpServletResponse response, 
//										Model model) throws Exception {
//
//		mLog.debug("importPanoptoSessionView param ::: " +param);
//
//		Context ctx = ContextManagerFactory.getInstance().getContext();		
//		PanoptoData soap = new PanoptoData(ctx);			
//		
//		DetailedUsageResponseItem res[] = 
//				soap.getSessionUserDetailedUsage("c5024173-1d15-4ae5-bce8-e4116ae83978", "381017ee-9741-4903-9109-ffb95066a8db");
//		
//		mLog.debug("getSessionUserDetailedUsage size :: " + res.length);
//		
//		JSONObject json = new JSONObject();
//		json.put("result", true);
//
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		out.print(json.toString());
//	}
	
}
