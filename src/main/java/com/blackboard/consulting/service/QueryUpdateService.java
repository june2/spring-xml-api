package com.blackboard.consulting.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blackboard.consulting.dao.QueryUpdateDao;

/**
 * DB Insert, Update, Delete Service
 * 
 * @author June
 * @since  2016.02.18
 */
@Service("QueryUpdateService")
public class QueryUpdateService extends QueryFactoryService {
	private static final Logger mLog = LoggerFactory.getLogger(QueryUpdateService.class);
	
	@Resource(name="QueryUpdateDao")
	QueryUpdateDao queryUpdateDao;
	
	public QueryUpdateService() {
	}
	
	public QueryUpdateService(QueryUpdateDao queryUpdateDao) {
		super();
		this.queryUpdateDao = queryUpdateDao;
	}

	/**
	 * @param sql
	 * @param param
	 * @return boolean
	 */
	public int executeUpdate(String sql, Map<String, Object> param) {
		
		//generate SQL and where condition
		sql = getSql(sql);
		mLog.debug(" sql :: "+sql);
		mLog.debug(" param :: "+param);
	    
		if(!param.isEmpty()) sql = getWhereCondition(sql, param);
		mLog.debug(" sql :: "+sql);
        return queryUpdateDao.executeUpdate(sql);
	}

	/**
	 * @param sql
	 * @param key
	 * @param value
	 * @return boolean
	 */
	public int executeUpdate(String sql, String key, String value) {
		
		//generate SQL and where condition
		sql = getSql(sql);
		if(key != null) sql = getWhereCondition(sql, key, value);
		mLog.debug(" sql :: "+sql);
		
		return queryUpdateDao.executeUpdate(sql);
	}
}
