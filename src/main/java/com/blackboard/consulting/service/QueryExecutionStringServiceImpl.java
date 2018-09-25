package com.blackboard.consulting.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blackboard.consulting.dao.QueryExecutionDao;

/**
 * DB Select String Service
 * 
 * @author June
 * @since  2016.02.18
 */
@Service("QueryExecutionStringServiceImpl")
public class QueryExecutionStringServiceImpl extends QueryFactoryService implements QueryExecutionService<String>{

	@Resource(name="QueryExecutionStringDaoImpl")
	private QueryExecutionDao<String> queryExecutionDao;
	
	public QueryExecutionStringServiceImpl() {		
	}
	
	public QueryExecutionStringServiceImpl(QueryExecutionDao<String> queryExecutionDao) {
		super();
		this.queryExecutionDao = queryExecutionDao;
	}
	
	/**
	 * @param sql
	 * @param param
	 * @return String 
	 */
	@Override
	public String executeQuery(String sql, Map<String, Object> param) {
		
		//generate SQL and where condition
		sql = getSql(sql);
        if(!param.isEmpty()) sql = getWhereCondition(sql, param);

        return queryExecutionDao.executeQuery(sql);
	}

	/**
	 * @param sql
	 * @param key
	 * @param value
	 * @return String 
	 */
	@Override
	public String executeQuery(String sql, String key, String value) {
		
		//generate SQL and where condition
		sql = getSql(sql);
		if(key != null) sql = getWhereCondition(sql, key, value);

        return queryExecutionDao.executeQuery(sql);
	}
}
