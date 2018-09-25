package com.blackboard.consulting.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackboard.consulting.dao.QueryExecutionListDaoImpl;
import com.blackboard.consulting.dao.QueryExecutionMapDaoImpl;
import com.blackboard.consulting.dao.QueryUpdateDao;
import com.blackboard.consulting.service.GradeColumnServiceImpl;
import com.blackboard.consulting.service.QueryExecutionListServiceImpl;
import com.blackboard.consulting.service.QueryExecutionMapServiceImpl;
import com.blackboard.consulting.service.QueryUpdateService;
import com.panopto.blackboard.PanoptoData;
import com.panopto.services.DetailedUsageResponseItem;

import blackboard.db.ConnectionManager;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.user.UserDbLoader;

public class ScheduledJob extends TimerTask {

	private static final Logger mLog = LoggerFactory.getLogger("scheduleJob");
	
	private QueryExecutionListServiceImpl qesList;
	private QueryExecutionMapServiceImpl qesMap;
	private QueryUpdateService qus;
	private PanoptoData soap;
	private GradeColumnServiceImpl gradeService;
	private Map<String, String> param;
	private Map<String, Object> queryParam;
	private SimpleDateFormat transFormat;
	private CommonUtil util; 
	
	private String[] id;
	
	private boolean isRunning = false;
	private String initTime;
	private String lastTime;	
	private String dbType;
	
	public ScheduledJob(boolean isRunning) {
		this.isRunning = isRunning;
				
		soap = new PanoptoData("usageReporting");
		param = new HashMap<String, String>();
		queryParam = new HashMap<String, Object>();
		transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		gradeService = new GradeColumnServiceImpl();
		util = CommonUtil.getInstance();		
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	// get query list result
	public List<Map<String, Object>> getQueryResultList(String sql, Map<String, Object> param) {
		qesList = new QueryExecutionListServiceImpl(new QueryExecutionListDaoImpl());
		return qesList.executeQuery(sql, param);
	}
	
	//get query string result
	public Map<String, Object> getQueryResultMap(String sql, Map<String, Object> param) {
		qesMap = new QueryExecutionMapServiceImpl(new QueryExecutionMapDaoImpl());
		return qesMap.executeQuery(sql, param);
	}

	// excute query
	public void executeQuery(String sql, Map<String, Object> param) {
		qus = new QueryUpdateService(new QueryUpdateDao());
		qus.executeUpdate(sql, param);
	}
	
	// get viewing data though the SOAP API
	public DetailedUsageResponseItem[] callAPI(String sessionId, String start, String end) {
		return soap.getSessionDetailedUsage(sessionId, start, end);
	}

	// get now date
	public String getNowDateTime() {
		Date now = new Date();
//		transFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return transFormat.format(now).replace(" ","T");
	}
	
	// get UTC time 
	public String localTimeToUtc(String datetime) {
		String utcTime = null;
		TimeZone tz = TimeZone.getTimeZone("GMT-09:00"); // 해당 국가 일시 확인 할 때, 한국은 +9
		// TimeZone tz = TimeZone.getDefault();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date parseDate = sdf.parse(datetime);
			long milliseconds = parseDate.getTime();
			int offset = tz.getOffset(milliseconds);
			utcTime = sdf.format(milliseconds + offset);
			utcTime = utcTime.replace("+0000", "");
		} catch (Exception e) {
			mLog.error("localTimeToUtc ::: " + e);
		}

		return utcTime.replace(" ","T");
	}
	
	// Converting watching time to binary 
	public static String[] convertTimeToBinary(String watchingStart, String watchingEnd) {
		
		int start = Integer.parseInt(watchingStart);
		int end = Integer.parseInt(watchingEnd);
		
		if(start == 0) start = 1;
		
		int divide = 30;
		int size = (end/divide);
		int rest = (end%divide);
		int position = 0;
		
		String temp = "";
		String flag = "0"; 
		
		if(rest > 0) {
			size = size + 1;
		} 
		
		String arr[] =  new String[size];

		for (int j = 1; j <= end; j++) {
			if(j == start) {
				flag = "1";
			}
			
			temp = temp + flag;
			
			if(j%divide == 0) {
				arr[position] = temp;
				temp = "";
				position = position +1;
			}
			
			if(end%divide != 0 && j == end) {
				String zero = "";
				for (int i = 0; i < divide-(end%divide); i++) {
					zero = zero + "0"; 
				}
				arr[position] = temp + zero;
			}
		}
		
		return arr;
	}
	
	// Calculate time
	public String[] orBinary(String[] arr1, String[] arr2) {
		int size = 0;
		String a = "0";
		String b = "0";
		
		if(arr1.length > arr2.length) { 
			size = arr1.length;
		}else {
			size = arr2.length;
		}
		
		String arr[] =  new String[size];
		
		for (int i = 0; i < size; i++) {
			if(i >= arr1.length) {
				a = "0";
			}else {
				a = arr1[i];
			}
			
			if(i >= arr2.length) {
				b = "0";
			}else {
				b = arr2[i];
			}
			
			arr[i] = Integer.toBinaryString(Integer.parseInt(a, 2)|Integer.parseInt(b, 2));
		}
		
		return arr;
	}
	
