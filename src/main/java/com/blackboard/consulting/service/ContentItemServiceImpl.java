package com.blackboard.consulting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import blackboard.base.FormattedText;
import blackboard.data.content.Content;
import blackboard.data.content.Content.RenderType;
import blackboard.data.navigation.CourseToc;
import blackboard.data.navigation.CourseToc.Target;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;

import com.blackboard.consulting.dao.ContentItemDao;
import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.service.QueryUpdateService;
import com.blackboard.consulting.util.CharUtil;
import com.blackboard.consulting.util.CommonUtil;

@Service("contentServiceImpl")
public class ContentItemServiceImpl {
	
	private static final Logger mLog = LoggerFactory.getLogger(ContentItemServiceImpl.class);
	
	@Resource(name = "ContentItemDao")
	private ContentItemDao cid;

	@Resource(name = "QueryUpdateService")
	private QueryUpdateService qus;

	@Resource(name = "QueryExecutionMapServiceImpl")
	private QueryExecutionService<Map<String, Object>> qesMap;

	/**
	 * Get item content
	 * @param crsId,  parentId, itemName, handler
	 * @return Content
	 * @throws PersistenceException
	 */
	public Content getItem(Id crsId, Id parentId, String itemName, String handler) throws PersistenceException {
		Content item = null;
		mLog.debug("itemName :: " + itemName);

		List<Content> itemList = ContentDbLoader.Default.getInstance().loadByCourseIdAndTitle(crsId, itemName);

		for (int i = 0; i < itemList.size(); i++) {
			if (parentId.equals(itemList.get(i).getParentId()) && handler.equals(itemList.get(i).getContentHandler())) {				
				item = itemList.get(i);
			}
		}

		return item;
	}

	/**
	 * Get week folder list in course Toc
	 * @param crsid, tocName
	 * @return List<Map<String,Object>>
	 * @throws PersistenceException
	 * @throws KeyNotFoundException
	 */
	public List<Map<String, Object>> getWeek(String crsid, String tocNo) throws KeyNotFoundException, PersistenceException {

		Id crsId = CourseDbLoader.Default.getInstance().loadByBatchUid(crsid).getId();
		Id tocContentId = Id.generateId(Content.DATA_TYPE, tocNo);
		mLog.debug("tocContentId :: " + tocContentId);
		
		List<Content> weekList = ContentDbLoader.Default.getInstance().loadChildren(tocContentId);// (tocId);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (weekList.size() == 0) {
			for (int i = 0; i < 3; i++) {
				Map<String, Object> temp = new HashMap<String, Object>();

				Id weekFolder = cid.createFolder(crsId, tocContentId, (i + 1) + "주", "");

				temp.put("weekId", CommonUtil.getInstance().getPk(weekFolder.getExternalString()));
				temp.put("weekTitle", ContentDbLoader.Default.getInstance().loadById(weekFolder).getTitle());

				result.add(temp);
			} // end for
		}else{
			for (int i = 0; i < weekList.size(); i++) {
				if (weekList.get(i).getIsFolder()) {
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("weekId", CommonUtil.getInstance().getPk(weekList.get(i).getId().getExternalString()));
					temp.put("weekTitle", weekList.get(i).getTitle());

					result.add(temp);
				}
			} // end for
		}

