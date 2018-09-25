package com.blackboard.consulting.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import blackboard.base.FormattedText;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.navigation.CourseToc;
import blackboard.data.navigation.CourseToc.Target;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.navigation.CourseTocDbLoader;

@Repository("ContentItemDao")
public class ContentItemDao {
	
	private static final Logger mLog = LoggerFactory.getLogger(ContentItemDao.class);
	
	/**
	 * Create Item content in course
	 * 
	 * @param crsId, parentId, itemName, itemHtml, handler
	 * @return Id
	 * @throws ValidationException
	 * @throws PersistenceException
	 */
	public Id createItem(Id crsId, Id parantId, String itemName, StringBuffer itemHtml, String handler, String url, String startDate, String endDate) {
		// TODO Auto-generated method stub

		Content content = new Content();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar start = Calendar.getInstance();
	        start.setTime(sdf.parse(startDate));
	        Calendar end = Calendar.getInstance();
	        end.setTime(sdf.parse(endDate));
	        
			content.setCourseId(crsId);
			content.setIsFolder(false);
			content.setIsAvailable(true);
			content.setTitle(itemName);
			content.setContentHandler(handler);
			content.setLaunchInNewWindow(false);
			content.setLaunchInNewWindow(true);
			content.setEndDate(end);
			content.setStartDate(start);
			content.setRenderType(Content.RenderType.URL);					
			
			FormattedText fm2t = new FormattedText(itemHtml.toString(), FormattedText.Type.HTML);
			content.setBody(fm2t);
			content.setParentId(parantId);
			content.setLaunchInNewWindow(false);
					
			ContentDbPersister.Default.getInstance().persist(content);
			
		} catch (PersistenceException e) {		 
			mLog.error("createItem PersistenceException ::: " + e);
		} catch (ValidationException e) {		 
			mLog.error("createItem PersistenceException ::: " + e);
		} catch (ParseException e) {
			 mLog.error("createItem ParseException ::: " + e);
		}
		