	// insert bulk viewing data
	public Set<String> excuteBatchJob(DetailedUsageResponseItem[] items, Set<String> userIds, String pk1) {
		
		String sql = "";
		
		if(dbType.equals("oracle")) {
			sql = "insert into BBLEARN.ppto_online_users_watching "
				+ "(pk1, online_content_pk1, panopto_session_id, user_id, watching_date, watching_time, watching_start) "
				+ "values (BBLEARN.ppto_online_users_watching_seq.NEXTVAL, ?, ?, ?, to_date(?, 'yyyy-MM-dd HH24:MI:SS'), ?, ?)";
		}else {
			sql = "insert into BBLEARN.dbo.ppto_online_users_watching "
				+ "(online_content_pk1, panopto_session_id, user_id, watching_date, watching_time, watching_start) "
				+ "values (?, ?, ?, ?, ?, ?)";
		}
			
		Connection con = null; 
		PreparedStatement ps = null;
		
		try {
			con = ConnectionManager.getDefaultConnection();
			ps = con.prepareStatement(sql);
			
			final int batchSize = 1000;
			int count = 0;
			
			for (DetailedUsageResponseItem item: items) {
				
				userIds.add(item.getUserId());				
				
				ps.setString(1, pk1);
				ps.setString(2, item.getSessionId());
				ps.setString(3, item.getUserId());
				ps.setString(4, item.getTime());
				ps.setString(5, String.valueOf(item.getSecondsViewed()));
				ps.setString(6, String.valueOf(item.getStartPosition()));
				ps.addBatch();
				
				if(++count % batchSize == 0) {
					ps.executeBatch();
				}
			}
			
			ps.executeBatch(); // insert remaining records
		} catch (Exception e) {
			mLog.error("excuteBatchJob error ::: " + e);
		} finally {
	        	try{
	        		if (ps != null) ps.close();
			} 
			catch (Exception e){
				mLog.error("excuteBatchJob error2 ::: " + e);
			}
			ConnectionManager.releaseDefaultConnection(con);
			mLog.debug("excuteBatchJob connetion close ::: ");
		}
		
		return userIds;
	}
	
	// Get total viewing time
	public int getTotalView(List<Map<String, Object>> datas) {
		// total viewing time
		int total = 0;
		String sum[] =  {};
		
		for (int i = 0; i < datas.size(); i++) {
			mLog.debug("getTotalView - culculate ::: " + datas.get(i));
			
			if(i == 1 ) {
				sum = orBinary(
					convertTimeToBinary(datas.get(0).get("watching_start").toString(), datas.get(0).get("watching_end").toString()),
					convertTimeToBinary(datas.get(1).get("watching_start").toString(), datas.get(1).get("watching_end").toString()));
			}else{
				sum = orBinary(
						sum,
						convertTimeToBinary(datas.get(i).get("watching_start").toString(), datas.get(i).get("watching_end").toString()));
			}
		}
		
		for (int i = 0; i < sum.length; i++) {
			total = total + Integer.bitCount(Integer.parseInt(sum[i], 2));
		}
		return total;
	}
	
	// Upsert viewing data
	public Set<String> updateViewData(Set<String> userIds, Set<String> courseIds, String pk1, String sessionId, String courseId, Double duration, Double minTime) {
 
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sessionId", sessionId);
		param.put("pk1", pk1);
		
		for (String userId : userIds) {			
			param.put("userId", userId);
			
			//get BB ids by Panopto ids
			Map<String, Object> idMap = getQueryResultMap("panopto/getBbIdByPnaoptoId", param);
			mLog.debug("updateViewData idMap ::: " + idMap);
			
			if(!idMap.isEmpty()) {				
		 		//store panoptoUserId and userId and courseId
				courseIds.add(userId +" "+ idMap.get("user_id").toString() +" "+ courseId);
				
				// get history by userId and sessionId
				int total = getTotalView(getQueryResultList("panopto/selectWatchingById", param)); 
				double percentage = (total / duration) * 100;
				mLog.debug("updateViewData percentage ::: " + total +", "+ duration +", "+ minTime +", "+ percentage);
				
				if(total >= minTime) {
					param.put("pass", 1); 
				}else {
					param.put("pass", 0); 
				}
				
				param.put("total", total* 60); // min -> second
				param.put("percentage", percentage);
				param.put("courseId", courseId);
				
				// update score
				mLog.debug("updateViewData queryParam ::: " + param);
				executeQuery("panopto/upsertOnlineUsers", param);				
			}
		}
		
		return courseIds;
	}
	
