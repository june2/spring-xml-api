package com.blackboard.consulting.web;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.util.CommonUtil;

import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

/**
 * Excel Download Controller
 * 
 * @author June
 * @since 2016.01.19
 */
@Controller
public class ExcelController {
	
	private static final Logger mLog = LoggerFactory.getLogger(ExcelController.class);
	
	@Resource(name="QueryExecutionListServiceImpl")
	private QueryExecutionService<List<Map<String, Object>>> qesList;
	
	@Resource(name = "QueryExecutionMapServiceImpl")
	private QueryExecutionService<Map<String, Object>> qesMap;
	
	
	/**
	 * @param param (column names, excel title)
	 * @return excel
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel/view", method = RequestMethod.GET)
	public String excelView(@RequestParam Map<String, Object> param, Model model) {
		
		try {
			mLog.debug("excelView ::: " + param);
			
			Context ctx = ContextManagerFactory.getInstance().getContext();		
			
			param.put("course_pk1", CommonUtil.getInstance().getPk(param.get("course_id").toString()));
			
			List<Map<String, Object>> resultList = qesList.executeQuery("stats/selectStatsView", param);
			Map<String, Object> resultMap = qesMap.executeQuery("stats/selectTargetCourse", param);
			String[] columnList = param.get("column").toString().split(",");
			
			mLog.debug("excelView resultList  ::: " + resultList);
			mLog.debug("excelView resultMap  ::: " + resultMap);
			
			int cutoff = Integer.parseInt(CommonUtil.getInstance().getValue("config/props/config.properties", "cutoff"));
			double percentage = 0;
			String pass = "0";
			
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = new TreeMap<String, Object>();
				
				if(null != resultList.get(i).get("pass")) {					
					percentage = (Integer.parseInt(resultList.get(i).get("pass").toString()) / Double.parseDouble(resultMap.get("total").toString())) * 100;
					pass = resultList.get(i).get("pass").toString();
				}

				map.put("1.userId", resultList.get(i).get("user_id"));
				map.put("2.lastName", resultList.get(i).get("lastname"));
				map.put("3.fristName", resultList.get(i).get("firstname"));
				map.put("4.overall", percentage);
				map.put("5.pass", pass +" / "+ resultMap.get("total"));
				if(percentage >= cutoff) {
					map.put("6.status", "P");
				}else {
					map.put("6.status", "F");
				}
				resultList.set(i, map);
			}
			
			model.addAttribute("columnList", columnList);
			model.addAttribute("resultList", resultList);
			model.addAttribute("title", ctx.getCourse().getBatchUid());
			
			mLog.debug("excelView resultList  ::: " + resultList);
			
		} catch (Exception e) {
			mLog.error("excelView error ::: " + e);
		}
		
		return "excel";
	}
	
	
	/**
	 * @param param (column names, excel title)
	 * @return excel
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel/view-user", method = RequestMethod.GET)
	public String excelViewUser(@RequestParam Map<String, Object> param, Model model) {
		
		try {
			Context ctx = ContextManagerFactory.getInstance().getContext();				
			
			CommonUtil util = CommonUtil.getInstance();
			param.put("course_pk1", util.getPk(param.get("course_id").toString()));
			List<Map<String, Object>> resultList = qesList.executeQuery("stats/selectStatsViewUser", param);
			String[] columnList = param.get("column").toString().split(",");
			DecimalFormat format = new DecimalFormat(".##");
			
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = new TreeMap<String, Object>();
				map.put("1.userId", param.get("user_id"));
				map.put("2.title", resultList.get(i).get("title"));
				
				if(null == resultList.get(i).get("cumulative_time")) {
					map.put("3.watching", 0);
				}else {
					map.put("3.watching", util.secondToTime(resultList.get(i).get("cumulative_time").toString()));
				}
				
				map.put("4.minimum", util.secondToTime(resultList.get(i).get("minimumtime").toString()));
				map.put("5.duration", util.secondToTime(resultList.get(i).get("duration").toString()));
				
				if(null == resultList.get(i).get("percentage")) {
					map.put("6.per", 0);
				}else {
					map.put("6.per", format.format(Double.parseDouble(resultList.get(i).get("percentage").toString())));
				}
				
				if(null != resultList.get(i).get("pass") && resultList.get(i).get("pass").toString().equals("1")) {
					map.put("7.status", "P");
				}else {
					map.put("7.status", "F");
				}
				
				resultList.set(i, map);
			}
			
			model.addAttribute("columnList", columnList);
			model.addAttribute("resultList", resultList);
			model.addAttribute("title", ctx.getCourse().getBatchUid() +"_"+param.get("user_id"));
		
			mLog.debug("excelViewUser resultList ::: " + resultList);
		
		} catch (Exception e) {
			mLog.error("excelViewUser error ::: " + e);
		}
		
		return "excel";
	}
	
	
	/**
	 * @param param (column names, excel title)
	 * @return excel
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel/view-user-detail", method = RequestMethod.GET)
	public String excelViewUserDetail(@RequestParam Map<String, Object> param, Model model) {
		
		try {
			mLog.debug("excelViewUserDetail ::: " + param);
			
			Context ctx = ContextManagerFactory.getInstance().getContext();	
	
			List<Map<String, Object>> resultList = qesList.executeQuery("stats/selectStatsViewUserDetail", param);
			String[] columnList = param.get("column").toString().split(",");
			
			model.addAttribute("columnList", columnList);
			model.addAttribute("resultList", resultList);
			model.addAttribute("title", ctx.getCourse().getBatchUid() +"_"+ resultList.get(0).get("title").toString() +"_"+ param.get("user_id"));
			
			mLog.debug("excelViewUserDetail resultList ::: " + resultList);
			
		} catch (Exception e) {
			mLog.error("excelView error ::: " + e);
		}
		
		return "excel";
	}
}
