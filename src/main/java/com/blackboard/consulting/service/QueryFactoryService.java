package com.blackboard.consulting.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blackboard.persist.PersistenceException;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.vxi.service.VirtualSystemException;

/**
 * Generate SQL query using by properties and parameter values
 * 
 * @author June
 * @since  2016.02.18
 */
public class QueryFactoryService {
	
	//Find location of SQL folder
	private final String root = QueryFactoryService.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("QueryFactoryService.class", "") + "../";
	
	private Logger mLog = LoggerFactory.getLogger(QueryFactoryService.class);
	
	/**
	 * get query from properties file
	 * 
	 * @author June
	 * @param  name
	 * @return value
	 */
	protected String getSql(String name) {
		String result = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			// get BB db type
			Context ctx = ContextManagerFactory.getInstance().getContext();
			String dbType = ctx.getVirtualInstallation().getDbType();
			
			mLog.debug("dbType ::: " + dbType);						
			mLog.debug("getSql name ::" + name);
			
			if(dbType.contains("mssql")) {
				fis = new FileInputStream(root + "/sql/mssql/" + name + ".properties");				
			}else {				
				fis = new FileInputStream(root + "/sql/oracle/" + name + ".properties");
			}
			
			bis = new BufferedInputStream(fis);
			
			Properties prop = new Properties();
			prop.load(bis);
			
			result = prop.getProperty("sql");
			
		} catch (IOException e) {
			mLog.error("SqlUtil getSql IOException error :: " + e);
		} catch (VirtualSystemException e) {
			mLog.error("SqlUtil getSql VirtualSystemException error :: " + e);
		} catch (PersistenceException e) {
			mLog.error("SqlUtil getSql PersistenceException error :: " + e);
		}finally {
			if(bis != null) try {bis.close();} catch (IOException e) {mLog.error("SqlUtil getSql IOException error :: " + e);}
			if(fis != null) try {fis.close();} catch (IOException e) {mLog.error("SqlUtil getSql IOException error :: " + e);}
		}

		return result;
	}

	/**
	 * Set SQL where condition 
	 * 
	 * @param ResultSet
	 * @return Map<String, Object>
	 * @throws SQLException
	 */
	protected String getWhereCondition(String sql, Map<String, Object> param) {
		mLog.debug("param ::" + param);
		
		Iterator<String> keys = param.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			sql = sql.replace("{{" + key + "}}", param.get(key).toString());
			// mLog.debug(key + " ::: " + param.get(key));
		}

		return sql;
	}
	
	/**
	 * Set SQL where condition 
	 * 
	 * @param ResultSet
	 * @return Map<String, Object>
	 * @throws SQLException
	 */
	protected String getWhereCondition(String sql, String key, String value) {

		sql = sql.replace("{{" + key + "}}", value);

		return sql;
	}
}