		return result;
	}

	/**
	 * Get toc(menu) list in course
	 * @param crsid
	 * @return List<Content>
	 */
	public List<Content> getToc(Id crsId) {

		List<Content> contentTocList = new ArrayList<Content>();

		try {
			List<CourseToc> tocList = CourseTocDbLoader.Default.getInstance().loadByCourseId(crsId);

			for (int i = 0; i < tocList.size(); i++) {
				if (tocList.get(i).getTargetType().equals(Target.CONTENT) && tocList.get(i).getIsEnabled()) {
					Content contentToc = ContentDbLoader.Default.getInstance().loadByTocId(tocList.get(i).getId());
					contentTocList.add(contentToc);
				}
			}
		} catch (KeyNotFoundException e) {
			mLog.error("getToc ::: " + e);
		} catch (PersistenceException e) {
			mLog.error("getToc ::: " + e);
		}

		return contentTocList;
	}

	/**
	 * Create item contents in week folder - xinics
	 * @param root, crsId, parantId, param
	 * @return String
	 */
	public String createItemsInFolder(Id crsId, Id parentId, Map<String, Object> param) {
	
		// Create content
		// To use Xinics player, add script source code into content HTML value.
		Map<String, Object> contentMap = qesMap.executeQuery("content/selectXcontentsByPk", param);
		String servername = CommonUtil.getInstance().getValue("config/props/config.properties", "servername");

		CharUtil charUtil = new CharUtil();
		String title = charUtil.UnicodeToChar(contentMap.get("title").toString()) + " / "
					 + param.get("start_date").toString().substring(0, 16) + " ~ "
				     + param.get("end_date").toString().substring(0, 16);
		
		StringBuffer desc = new StringBuffer();
		String iframe = "<script src=/webapps/ppto-panopto-online-attendance-BBLEARN/js/sync-bb-panopto.js type=text/javascript></script>"
				+ "<iframe src=\"https://" +servername+ "/Panopto/Pages/Embed.aspx?id=" +contentMap.get("url")+ "&v=1\" "
				+ "width=\"100%\" height=\"600px\" style=\"padding: 0px; border: 1px solid #464646;\" "
				+ "frameborder=\"0\" allowfullscreen></iframe>";
		desc.append(iframe);
		
		mLog.debug("createItemsInFolder  contentMap :::::: " + contentMap);		
		
		// create papopto content in BB  
		Id contentId = cid.createItem(crsId, parentId, title, desc, "resource/x-bb-blankpage", 
				contentMap.get("url").toString(), param.get("start_date").toString(), param.get("end_date").toString());		
		
		// update content minimum time
		param.put("content_pk1", Integer.parseInt(CommonUtil.getInstance().getPk(contentId.getExternalString())));
		qus.executeUpdate("content/updateContent", param);
		
		mLog.debug("param  contentMap :::::: " + param);

		return contentMap.get("title").toString();
	}

	/**
	 * Check if item exist or not in weekly Folder
	 * @param root, crsId, folderId, param
	 * @return String
	 */
	public String existItemsInFolder(Id crsId, Id folderId, Map<String, Object> param, String dbType) {

		String result = "";
		try {

			Map<String, Object> contentMap = qesMap.executeQuery("content/selectXcontentsByPk", param);

			mLog.debug("existItemsInFolder contentMap  :: " + contentMap);
			mLog.debug("existItemsInFolder folderId  " + folderId);
			
			String key = "minimumTime"; //// mssql  	
			if(dbType.contains("mssql")) {				
				key = "minimumtime"; //// oracle
			}
									
			if(contentMap.get("content_pk1").equals("0") && contentMap.get(key).equals(null)){
				return result;
			} else {
				// check
				@SuppressWarnings("deprecation")
				Content content = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, Integer.parseInt(contentMap.get("content_pk1").toString())));

				String preTitle = "";
				if (content.getTitle().contains(" / ")) {
					preTitle = content.getTitle().substring(0, content.getTitle().length() - 38);
				} else {
					preTitle = content.getTitle();
				}

				content.setTitle(preTitle + " / " + param.get("start_date").toString().substring(0, 16) +" ~ "+ param.get("end_date").toString().substring(0, 16));

				if (content.getParentId().equals(folderId) && content.getContentHandler().equals("resource/x-bb-blankpage")) {					
				
					if (param.get("mTime").toString().equals(contentMap.get(key).toString()) 
							&& param.get("start_date").toString().equals(contentMap.get("start_date").toString()) 
							&& param.get("end_date").toString().equals(contentMap.get("end_date").toString())) {
						mLog.debug("already created ");
						result = content.getTitle();
						
					} else if (param.get("mTime").toString() != contentMap.get(key).toString() 
							|| param.get("start_date").toString() == "0" || param.get("end_date").toString() == "0") {
						mLog.debug("diffrent the minimumTime");
						qus.executeUpdate("common/updateMinTime", param);
						result = "update";
					}
				}

				ContentDbPersister.Default.getInstance().persist(content);
			}

		} catch (Exception e) {
			mLog.error("existItemsInFolder ::: " + e);
		}

		return result;
	}

	/**
	 * Get item contents in week folder
	 * @param root, crsId, folderId, param
	 * @return String
	 */
	public String getItemsInFolder(String root, Id crsId, Id folderId, Map<String, Object> param) {

		String result = "";
		try {

			Map<String, Object> contentMap = qesMap.executeQuery("content/selectXcontentsByPk", param);

			// check
			List<Content> itemList = ContentDbLoader.Default.getInstance().loadByCourseIdAndTitle(crsId,
					contentMap.get("title").toString());

			for (int i = 0; i < itemList.size(); i++) {
				Content content = ContentDbLoader.Default.getInstance().loadById(itemList.get(i).getId());

				// title, handler, folder same
				if (content.getParentId().equals(folderId)
						&& content.getContentHandler().equals("resource/x-bb-blankpage")
						&& content.getTitle().equals(contentMap.get("title"))) {
					result = content.getTitle();
					break;
				}
			}

		} catch (Exception e) {
			mLog.error("getItemsInFolder ::: " + e);	
		}

		return result;
	}

	/**
	 * Update item contents in week folder
	 * @param crsId, folderId, param
	 * @return Content
	 */
	public Content updateItemsInFolder(Id crsId, Id folderId, Map<String, Object> param) {

		Content item = null;
		try {

			item = ContentDbLoader.Default.getInstance()
					.loadById(Id.generateId(Content.DATA_TYPE, param.get("content_pk1").toString()));
			mLog.debug("item :::: " + item);

			StringBuffer desc = new StringBuffer();

			String preTitle = "";
			if (item.getTitle().contains(" / ")) {

				preTitle = item.getTitle().substring(0, item.getTitle().length() - 38);
				mLog.debug("preTitle  //// :::: " + preTitle);

			} else {
				preTitle = item.getTitle();
				mLog.debug("preTitle :::: " + preTitle);
			}

			String tmp = item.getBody().getFormattedText()
					.replace("/webapps/bbgs-OnlineAttendance-BBLEARN/js/BB-timer.js", "")
					.replace("<script src= type=text/javascript></script>", "")
					.replace("본 학습 창을 닫으면 학습 진도율이 반영되지 않습니다. 학습이 완료된 후에 창을 닫아야 하며, 학습후에는 반드시 온라인 출결 확인을 하시기 바랍니다.", "");

			if (item.getContentHandler().equals("resource/x-bb-externallink")) // when content type is web link
			{
				item.setTitle(preTitle + " / " + param.get("start_date").toString().substring(0, 16) + " ~ "
						+ param.get("end_date").toString().substring(0, 16));

				item.setRenderType(RenderType.REGULAR);
				item.setLaunchInNewWindow(false);
				desc.append("<input type=\"hidden\"  name=\"content_" + param.get("content_pk1").toString()
						+ "_pk1\" value=\"" + param.get("content_pk1").toString() + "\" />"
						+ "<script src=/webapps/bbgs-OnlineAttendance-BBLEARN/js/BB-timer.js type=text/javascript></script>"
						+ "<span style='font-size: large; color: #0000ff;'><strong>본 학습 창을 닫으면 학습 진도율이 반영되지 않습니다. 학습이 완료된 후에 창을 닫아야 하며, 학습후에는 반드시 온라인 출결 확인을 하시기 바랍니다.</strong></span><br><br>"
						+ "<span style='font-size: medium; color: #ff0000;'><strong><a href=" + item.getUrl()
						+ " target=_blank ><span style='color: #ff0000;'>" + preTitle
						+ "(클릭)</span></a></strong></span><br><br>");
			} else {
				item.setTitle(preTitle + " / " + param.get("start_date").toString().substring(0, 16) + " ~ "
						+ param.get("end_date").toString().substring(0, 16));

				// desc.append("<input type='hidden'
				// name='content_"+param.get("content_pk1").toString()+"_pk1'/><script
				// src=/webapps/bbgs-OnlineAttendance-BBLEARN/js/BB-timer.js
				// type=text/javascript></script>");
				desc.append("<input type=\"hidden\" name=\"content_" + param.get("content_pk1").toString()
						+ "_pk1\" value=\"" + param.get("content_pk1").toString()
						+ "\" /><script src=/webapps/bbgs-OnlineAttendance-BBLEARN/js/BB-timer.js type=text/javascript></script>"
						+ "<span style='font-size: large; color: #0000ff;'><strong>본 학습 창을 닫으면 학습 진도율이 반영되지 않습니다. 학습이 완료된 후에 창을 닫아야 하며, 학습후에는 반드시 온라인 출결 확인을 하시기 바랍니다.</strong></span><br><br>");

			}
			desc.append(tmp);
			// desc.append(item.getBody().getFormattedText().replace("<script
			// src=/webapps/bbgs-OnlineAttendance-BBLEARN/js/BB-timer.js
			// type=text/javascript></script>", ""));
			FormattedText fm2t = new FormattedText(desc.toString(), FormattedText.Type.HTML);
			item.setBody(fm2t);
			item.setContentHandler("resource/x-bb-blankpage");
			ContentDbPersister.Default.getInstance().persist(item);

		} catch (Exception e) {
			mLog.error("updateItemsInFolder ::: " + e);				
		}

		return item;
	}

	/**
	 * Update copied Blackboard contents in week folder
	 * @param crsId, folderId, param
	 * @return Content
	 */
	public Content updateBBCopiedItemsInFolder(Id folderId, Map<String, Object> param, Map<String, Object> period) {

		Content item = null;
		try {

			item = ContentDbLoader.Default.getInstance()
					.loadById(Id.generateId(Content.DATA_TYPE, param.get("content_pk1").toString()));
			mLog.debug("item :::: " + item);

			StringBuffer desc = new StringBuffer();

			String preTitle = "";
			if (item.getTitle().contains(" / ")) {
				preTitle = item.getTitle().substring(0, item.getTitle().length() - 38);
			} else {
				preTitle = item.getTitle();
			}

			if (period.isEmpty() == true) {
				item.setTitle(preTitle);
			} else {
				item.setTitle(preTitle + " / " + period.get("start_date") + " ~ " + period.get("end_date"));
			}
			desc.append("<input type=\"hidden\" name=\"content_" + param.get("content_pk1").toString()
					+ "_pk1\" value=\"" + param.get("content_pk1").toString() + "\" />");
			desc.append(item.getBody().getFormattedText());

			FormattedText fm2t = new FormattedText(desc.toString(), FormattedText.Type.HTML);
			item.setBody(fm2t);
			item.setContentHandler("resource/x-bb-blankpage");
			ContentDbPersister.Default.getInstance().persist(item);

		} catch (Exception e) {
			mLog.error("updateBBCopiedItemsInFolder ::: " + e);						
		}

		return item;
	}

	/**
	 * delete time item contents in week folder
	 * @param crsId, folderId, param
	 * @return Content
	 */
	public Content DeleteBBTimerItemsInFolder(Id crsId, Id folderId, Map<String, Object> param) {

		Content item = null;
		try {

			item = ContentDbLoader.Default.getInstance()
					.loadById(Id.generateId(Content.DATA_TYPE, param.get("content_pk1").toString()));

			StringBuffer desc = new StringBuffer();

			String preTitle = "";
			if (item.getTitle().contains(" / ")) {
				preTitle = item.getTitle().substring(0, item.getTitle().length() - 38);

			} else {
				preTitle = item.getTitle();
			}
			mLog.debug("preTitle ::: " + preTitle);
			item.setTitle(preTitle);

			String temp = item.getBody().getFormattedText().replace("/webapps/bbgs-OnlineAttendance-BBLEARN/js/BB-timer.js", "");
			mLog.debug("temp ::: " + temp);

			desc.append(temp);
			mLog.debug("desc ::: " + desc);
			FormattedText fm2t = new FormattedText(desc.toString(), FormattedText.Type.HTML);
			item.setBody(fm2t);

			ContentDbPersister.Default.getInstance().persist(item);

		} catch (Exception e) {
			mLog.error("DeleteBBTimerItemsInFolder ::: " + e);			
		}

		return item;
	}

	/**
	 * delete item contents in week folder
	 * @param crsId, folderId, param
	 * @return Content
	 */
	public void DeleteContentInFolder(Id crsId, Map<String, Object> param) {

		Content item = null;
		try {
			item = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, param.get("content_pk1").toString()));
			ContentDbPersister.Default.getInstance().deleteById(item.getId());
		} catch (Exception e) {
			mLog.error("DeleteContentInFolder ::: " + e);			
		}
	}

	/**
	 * Update copied Blackboard contents in week folder
	 * @param crsId, folderId, param
	 * @return Content
	 */
	public Content updateXinicsCopiedItemsInFolder(Id folderId, Map<String, Object> param, Map<String, Object> period) {

		Content item = null;
		try {

			item = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, param.get("content_pk1").toString()));
			mLog.debug("item :::: " + item);

			StringBuffer desc = new StringBuffer();

			String preTitle = "";
			if (item.getTitle().contains(" / ")) {
				preTitle = item.getTitle().substring(0, item.getTitle().length() - 38);
			} else {
				preTitle = item.getTitle();
			}

			if (period == null) {
				item.setTitle(preTitle);
			} else {
				item.setTitle(preTitle + " / " + period.get("start_date") + " ~ " + period.get("end_date"));
			}

			desc.append("<input type=\"hidden\" name=\"content_" + param.get("online_pk1").toString() + "_pk1\" value=\"" + param.get("online_pk1").toString() + "\" />");

			desc.append(item.getBody().getFormattedText().replace("id=" + param.get("old_content_pk1").toString(), "id=" + param.get("online_pk1").toString()));

			FormattedText fm2t = new FormattedText(desc.toString(), FormattedText.Type.HTML);
			item.setBody(fm2t);
			item.setContentHandler("resource/x-bb-blankpage");
			ContentDbPersister.Default.getInstance().persist(item);

		} catch (Exception e) {
			mLog.error("updateXinicsCopiedItemsInFolder ::: " + e);
		}

		return item;
	}

	/**
	 * delete item contents in week folder
	 * @param crsId, folderId, param
	 * @return Content
	 */
	public Content DeleteXinicsTimerItemsInFolder(Id crsId, Id folderId, Map<String, Object> param) {

		Content item = null;
		try {

			item = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, param.get("content_pk1").toString()));

			String preTitle = "";
			if (item.getTitle().contains(" / ")) {
				preTitle = item.getTitle().substring(0, item.getTitle().length() - 38);
			} else {
				preTitle = item.getTitle();
			}

			item.setTitle(preTitle);
			ContentDbPersister.Default.getInstance().persist(item);

		} catch (Exception e) {
			mLog.error("DeleteXinicsTimerItemsInFolder ::: " + e);
		}

		return item;
	}

}
