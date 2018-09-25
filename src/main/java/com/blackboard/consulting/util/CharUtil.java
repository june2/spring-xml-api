package com.blackboard.consulting.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CharUtil {

	private Logger mLog = LoggerFactory.getLogger(CommonUtil.class);
	

	public static final int[] adminList = {2, 3}; // 2=faculty, 3=staf
	
	public CharUtil() {
		// prevent instantiation
	}

	/**
	 * @param input
	 * @return
	 */
	/**
	 * 한글을 한글 UniCode 로 변환 시키는 소스
	 * @throws UnsupportedEncodingException 
	 **/

	public String CharToUnicode(String param) {
		
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(param.length() * 6);
		for (i = 0; i < param.length(); i++) {
			j = param.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		mLog.debug("escape :"+tmp.toString());
		return tmp.toString();
	}

	
	/**
	 * 한글 UniCode 를 한글로 변환 시키는 소스
	 * @throws UnsupportedEncodingException 
	 **/

	public String UnicodeToChar(String param) {
		
		
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(param.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < param.length()) {
			pos = param.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (param.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(param
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(param
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(param.substring(lastPos));
					lastPos = param.length();
				} else {
					tmp.append(param.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		

		mLog.debug("escape :"+tmp.toString());
		return tmp.toString();
	}

}