	//update grade center	
	public void updateGradeCenter(Set<String> courseIds, Double cutoff) {
		
		String panoptoUserId;
		String userId;
		String courseId;
		double percentage;
		int sum; 
		
		//update grade center
		for (String val : courseIds) {
			
			id = val.split(" ");
			panoptoUserId = id[0];
			userId = id[1];
			courseId = id[2];
			
			queryParam.put("userId", panoptoUserId);
			queryParam.put("courseId", courseId);
			Map<String, Object> res = getQueryResultMap("panopto/getTotalTimeByUserId", queryParam);
			mLog.debug("updateGradeCenter start ::: " +res);
			
			sum = Integer.parseInt(res.get("sum").toString());
			if(sum > 0) {
				// get total percentage
				percentage = (sum / Double.parseDouble(res.get("total").toString())) * 100;
				mLog.debug("updateGradeCenter ::: " + courseId +","+userId+","+percentage+","+cutoff);
				
				try {
					gradeService.updateGrade(
							CourseDbLoader.Default.getInstance().loadByCourseId(courseId), 
							UserDbLoader.Default.getInstance().loadByBatchUid(userId).getId(),
							percentage,
							cutoff);
				} catch (KeyNotFoundException e) {
					mLog.error("updateGradeCenter KeyNotFoundException ::: " + e);
				} catch (PersistenceException e) {
					mLog.error("updateGradeCenter PersistenceException ::: " + e);
				}
			}
			
			mLog.debug("updateGradeCenter end ::::::::::::::::::::::::::: ");
		}
	}
	
	@Override
	public void run() {
		
		try {
			mLog.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" );
			mLog.info(":::::::::::::::::::::::::::::::::::::::::::::::: ScheduledJob init - " + new Date()+ " ::::::::::::::::::::::::::::::::::::" );
			mLog.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" );
	
			String pk1;
			String courseId;
			String sessionId;
			String start;
			String end;
			Double minTime;
			Double duration;
			Double cutoff = Double.parseDouble(util.getValue("config/props/config.properties", "cutoff"));
			Set<String> userIds = new HashSet<String>();
			Set<String> courseIds = new HashSet<String>();
			dbType = util.getValue("config/props/config.properties", "dbType");
			
			// Get initial timing from configuration
			if(null == initTime) {
				initTime = CommonUtil.getInstance().getValue("config/props/scheduler.properties", "initTime");
			}
			
			// Get current time					
			mLog.info("ScheduledJob initTime ::: " + initTime);
							
			List<Map<String, Object>> sessions = getQueryResultList("panopto/selectTargetSessions", null);
			
			if(null != sessions){				
				//Get all targeting sessions
				for (Map<String, Object> session : sessions) {
					
					// assert session data
					mLog.debug("ScheduledJob 1 ::: " + session);
					pk1 = session.get("pk1").toString();
					courseId = session.get("course_id").toString();
					sessionId = session.get("url").toString();
					start = session.get("start_date").toString();
					end = session.get("end_date").toString();			
					duration = (double) util.secondToTime(session.get("duration").toString());
					minTime = (double) util.secondToTime(session.get("minimumtime").toString());
					
					// Call soap API and get results 
					DetailedUsageResponseItem[]  res = callAPI(sessionId, localTimeToUtc(start), localTimeToUtc(end));
					
					// insert new data and update attendance data 
					if(res != null && res.length>0) {
						mLog.debug("ScheduledJob 2 - logs ::: " + res.length);
						
						// delete duplicate data
						queryParam.put("sessionId", sessionId);
						queryParam.put("pk1", pk1);
//						queryParam.put("start", start);  // *unused in delete query
//						queryParam.put("end", end);  // *unused in delete query
						mLog.debug("ScheduledJob 3 - delete watichg data ::: " + queryParam);
						executeQuery("panopto/deleteOnlineUsersWatching", queryParam);
						queryParam.clear();
						
						// Insert bulk data into BB table
						userIds = excuteBatchJob(res, userIds, pk1);
						
						// Calculate viewing time in each userId and sessionId 
						courseIds = updateViewData(userIds, courseIds, pk1, sessionId, courseId, duration, minTime);
						userIds.clear();
					}
					
				} // end for
				
				// update grade center by course and user
				updateGradeCenter(courseIds, cutoff);
				courseIds.clear();
				
			}else{
				mLog.debug("ScheduledJob empty ::: ");
			}
			
			// Get current time
			lastTime = getNowDateTime();
			mLog.info("ScheduledJob lastTime ::: " + lastTime);
			
			// Save result values in configuration file
			param.put("initTime", lastTime);
			param.put("lastTime", initTime);
			CommonUtil.getInstance().writeProperties("config/props/scheduler.properties", param);
			
			// Initialize values 
			initTime = lastTime;
			mLog.debug("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" );
		
		} catch (Exception e) {
			// Save error message
			param.put("lastTime", e.toString());
			CommonUtil.getInstance().writeProperties("config/props/scheduler.properties", param);
			mLog.error("schedulerJob error  ::: " + e);
		}
	}
	
}

