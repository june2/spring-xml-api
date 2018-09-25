package com.blackboard.consulting.dao;

/**
 * DB Select DAO Interface 
 * 
 * @author June
 * @since  2016.02.18
 * @param <E>
 */
public interface QueryExecutionDao<E> {
	
	/**
	 * @param sql
	 */
	public E executeQuery(String sql);
}
