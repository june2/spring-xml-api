 package com.blackboard.consulting.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import blackboard.db.ConnectionManager;
import blackboard.db.ConnectionNotAvailableException;

/**
 * DB Select Map DAO
 * 
 * @author June
 * @since  2016.02.18
 */
@Repository("QueryExecutionMapDaoImpl")
public class QueryExecutionMapDaoImpl extends QueryResultDao implements QueryExecutionDao<Map<String,Object>>
{
	Map<String,Object> result = null;
	
	/**
	 * @param sql
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String,Object> executeQuery(String sql) {
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = ConnectionManager.getDefaultConnection();
			mLog.debug("connetion open");
            
            stmt = con.createStatement();
            mLog.debug("Execute query ::: " + sql);
            
            rs = stmt.executeQuery(sql);
            
            //get Result 
            result = getResultSet(rs, new HashMap<String,Object>());
            
        } catch (ConnectionNotAvailableException ex) {
            mLog.error("connection err", ex);
        } catch (SQLSyntaxErrorException ex) {
	        mLog.error("SQLSyntaxErrorException err", ex);
        } catch (SQLException ex) {
            mLog.error("sql err", ex);
        } finally {
        	try{
        		if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} 
			catch (Exception e){
				mLog.error("error ::: " + e);
			}
        	
        	ConnectionManager.releaseDefaultConnection(con);
        	mLog.debug("connetion close");
        }
		
		
        return result;
	}
}
