package com.blackboard.consulting.model;

public class ContentVO {

	private int pk1; // x_content_id
	private int content_pk1;
	private String url;
	private String course_Id; // conent - 콘텐츠 개요

	private float duration; // seconds
	private String dHour;
	private String dMin;
	private String dSec;
	
	private float minimumTime; // view_url
	private String mHour;
	private String mMin;
	private String mSec;
	
	private String title;
	private String content_title;
	private String parent_title; //folder name
	private String ancestor_title; // menu name
	private String start_date;
	private String end_date;

	
	public ContentVO(int pk1, int content_pk1, String url, String course_Id, String title, 
			float duration, float minimumTime, String start_date, String end_date) {
		super();
		this.pk1 = pk1;
		this.content_pk1 = content_pk1;
		this.url = url;
		this.course_Id = course_Id;
		this.duration = duration;
		this.title = title;
		this.minimumTime = minimumTime;
		this.start_date = start_date;
		this.end_date = end_date;
	}

	public ContentVO(int pk1, int content_pk1, String course_Id, String title, String content_title, String url, float duration, float minimumTime,
			String parent_title, String ancestor_title, String start_date, String end_date) {
		super();
		this.pk1 = pk1;
		this.content_pk1 = content_pk1;
		this.course_Id = course_Id;
		this.url = url;
		this.title = title;
		this.content_title = content_title;
		this.duration = duration;
		this.minimumTime = minimumTime;
		this.parent_title = parent_title;
		this.ancestor_title = ancestor_title;
		this.start_date = start_date;
		this.end_date = end_date;
	}
	
	
	
	public ContentVO(int pk1, int content_pk1, String course_Id, String title, String content_title, String url,
			float duration, String dHour,String dMin,String dSec,
			float minimumTime,String mHour,String mMin,String mSec,
			String parent_title, String ancestor_title, String start_date, String end_date) {
	
		super();
		this.pk1 = pk1;
		this.content_pk1 = content_pk1;
		this.course_Id = course_Id;
		this.url = url;
		this.title = title;
		this.content_title = content_title;
		
		this.duration = duration;
		this.dHour = dHour;
		this.dMin = dMin;
		this.dSec = dSec;
		
		this.minimumTime = minimumTime;
		this.mHour = mHour;
		this.mMin = mMin;
		this.mSec = mSec;
		
		this.parent_title = parent_title;
		this.ancestor_title = ancestor_title;
		this.start_date = start_date;
		this.end_date = end_date;
	}
	
	public String getmHour() {
		return mHour;
	}

	public void setmHour(String mHour) {
		this.mHour = mHour;
	}

	public String getmMin() {
		return mMin;
	}

	public void setmMin(String mMin) {
		this.mMin = mMin;
	}

	public String getmSec() {
		return mSec;
	}

	public void setmSec(String mSec) {
		this.mSec = mSec;
	}

	public ContentVO(int pk1, String course_Id, String title, float duration) {
		super();
		this.pk1 = pk1;
		this.course_Id = course_Id;
		this.title = title;
		this.duration = duration;
	}

	
	public String getContent_title() {
		return content_title;
	}

	public void setContent_title(String content_title) {
		this.content_title = content_title;
	}

	public ContentVO() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPk1() {
		return pk1;
	}

	public void setPk1(int pk1) {
		this.pk1 = pk1;
	}

	public int getContent_pk1() {
		return content_pk1;
	}

	public void setContent_pk1(int content_pk1) {
		this.content_pk1 = content_pk1;
	}

	public String getCourse_Id() {
		return course_Id;
	}

	public void setCourse_Id(String course_Id) {
		this.course_Id = course_Id;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getMinimumTime() {
		return minimumTime;
	}

	public void setMinimumTime(float minimumTime) {
		this.minimumTime = minimumTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParent_title() {
		return parent_title;
	}

	public void setParent_title(String parent_title) {
		this.parent_title = parent_title;
	}

	public String getAncestor_title() {
		return ancestor_title;
	}

	public void setAncestor_title(String ancestor_title) {
		this.ancestor_title = ancestor_title;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getdHour() {
		return dHour;
	}

	public void setdHour(String dHour) {
		this.dHour = dHour;
	}

	public String getdMin() {
		return dMin;
	}

	public void setdMin(String dMin) {
		this.dMin = dMin;
	}

	public String getdSec() {
		return dSec;
	}

	public void setdSec(String dSec) {
		this.dSec = dSec;
	}

	
	

}
