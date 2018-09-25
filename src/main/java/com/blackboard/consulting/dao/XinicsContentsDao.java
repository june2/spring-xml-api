package com.blackboard.consulting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.blackboard.consulting.service.QueryFactoryService;
import com.blackboard.consulting.util.CharUtil;
import com.blackboard.consulting.util.CommonUtil;
import com.blackboard.consulting.model.ContentVO;

import blackboard.db.ConnectionManager;
import blackboard.db.ConnectionNotAvailableException;

/**
 * FeedDataDao
 * 
 * @author MJ
 * @since 2015.02.03
 */
@Repository("XinicsContentsDao")
public class XinicsContentsDao extends QueryFactoryService {
	private Logger mLog = LoggerFactory.getLogger(this.getClass());

	private Connection con = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private PreparedStatement ps = null;
	private String sql = "";

	// get Xinics Content by userNmae
	public List<ContentVO> selectXContentsQueryByUname(String queryName, Map<String, Object> param,
			List<ContentVO> result) throws ClassNotFoundException, ConnectionNotAvailableException {

		try {

			mLog.debug("connetion open");
			mLog.debug("param ::: " + param);

			con = ConnectionManager.getDefaultConnection();

			mLog.debug("connetion open");

			sql = getSql(queryName);

			// generate SQL where condition
			if (param != null)
				sql = getWhereCondition(sql, param);

			stmt = con.createStatement();
			mLog.debug("Execute query ::: " + sql);

			rs = stmt.executeQuery(sql);

			// get Result
			while (rs.next()) {

				String minimum[] = CommonUtil.getInstance().getTime(rs.getString("minimumTime")).split("/");
				String duration[] = CommonUtil.getInstance().getTime(rs.getString("duration")).split("/");

				CharUtil charUtil = new CharUtil();
				ContentVO content;
				content = new ContentVO(rs.getInt("pk1"), rs.getInt("content_pk1"), rs.getString("course_id"),
						charUtil.UnicodeToChar(rs.getString("title")), rs.getString("content_title"),
						rs.getString("url"), rs.getFloat("duration"), duration[1], duration[2], duration[3],
						rs.getFloat("minimumTime"), minimum[1], minimum[2], minimum[3], rs.getString("parentTitle"),
						rs.getString("menuTitle"), rs.getString("start_date"), rs.getString("end_date"));
				result.add(content);

			}
			mLog.debug("result query ::: " + result);

		} catch (SQLException ex) {
			mLog.debug("sql err", ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
				mLog.error("error ::: " + e);
			}
		}

		ConnectionManager.releaseDefaultConnection(con);
		mLog.debug("connetion close");

		return result;
	}
}
