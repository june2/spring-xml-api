package com.blackboard.consulting.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blackboard.consulting.dao.QueryExecutionDao;

/**
 * DB Select List Service 
 * 
 * @author June
 * @since  2016.02.18
 */
@Service("QueryExecutionListServiceImpl")
public class QueryExecutionListServiceImpl extends QueryFactoryService implements QueryExecutionService<List<Map<String, Object>>>{

	@Resource(name="QueryExecutionListDaoImpl")
	private QueryExecutionDao<List<Map<String, Object>>> queryExecutionDao;
	
	public QueryExecutionListServiceImpl() {		
	}
	
	public QueryExecutionListServiceImpl(QueryExecutionDao<List<Map<String, Object>>> queryExecutionDao) {
		super();
		this.queryExecutionDao = queryExecutionDao;
	}

	/**
	 * @param sql
	 * @param param
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> param) {
		
		//generate SQL and where condition
		sql = getSql(sql);
        if(null != param && !param.isEmpty()) sql = getWhereCondition(sql, param);

        return queryExecutionDao.executeQuery(sql);
	}

	/**
	 * @param sql
	 * @param key
	 * @param value
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> executeQuery(String sql, String key, String value) {
		
		//generate SQL and where condition
		sql = getSql(sql);
		if(key != null) sql = getWhereCondition(sql, key, value);

        return queryExecutionDao.executeQuery(sql);
	}

}
