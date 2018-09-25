package com.blackboard.consulting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import blackboard.db.ConnectionManager;
import blackboard.db.ConnectionNotAvailableException;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

/**
 * DB Insert, Update, Delete DAO 
 * 
 * @author June
 * @since  2016.02.18
 */
@Repository("QueryUpdateDao")
public class QueryUpdateDao {
	
	protected static final Logger mLog = LoggerFactory.getLogger(QueryUpdateDao.class);

//	protected Connection con = null;
//	protected PreparedStatement pstmt = null;
	
	int result = 1;
	
	/**
	 * @param sql
	 * @return boolean
	 */
	public int executeUpdate(String sql) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			mLog.debug("connetion open");
           
			con = ConnectionManager.getDefaultConnection();
            mLog.debug("executeUpdate query ::: " + sql);
                        
            // get BB db type
			Context ctx = ContextManagerFactory.getInstance().getContext();
			String dbType = ctx.getVirtualInstallation().getDbType();					
			
			if(dbType.contains("mssql")) {
				//// mssql
				pstmt= con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				result = pstmt.executeUpdate();
				rs = pstmt.getGeneratedKeys();		            
		        	while (rs.next()) {
		        		if(rs.getString(1) != null) result = Integer.parseInt(rs.getString(1));
		        	}
								
			}else {
				//// oracle
				stmt = con.createStatement();
				result = stmt.executeUpdate(sql);				
			}
            			
			mLog.debug("executeUpdate result ::: " + result);
            
	    }catch(NullPointerException ex ){
	    	mLog.error("NullPointerException err", ex);
            result =  -1;
	    }catch (ConnectionNotAvailableException ex) {
           mLog.error("connection err", ex);
           result =  -1;
		} catch (SQLSyntaxErrorException ex) {
	        mLog.error("SQLSyntaxErrorException err", ex);
	        result =  -1;
	    }catch (SQLException ex) {
		    	mLog.error("SQLException err", ex);
		    	result =  -1;
	    }catch (Exception ex){
			mLog.error("Exception ::: " + ex);
			result =  -1;
		}finally {
			try{
				if (rs != null) rs.close();       		
				if (pstmt != null) pstmt.close();
				if (stmt != null) stmt.close();
			} 
			catch (Exception e){
				mLog.error("error ::: " + e);
				result =  -1;
			}
			ConnectionManager.releaseDefaultConnection(con);
			mLog.debug("connetion close");
		}
		
		
		
        return result;
	}
}
