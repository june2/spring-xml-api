package com.blackboard.consulting.service;

import java.util.Map;

/**
 * DB Select Service Interface
 * 
 * @author June
 * @since  2016.02.18
 * @param <E>
 */
public interface QueryExecutionService<E> {
	
	/**
	 * @param sql
	 * @param param
	 */
	public E executeQuery(String sql, Map<String, Object> param);
	
	/**
	 * @param sql
	 * @param key
	 * @param value
	 */
	public E executeQuery(String sql, String key, String value);
}
