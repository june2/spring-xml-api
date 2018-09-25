package com.blackboard.consulting.service;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import blackboard.base.FormattedText;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.platform.gradebook2.AttemptDetail;
import blackboard.platform.gradebook2.BookData;
import blackboard.platform.gradebook2.BookDataRequest;
import blackboard.platform.gradebook2.GradableItem;
import blackboard.platform.gradebook2.GradeDetail;
import blackboard.platform.gradebook2.GradebookManager;
import blackboard.platform.gradebook2.GradebookManagerFactory;
import blackboard.platform.gradebook2.GradingSchema;
import blackboard.platform.gradebook2.GradingSchemaManager;
import blackboard.platform.gradebook2.impl.GradableItemDAO;
import blackboard.platform.security.authentication.BbSecurityException;

@Service("GradeColumnServiceImpl")
public class GradeColumnServiceImpl{

	private static final Logger mLog = LoggerFactory.getLogger(GradeColumnServiceImpl.class);

	/**
	 * Get DefaultGradableItem in course
	 * 
	 * @param gbItem, crs, title, schemaId
	 * @return GradableItem
	 */
	public GradableItem getDefaultGradableItem(GradableItem gbItem, Course crs, String title, Id schemaId){

		gbItem.setId(Id.UNSET_ID);
		gbItem.setCourseId(crs.getId());
		gbItem.setUserCreatedColumn(true);
		gbItem.setTitle(title);
		gbItem.setDescription(new FormattedText("Xinics Attendance", FormattedText.Type.DEFAULT));
		gbItem.isManual();

		gbItem.setVisibleInAllTerms(true);
		gbItem.setDeleted(false);
		gbItem.setMaxAttempts(1);

		gbItem.setDateAdded(Calendar.getInstance());
		gbItem.setDateModified(Calendar.getInstance());
		gbItem.setCourseContentId(null);

		gbItem.setPoints(100);
		gbItem.setVisibleToStudents(false);
		gbItem.setVisibleInBook(true);
		gbItem.setHideAttempt(false);
		gbItem.setCalculatedInd(GradableItem.CalculationType.NON_CALCULATED);
		gbItem.setGradingSchemaId(schemaId);

		return gbItem;
	}


	/**
	 * Create GradableItem in course
	 * 
	 * @param crs, title
	 * @return 
	 */
	public void createGradeColumn(Course crs, String title) throws Exception {
		// TODO Auto-generated method stub

		GradebookManager gm = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
		BookData bookData = gm.getBookData(new BookDataRequest(crs.getId()));
		bookData.addParentReferences();
		bookData.runCumulativeGrading();

		GradableItem gi = null;
		List<GradableItem> list = bookData.getGradableItems();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTitle().equals(title)) {
				gi = list.get(i);
				break;
			}
		}

		if (gi == null) {

			GradingSchemaManager gsm = GradebookManagerFactory.getInstance();
			List<GradingSchema> gslist = gsm.getGradingSchemaForCourse(crs.getId());

			try {
				Id schemaId = null;
				// Generate Id value for Schema
				// 0 : score, 1 : letter
				if (title.contains("진도율")) // percentage
					schemaId = gslist.get(3).getId();
				else if (title.contains("상태")) // status
					schemaId = gslist.get(2).getId();

				// Create new Gradable Item and set values
				gi = getDefaultGradableItem(new GradableItem(), crs, title, schemaId);
				gm.persistGradebookItem(gi);

			} catch (Exception ex) {
				// Set error flag - no columns will be persisted below
				mLog.debug(ex.getMessage());
			}
		}

	}

	

	/**
	 * initGradeCenter in course
	 * 
	 * @param crsId, title
	 * @return boolean
	 */
	public boolean initGradeCenter(Id crsId, String title) {
		// TODO Auto-generated method stub
		boolean result = false;

		try {
			Course crs = CourseDbLoader.Default.getInstance().loadById(crsId);// loadByCourseId(courseId);

			if (crs != null) {
				// percentage column
				List<GradableItem> list = GradableItemDAO.get().getGradableItemByCourseAndColumnTitle(crs.getId(),
						title);

				if (list != null && list.size() > 0) {
					GradableItem gi = list.get(0);
					GradableItemDAO.get().updateVisibleInBook(gi.getId(), true);
					GradableItemDAO.get().setColumnStudentVisibility(gi.getId(), false);
					GradableItemDAO.get().persist(gi);

					result = true;
				}

			}
		} catch (Exception e) {
			mLog.error("Error initializing grade center.", e);
		}
		return result;
	}

	/**
	 * update user's grade in Grade Center
	 * 
	 * @param crs, userId, score
	 * @return boolean
	 */
	public boolean updateGrade(Course crs, Id userId, Double score, Double cutoff) {
//		String perTitle = "온라인 출석 진도율"; // online attendance percentage
//		String statusTitle = "온라인 출석 상태"; // online attendance status
		String perTitle = "Panato 온라인 출석 진도율"; // online attendance percentage
		String statusTitle = "Panato 온라인 출석 상태"; // online attendance status
		
		boolean result = false;
		CourseMembership cms;
		
		try {
			cms = CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId(crs.getId(), userId);

			GradebookManager gm = GradebookManagerFactory.getInstanceWithoutSecurityCheck();
			BookData bookData = gm.getBookData(new BookDataRequest(crs.getId()));
			bookData.addParentReferences();
			bookData.runCumulativeGrading();

			List<GradableItem> list = bookData.getGradableItems();

			if (list != null) {
				AttemptDetail ad = null;
				GradableItem gi = null;
				GradeDetail gd = null;

//				if (score == null) {
//					score = "0";					
//				}

				for (int i = 0; i < list.size(); i++) {
					// percentage column
					if ((list.get(i).getTitle() != null) && (list.get(i).getTitle().equals(perTitle))) {
						gi = list.get(i);

						// Attempt Table
						ad = gm.createAttempt(gi.getId(), cms.getId());

						// Grade_grade Table
						gd = gm.getGradeDetailForAttempt(ad.getId());
						gd.setManualScore(score);
						gd.setManualGrade(String.valueOf(score));
						gm.updateGrade(gd, true, crs.getId());

					}
					// status column
					if ((list.get(i).getTitle() != null) && (list.get(i).getTitle().equals(statusTitle))) {
						gi = list.get(i);

						ad = gm.createAttempt(gi.getId(), cms.getId());

						// Grade_grade Table
						gd = gm.getGradeDetailForAttempt(ad.getId());

						if (score >= cutoff) {
							gd.setManualGrade("P");
						} else {
							gd.setManualGrade("F");
						}

						gm.updateGrade(gd, true, crs.getId());
					}

				}
				result = true;
			}
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
			result = false;
		} catch (PersistenceException e) {
			e.printStackTrace();
			result = false;
		} catch (BbSecurityException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
}
