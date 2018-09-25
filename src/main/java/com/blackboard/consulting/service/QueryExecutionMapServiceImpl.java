package com.blackboard.consulting.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blackboard.consulting.dao.QueryExecutionDao;

/**
 * DB Select Map Service
 * 
 * @author June
 * @since  2016.02.18
 */
@Service("QueryExecutionMapServiceImpl")
public class QueryExecutionMapServiceImpl extends QueryFactoryService implements QueryExecutionService<Map<String,Object>>{
	

	@Resource(name="QueryExecutionMapDaoImpl")
	private QueryExecutionDao<Map<String, Object>> queryExecutionDao;

	public QueryExecutionMapServiceImpl() {		
	}
	
	public QueryExecutionMapServiceImpl(QueryExecutionDao<Map<String, Object>> queryExecutionDao) {
		super();
		this.queryExecutionDao = queryExecutionDao;
	}
	
	/**
	 * @param sql
	 * @param param
	 * @return Map<String,Object>
	 */
	@Override
	public Map<String,Object> executeQuery(String sql, Map<String, Object> param) {
		
		//generate SQL and where condition
		sql = getSql(sql);
        if(!param.isEmpty()) sql = getWhereCondition(sql, param);

        return queryExecutionDao.executeQuery(sql);
	}

	/**
	 * @param sql
	 * @param key
	 * @param value
	 * @return Map<String,Object>
	 */
	@Override
	public Map<String,Object> executeQuery(String sql, String key, String value) {
		
		//generate SQL and where condition
		sql = getSql(sql);
		if(key != null) sql = getWhereCondition(sql, key, value);

        return queryExecutionDao.executeQuery(sql);
	}
}