		return content.getId();
	}
	
	/**
	 * update item content in course
	 * 
	 * @param crsId, parentId, itemName, itemHtml, handler
	 * @return Id
	 * @throws ValidationException
	 * @throws PersistenceException
	 */
	public void updateItem(String pk1, String title, String startDate, String endDate) {

		try {
			mLog.error("updateItem::: "+ pk1 +", "+ title +", "+ startDate +", "+ endDate);
			Content content = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, pk1));
			
			String itemName = title.split(" / ")[0] +" / "+ startDate.substring(0, 16) + " ~ " + endDate.substring(0, 16);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar start = Calendar.getInstance();
	        start.setTime(sdf.parse(startDate));
	        Calendar end = Calendar.getInstance();
	        end.setTime(sdf.parse(endDate));
	        
			content.setTitle(itemName);
			content.setEndDate(end);
			content.setStartDate(start);
			ContentDbPersister.Default.getInstance().persist(content);
			
		} catch (PersistenceException e) {		 
			mLog.error("createItem PersistenceException ::: " + e);
		} catch (ValidationException e) {		 
			mLog.error("createItem PersistenceException ::: " + e);
		} catch (ParseException e) {
			 mLog.error("createItem ParseException ::: " + e);
		}
	}
	
	/**
	 * delete item content in course
	 * 
	 * @param crsId, parentId, itemName, itemHtml, handler
	 * @return Id
	 * @throws ValidationException
	 * @throws PersistenceException
	 */
	public void deleteItem(String pk1) {
		try {
			mLog.error("updateItem::: "+ pk1);
			Content content = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, pk1));
			content.setIsAvailable(false);
			ContentDbPersister.Default.getInstance().persist(content);
		} catch (PersistenceException e) {		 
			mLog.error("createItem PersistenceException ::: " + e);
		} catch (ValidationException e) {		 
			mLog.error("createItem PersistenceException ::: " + e);
		}
	}

	/**
	 * Create folder content in course
	 * @param crsId, parentId, folderName, html
	 * @return Id
	 */
	public Id createFolder(Id courseId, Id parentId, String folderName, String html) {
		// TODO Auto-generated method stub
		Content content = new Content();
		content.setCourseId(courseId);
		content.setIsFolder(true);
		content.setIsAvailable(true);
		content.setTitle(folderName);

		FormattedText fm2t = new FormattedText(html.toString(), FormattedText.Type.HTML);
		content.setBody(fm2t);
		content.setContentHandler("resource/x-bb-folder");
		content.setParentId(parentId);

		try {
			ContentDbPersister.Default.getInstance().persist(content);
		} catch (PersistenceException e) {
			mLog.error("createFolder PersistenceException ::: " + e);
		} catch (ValidationException e) {
			mLog.error("createFolder ValidationException ::: " + e);
		}

		return content.getId();
	}

	/**
	 * Create Toc(menu) content in course
	 * 
	 * @param courseId, tocName
	 * @return Id
	 * @throws ValidationException
	 * @throws PersistenceException
	 */
	public Id createToc(Id courseId, String tocName, boolean Enable){
		Content content = new Content();
		CourseToc ctoc = new CourseToc();
		
		try {
			content.setCourseId(courseId);
			content.setIsFolder(true);
			content.setAllowObservers(false);
			content.setIsAvailable(true);
			content.setTitle(tocName);
			content.setContentHandler("resource/x-bb-folder");
			content.persist();

			ctoc.setPosition(1);
			ctoc.setCourseId(courseId);
			ctoc.setContentId(content.getId());
			ctoc.setTargetType(Target.CONTENT);
			ctoc.setLabel(tocName);
			ctoc.setIsEnabled(Enable);
			ctoc.setInternalHandle("content");
			ctoc.persist();			
		} catch (PersistenceException e) {
			mLog.error("createToc PersistenceException ::: " + e);
		} catch (ValidationException e) {
			mLog.error("createToc ValidationException ::: " + e);
		}
		
		return ctoc.getContentId();
	}
	
	/**
	 * Check if folder exist or not in course Toc
	 * 
	 * @param crsId, parentId, itemName, handler
	 * @return Id
	 */
	public Id existFolder(Id crsId, Id parentId, String itemName, String handler) {
		
		Id result = null;

		try {
			List<Content> itemList = ContentDbLoader.Default.getInstance().loadByCourseIdAndTitle(crsId, itemName);

			for (int i = 0; i < itemList.size(); i++) {

				Content content = ContentDbLoader.Default.getInstance().loadById(itemList.get(i).getId());
				// 제목, 설명, handler다 같으
				if (parentId.equals(content.getParentId()) && handler.equals(content.getContentHandler())) {
					result = content.getId();
					break;
				}
			}

		} catch (KeyNotFoundException e) {			
			mLog.error("existFolder KeyNotFoundException ::: " + e);
			result = null;
		} catch (PersistenceException e) {
			mLog.error("crsaToc no exist, so we have to create it ::: " + e);
			result = null;
		}

		return result;
	}

	/**
	 * Check if Toc exist or not in course
	 * 
	 * @param crsId,
	 *            tocName
	 * @return Id
	 */
	public Id existToc(Id crsId, String tocName) {
		Id result = null;
		mLog.debug("EXIST irstToc : : " + tocName);

		try {
			CourseToc crsToc = CourseTocDbLoader.Default.getInstance().loadByCourseIdAndLabel(crsId, tocName);
			mLog.debug("crsToc exist : " + crsToc);
			result = crsToc.getContentId();

		} catch (KeyNotFoundException e) {
			mLog.error("existToc KeyNotFoundException ::  " + e);
			result = null;
		} catch (PersistenceException e) {
			mLog.error("existToc PersistenceException " + e);
			result = null;
		}
		return result;
	}
}
