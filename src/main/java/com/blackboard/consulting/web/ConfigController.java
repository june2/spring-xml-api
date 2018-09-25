package com.blackboard.consulting.web;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.service.QueryUpdateService;
import com.blackboard.consulting.util.CommonUtil;
import com.blackboard.consulting.util.ScheduledJob;

import net.sf.json.JSONObject;

/**
 * @author June
 * @since 2016.01.19
 */
@Controller
public class ConfigController {

	private static final Logger mLog = LoggerFactory.getLogger(ConfigController.class);
	
	@Resource(name="QueryExecutionListServiceImpl")
	private QueryExecutionService<List<Map<String, Object>>> qesList; 
	
	@Resource(name="QueryUpdateService")
	private QueryUpdateService qus;
	
	private Timer jobScheduler;
	private ScheduledJob scheduledJob; 

	@RequestMapping(value = "/admin/config", method = RequestMethod.GET)
	public String configPage(Model model) throws Exception {

		mLog.debug("configPage param");

		Map<String, Object> resultMap = CommonUtil.getInstance().getValues("config/props/config.properties");
		Map<String, Object> schedulerMap = CommonUtil.getInstance().getValues("config/props/scheduler.properties");
		
		boolean isRunning = false;
		if(scheduledJob != null) {
			isRunning = scheduledJob.isRunning();
		}
		
		model.addAttribute("configMap", resultMap);
		model.addAttribute("schedulerMap", schedulerMap);
		model.addAttribute("isRunning", isRunning);

		return "config";
	}

	@RequestMapping(value = "/admin/config", method = RequestMethod.POST)
	public String saveConfig(@RequestParam Map<String, String> param, Model model) throws Exception {

		mLog.debug("saveConfig param ::: " + param);

		// Save values in configuration file
		CommonUtil.getInstance().writeProperties("config/props/config.properties", param);

		return "redirect:../admin/config";
	}

	@RequestMapping(value = "/admin/configWeek", method = RequestMethod.GET)
	public String configWeekPage(@RequestParam Map<String, Object> param, Model model) throws Exception {

		mLog.debug("configWeekPage ::: " + param);

		// Default Select Term value
		String term = "1";

		if (null != param.get("term")) {
			term = param.get("term").toString();
		}

		List<Map<String, Object>> resultList = qesList.executeQuery("common/selectConfigWeek", "term", term);
		List<Map<String, Object>> termList = qesList.executeQuery("common/selectConfigTerm", param);

		model.addAttribute("resultList", resultList);
		model.addAttribute("termList", termList);
		model.addAttribute("termPk", term);

		return "config-week";
	}

	@RequestMapping(value = "/admin/configWeek", method = RequestMethod.POST)
	public void saveConfigWeek(@RequestBody List<Map<String, Object>> list, @RequestParam Map<String, Object> param,
			Model model, HttpServletResponse response) throws Exception {

		mLog.debug("saveConfigWeek ::: " + list);
		JSONObject json = new JSONObject();

		try {
			// Delete all week data
			qus.executeUpdate("common/deleteConfigWeek", param);

			for (Map<String, Object> map : list) {
				int result = qus.executeUpdate("common/insertConfigWeek", map);
				mLog.debug("result : " + result);
			}

			json.put("res", true);
		} catch (Exception e) {
			json.put("res", false);
		}

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}

	@RequestMapping(value = "/admin/configWeek", method = RequestMethod.DELETE)
	public void deleteConfigWeekByPk(@RequestBody String pk1, @RequestParam Map<String, Object> param, Model model,
			HttpServletResponse response) throws Exception {

		mLog.debug("deleteConfigWeekByPk ::: " + pk1);
		JSONObject json = new JSONObject();

		try {
			//// Delete week by Pk1
			qus.executeUpdate("common/deleteConfigWeekByPk", "pk1", pk1);
			json.put("res", true);
		} catch (Exception e) {
			json.put("res", false);
		}

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}
	
	@RequestMapping(value = "/admin/initScheduler", method = RequestMethod.POST)
	public String initScheduler(@RequestParam Map<String, String> param, Model model) throws Exception {

		mLog.debug("initScheduler ::: " + param);
		
		// Save values in scheduler configuration file
		Map<String, String> schedulerMap = new HashMap<String, String>();
		Date now = new Date();
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		schedulerMap.put("initTime", transFormat.format(now).replace(" ","T"));
		CommonUtil.getInstance().writeProperties("config/props/scheduler.properties", schedulerMap);
		
		long period = Integer.parseInt(param.get("syncPrd").toString()) * 60000;		
		
		jobScheduler = new Timer();
		scheduledJob = new ScheduledJob(true);
		jobScheduler.scheduleAtFixedRate(scheduledJob, 0, period);
		
		// Save values in configuration file
		CommonUtil.getInstance().writeProperties("config/props/config.properties", param);

		return "redirect:../admin/config";
	}
	
	@RequestMapping(value = "/admin/stopScheduler", method = RequestMethod.POST)
	public String stopScheduler(@RequestParam Map<String, String> param, Model model) throws Exception {

		mLog.debug("stopScheduler ::: " + param);

		jobScheduler.cancel();
		scheduledJob.setIsRunning(false);

		return "redirect:../admin/config";
	}

}
