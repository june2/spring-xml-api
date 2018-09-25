package com.blackboard.consulting.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blackboard.consulting.service.QueryExecutionService;
import com.blackboard.consulting.service.QueryUpdateService;
import com.blackboard.consulting.util.CommonUtil;
import com.blackboard.consulting.util.LocaleUtil;
import com.blackboard.consulting.dao.ContentItemDao;
import com.blackboard.consulting.dao.XinicsContentsDao;
import com.blackboard.consulting.model.ContentVO;
import com.blackboard.consulting.service.ContentItemServiceImpl;

import blackboard.data.ReceiptOptions;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.navigation.CourseToc;
import blackboard.db.ConnectionNotAvailableException;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.servlet.InlineReceiptUtil;
import blackboard.platform.term.CourseTerm;
import blackboard.platform.term.impl.CourseTermDAO;

@Controller
public class ContentController implements MessageSourceAware {

	private static final Logger mLog = LoggerFactory.getLogger(ContentController.class);
	private MessageSource messageSource;

	@Resource(name = "XinicsContentsDao")
	XinicsContentsDao xinicsContentsDao;
	
	@Resource(name = "ContentItemDao")
	ContentItemDao contentItemDao;
	
	@Resource(name = "QueryUpdateService")
	private QueryUpdateService qus;

	@Resource(name = "QueryExecutionMapServiceImpl")
	private QueryExecutionService<Map<String, Object>> qesMap;

	@Resource(name = "contentServiceImpl")
	ContentItemServiceImpl contentService;

	@Resource(name = "QueryExecutionListServiceImpl")
	private QueryExecutionService<List<Map<String, Object>>> qesList;

	private String getMessage(String messageKey) {
		return messageSource.getMessage(messageKey, null, LocaleUtil.getLocale());
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub
		this.messageSource = messageSource;
	}

	/**
	 * Get Xinics Contents by userId
	 * 
	 * @param crsid, crsBatchUid, menuNo, weekNo, userId
	 * @return String
	 * @throws ConnectionNotAvailableException
	 * @throws ClassNotFoundException
	 * @throws PersistenceException
	 * @throws KeyNotFoundException
	 */
	@SuppressWarnings("static-access")	
	@RequestMapping(value = "/imported-contents", method = RequestMethod.GET)
	public String getImportedContents(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model)
			throws PersistenceException, ClassNotFoundException, ConnectionNotAvailableException {
		
		mLog.debug("getImportedContents param ::: " + param);

		// Get ContentId and title
		Context ctx = ContextManagerFactory.getInstance().getContext();
		Content content = ContentDbLoader.Default.getInstance().loadById(Id.generateId(Content.DATA_TYPE, param.get("weekNo").toString()));
		CourseToc toc = CourseTocDbLoader.Default.getInstance().loadByContentId(content.getParentId());

		// Get Default week period
		String contentTitle = content.getTitle();
		mLog.debug("getImportedContents folder title : " + contentTitle);

		// Get Term		
		Map<String, Object> period = null;
		
		try {
			CourseTerm term = CourseTermDAO.get().get().loadByCourse(ctx.getCourse().getId());
			String termPk = term.getTermId().toExternalString();
			param.put("termPk", CommonUtil.getInstance().getPk(termPk));
			param.put("week", contentTitle.replaceAll("[\\D]", ""));
			
			period = qesMap.executeQuery("common/selectConfigWeekByNum", param);
			mLog.debug("getImportedContents folder preiod  : " + period);
		} catch (Exception e) {
			mLog.error("getImportedContents get  :: not found term key");
		}

		
		String userId = ctx.getUser().getBatchUid();
		param.put("userId", userId);
		String toc_pk1 = CommonUtil.getInstance().getPk(toc.getId().toExternalString());
		param.put("toc_pk1", toc_pk1);
		String folder_pk1 = CommonUtil.getInstance().getPk(content.getId().toExternalString());
		param.put("folder_pk1", folder_pk1);

		// Get custom contents List
		List<ContentVO> contentList = xinicsContentsDao.selectXContentsQueryByUname("content/selectXcontentsByUid", param, new ArrayList<ContentVO>());

		mLog.debug("======================================================================================================");
		mLog.debug("contentList : " + contentList);
		mLog.debug("======================================================================================================");

		List<Integer> pk1List = new ArrayList<Integer>();
		for (int i = 0; i < contentList.size(); i++) {
			pk1List.add(contentList.get(i).getPk1());
		}

		model.addAttribute("contentId", content.getId().toExternalString());
		model.addAttribute("contentParentId", content.getParentId().toExternalString());
		model.addAttribute("contentTitle", contentTitle);
		model.addAttribute("tocTitle", toc.getLabel());
		model.addAttribute("toc_pk1", toc_pk1);
		model.addAttribute("folder_pk1", folder_pk1);
		model.addAttribute("period", period);
		
		model.addAttribute("userId", userId);
		model.addAttribute("userName", ctx.getUser().getUserName());
		model.addAttribute("contentList", contentList);
		model.addAttribute("course_id", ctx.getCourse().getCourseId());
		model.addAttribute("course_pk", ctx.getCourse().getId().toExternalString());

		model.addAttribute("pk1List", pk1List);
		model.addAttribute("contentListSize", contentList.size());

		return "panopto-imported";
	}

