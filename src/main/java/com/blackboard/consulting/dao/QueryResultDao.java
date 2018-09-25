package com.blackboard.consulting.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DB Select DAO
 * 
 * @author June
 * @since  2016.02.18
 */
class QueryResultDao {
	
	protected static final Logger mLog = LoggerFactory.getLogger(QueryResultDao.class);

//	protected Connection con = null;
//	protected Statement stmt = null;
//	protected ResultSet rs = null;
	
    /**
	 * @param  ResultSet
	 * @return List<Map<String, Object>>
     * @throws SQLException 
	 */
	protected List<Map<String, Object>> getResultSet(ResultSet rs, List<Map<String, Object>> list) throws SQLException {
		
		ResultSetMetaData metadata = rs.getMetaData();
        int count = metadata.getColumnCount();
		
		while (rs.next()) 
        {
        	//Map<String, Object> tempMap = new HashMap<String, Object>();
        	Map<String, Object> tempMap = new TreeMap<String, Object>();
        	
        	for (int i = 1; i <= count; i++) {
        		mLog.trace("data type " + i + " ::: " +metadata.getColumnName(i) +" - "+ rs.getString(i));
        		tempMap.put(metadata.getColumnName(i).toLowerCase(), rs.getString(i));
			}
        	list.add(tempMap);
		}
		
		return list;
	}
	
	/**
	 * @param  ResultSet
	 * @return Map<String, Object>
     * @throws SQLException 
	 */
	protected Map<String, Object> getResultSet(ResultSet rs, Map<String, Object> map) throws SQLException {
		
		ResultSetMetaData metadata = rs.getMetaData();
        int count = metadata.getColumnCount();
        
		while (rs.next()) 
        {
        	for (int i = 1; i <= count; i++) {
        		map.put(metadata.getColumnName(i).toLowerCase(), rs.getString(i));
			}
		}
		
		return map;
	}
	
	/**
	 * @param  ResultSet
	 * @return String
     * @throws SQLException 
	 */
	protected String getResultSet(ResultSet rs, String str) throws SQLException {
		
		while (rs.next()) 
        {
    		str = rs.getString(1);
		}
		
		return str;
	}
}
