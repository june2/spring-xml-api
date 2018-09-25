package com.blackboard.consulting.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {
	
	private Logger mLog = LoggerFactory.getLogger(CommonUtil.class);
	
	public static final String pluginHandle = "ppto-panopto-online-attendance-BBLEARN";
	final private URL location = CommonUtil.class.getProtectionDomain().getCodeSource().getLocation();
	
	// prevent instantiation
	private CommonUtil() {}

	private static class SingletonHolder{
		static final CommonUtil util = new CommonUtil();
	}
	
	public static CommonUtil getInstance(){
		return SingletonHolder.util;
	}
	
	/**
	 * @param input
	 * @return
	 */
	public  String getPk(String input) {

		if (input.charAt(0) == '_') {
			return Pattern.compile("[_]+").split(input)[1];
		} else {
			throw new IllegalStateException("invalid input");
		}
	}
    
    /**
	 * get value from properties file 
	 * 
	 * @author June
	 * @param  root, key
	 * @return value
	 */
	public String getValue(String path, String key) {

		String result = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		Properties prop = new Properties();
		String root = location.getPath() + "/../";

		try {
			fis = new FileInputStream(root + path);
			bis = new BufferedInputStream(fis);
			prop.load(bis);
			result = prop.getProperty(key);
		} catch (IOException ex) {
			mLog.error("CommonUtil getValue error :: " + ex);
		}finally {
			if(bis != null) try {bis.close();} catch (IOException e) {mLog.error("CommonUtil getValue error :: " + e);}
			if(fis != null) try {fis.close();} catch (IOException e) {mLog.error("CommonUtil getValue error :: " + e);}
		}

		return result;
	}
	
	/**
	 * get value from properties file 
	 * 
	 * @author June
	 * @param  root
	 * @return Map
	 */
	public Map<String, Object> getValues(String path) {

		Map<String, Object> map = new HashMap<String, Object>(); 
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		Properties prop = new Properties();
		String root = location.getPath() + "/../";

		mLog.debug("CommonUtil getValue :: "+ root + path);
		
		try {
			fis = new FileInputStream(root + path);
			bis = new BufferedInputStream(fis);
			prop.load(bis);
			
			Iterator<Object> keys = prop.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next().toString();
				if(!prop.getProperty(key).isEmpty()){
					map.put(key, prop.getProperty(key));
				}
			}
		} catch (IOException ex) {
			mLog.error("CommonUtil getValue error :: " + ex);
		}finally {
			if(bis != null) try {bis.close();} catch (IOException e) {mLog.error("CommonUtil getValue error :: " + e);}
			if(fis != null) try {fis.close();} catch (IOException e) {mLog.error("CommonUtil getValue error :: " + e);}
		}

		return map;
	}
	
	/**
	 * write properties file
	 * 
	 * @author June
	 * @param path, name, param
	 * @return value
	 */
	public void writeProperties(String name, Map<String, String> param) {
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			String root = location.getPath() + "/../";
			output = new FileOutputStream(root + name);

			if(param != null){
				// set the properties value
				Iterator<String> keys = param.keySet().iterator();
				while( keys.hasNext() )
				{
					String key = keys.next();
					mLog.debug("writeProperties - ::: " + key + " : " + param.get(key));
					prop.setProperty(key, param.get(key));
				}
				
				// save properties to project root folder
				prop.store(output, null);
			}

		} catch (IOException e) {
			mLog.error("writeProperties - IOException ::: " + e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					mLog.error("writeProperties - IOException ::: " + e);
				}
			}

		}
	}
	
	/**
	* 쿼리에서 조회된 수를 시간: 분: 초로 표시함
	* @param getSecond 초
	* @return 시간 : 분 : 초
	*/
	public String getTime(String getSecond)
	{
		int tsecond = 0;
	
		if(getSecond.equals("") || getSecond.equals("-"))
		{
			return "-";
		}
		else
		{
			if(getSecond.indexOf(".") > 0)
				getSecond = getSecond.substring(0,getSecond.indexOf("."));
	
			tsecond = Integer.parseInt(getSecond);
		}
		
		int minute = 60;
		int hour = 60 * 60;
		int day = 60 * 60 * 24;
		int second;
		int sday, shour, sminute = 0;
	
		String retStr= "";
	
		sday = tsecond/day;
	
		if(sday != 0)
			shour = (tsecond % day) / hour;
		else
			shour = tsecond / hour;
	
		if(shour != 0)
			sminute = (tsecond % hour) / minute;
		else
			sminute = (tsecond % day) / minute;
	
		if(sminute != 0)
			second = (tsecond % minute) % 60;
		else
			second = (tsecond % hour) % 60;
	
		
		retStr += Integer.toString(sday) + "/" + Integer.toString(shour) + "/" + Integer.toString(sminute) + "/" + Integer.toString(second) + "/";
		
		return retStr;
	}
	

	public String getTimeString(String getSecond)
	{
		int tsecond = 0;
	
		if(getSecond.equals("") || getSecond.equals("-"))
		{
			return "-";
		}
		else
		{
			if(getSecond.indexOf(".") > 0)
				getSecond = getSecond.substring(0,getSecond.indexOf("."));
	
			tsecond = Integer.parseInt(getSecond);
		}
		
		int minute = 60;
		int hour = 60 * 60;
		int day = 60 * 60 * 24;
		int sday, shour, sminute, second = 0;
	
		String retStr= "";
	
		sday = tsecond/day;
	
		if(sday != 0)
			shour = (tsecond % day) / hour;
		else
			shour = tsecond / hour;
		
		if(shour != 0)
			sminute = (tsecond % hour) / minute;
		else
			sminute = (tsecond % day) / minute;
			
		if(sminute != 0)
			second = (tsecond % minute) % 60;
		else
			second = (tsecond % hour) % 60;
	
		if(sday != 0){
			retStr += Integer.toString(sday) + "일";
		}
		if(shour != 0){
			retStr += Integer.toString(shour) + "시";
		}
		if(sminute !=0 ){
			retStr += Integer.toString(sminute) + "분";	
		}
		if(second != 0 ){
			retStr += Integer.toString(second) + "초";
		}
		
		
		return retStr;
	}
	
	
	
	/** 
	 * convertToDate
	 * @param datetime
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss") HH 대문자 : 24 Hour
	 * 소문자 : 12 Hour 
	**/
	public String convertToDate(String inputDate){
		String dateTime = inputDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Date parseDate = null;
		String convertedDate = null;

		try {
			parseDate = format.parse(dateTime);
			DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			convertedDate = dateFormatNeeded.format(parseDate);
		} catch (ParseException e) {
			mLog.error("convertToDate error :: " + e);	
		}

		return convertedDate;
	}

	
	/**
	 * UTC time to local time
	 * @param datetime
	**/
	public String utcToLocaltime(String datetime){
		
		String locTime = null;
		
		// TimeZone tz = TimeZone.getDefault();
		TimeZone tz = TimeZone.getTimeZone("GMT+09:00"); // 해당 국가 일시 확인 할 때, 한국은 +9
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date parseDate = sdf.parse(datetime);
			long milliseconds = parseDate.getTime();
			int offset = tz.getOffset(milliseconds);
			locTime = sdf.format(milliseconds + offset);
			locTime = locTime.replace("+0000", "");
		} catch (Exception e) {
			mLog.error("utcToLocaltime error :: " + e);
		}
		return locTime;
	}
	
	
	/**
	 * Convert second to time
	 * @param secondTime
	**/
	public long secondToTime(String secondTime) {
		
		secondTime = String.format("%d", Math.round(Double.parseDouble(secondTime)));
		
		Duration duration = Duration.ofSeconds(Integer.parseInt(secondTime));
		
		long hours = duration.toHours();
		long minutes = ((duration.getSeconds() % (60 * 60)) / 60);
//		long seconds = (duration.getSeconds() % 60);
		
//		String min = Long.toString(minutes);
////		String sec = Long.toString(seconds);
//		String time = "";
		
		
//		if(minutes<10) {
//			min = 0 + min;
//		}
		
//		if(seconds<10) {
//			sec = 0 + sec;
//		}
		
		return ((hours * 60) + minutes);
	}
}