	/**
	 * Create content in selected folder
	 * 
	 * @param crsid, weekNo, chk ,checkedlist, mtime
	 * @return String
	 * @throws PersistenceException
	 * @throws ValidationException
	 * @throws ConnectionNotAvailableException
	 * @throws ClassNotFoundException
	 * 
	 */
	@RequestMapping(value = "/create-contents", method = RequestMethod.POST)
	public String createContents(@RequestParam Map<String, Object> param,
			@RequestParam(value = "checkedlist", required = false) List<String> pk1List, HttpServletRequest request, Model model)
			throws PersistenceException, ClassNotFoundException, ConnectionNotAvailableException, ValidationException {

		mLog.debug("createContents param ::: " + param);
		mLog.debug("createContents pk1List ::: " + pk1List);

		ReceiptOptions ro = InlineReceiptUtil.getReceiptFromRequest(request);
		if (null == ro) {
			ro = new ReceiptOptions();
		}

		Context ctx = ContextManagerFactory.getInstance().getContext();
		String dbType = ctx.getVirtualInstallation().getDbType();

		// selected folder
		Id folderId = Id.generateId(Content.DATA_TYPE, param.get("weekNo").toString());
		String pk1 = "";
		String contentPk1 = "";
		String title = "";
		String startDate = "";
		String endDate = "";		

		for (int i = 0; i < pk1List.size(); i++) {

			mLog.debug("createContents pk1List ::: " + pk1List.get(i));
			
			HashMap<String, Object> temp = new HashMap<String, Object>();
			pk1 = pk1List.get(i);
			contentPk1 = request.getParameter("content_pk1" + pk1List.get(i));
			title = request.getParameter("title" + pk1List.get(i));
			startDate = request.getParameter("start_date" + pk1List.get(i));
			endDate = request.getParameter("end_date" + pk1List.get(i));
			
			temp.put("start_date", startDate);
			temp.put("end_date", endDate);

			temp.put("pk1", pk1);
			temp.put("mTime",
					(Double.valueOf(request.getParameter("mHour" + pk1List.get(i))) * 3600)
							+ Double.valueOf(request.getParameter("mMin" + pk1List.get(i))) * 60
							+ Double.valueOf(request.getParameter("mSec" + pk1List.get(i))));

			String existContent = contentService.existItemsInFolder(ctx.getCourse().getId(), folderId, temp, dbType);
			mLog.debug("existContent  ::: " + existContent);

			if (existContent == "") {
				mLog.debug("create ");
				String createdTitle = contentService.createItemsInFolder(ctx.getCourse().getId(), folderId, temp);
				ro.addSuccessMessage(getMessage("common.createContents.ReceiptOptions.success") + createdTitle);
			} else if (existContent.equals("update")) {
				mLog.debug("update ");
				contentItemDao.updateItem(contentPk1, title, startDate, endDate);
				ro.addSuccessMessage(getMessage("common.createContents.ReceiptOptions.update"));
			} else {
				mLog.debug("exist ");
				ro.addWarningMessage(getMessage("common.createContents.ReceiptOptions.warning") + existContent);
			}
		}

		return "redirect:imported-contents?course_id=" + ctx.getCourse().getId().toExternalString() + "&weekNo="+ param.get("weekNo").toString();
	}
	
	@RequestMapping(value = "/delete-contents")
	public String deleteContents(@RequestParam Map<String, Object> param,
								@RequestParam(value = "checkedlist", required = false) List<String> pk1List, 
								HttpServletRequest request, Model model) throws Exception {

		mLog.debug("deleteContents ::: " + param);
		mLog.debug("deleteContents ::: " + pk1List);

		ReceiptOptions ro = InlineReceiptUtil.getReceiptFromRequest(request);
		if (null == ro) {
			ro = new ReceiptOptions();
		}

		Context ctx = ContextManagerFactory.getInstance().getContext();

		for (int i = 0; i < pk1List.size(); i++) {

			// 커스텀테이블 삭제 .
			// 학생 기록 삭제
			param.put("pk1", pk1List.get(i));
			
			// not available
//			contentItemDao.deleteItem(request.getParameter("content_pk1" + pk1List.get(i)));

			Map<String, Object> resultMap = null;
			resultMap = qesMap.executeQuery("content/selectContentsByPk", param);
			mLog.debug("result : " + resultMap);

			// 콘텐츠 삭제
			contentService.DeleteContentInFolder(ctx.getCourseId(), resultMap);
			
			// get BB db type
			String dbType = ctx.getVirtualInstallation().getDbType();
			int result = 0;
			// 커스텀테이블삭제
			if(dbType.contains("mssql")) {				
				//// mssql
				result = qus.executeUpdate("content/deleteContents", param);
			}else{
				//// oracle
				result = qus.executeUpdate("content/deleteContents/deleteOnlineContent", param);
				result = qus.executeUpdate("content/deleteContents/deleteOnlineUsers", param);
				result = qus.executeUpdate("content/deleteContents/deleteOnlineUsersWatching", param);			
			}
			
			 mLog.debug("result : " + result);

			if (result > 0) {
				ro.addSuccessMessage("출결 설정 삭제가 완료되었습니다.");				
			}
		} // end for

		return "redirect:imported-contents?course_id=" + ctx.getCourse().getId().toExternalString() + "&weekNo="+ param.get("weekNo").toString();		
	}

}
