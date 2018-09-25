<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><!-- checkGuest="Y" , since this tool can be made available to course guest and guest users and tool availability is checked rather than using entitlement-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ page import="com.blackboard.consulting.util.CommonUtil" %>
<%@ page session="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib uri="/bbUI" prefix="bbUI" %>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%> 

<% 
	CommonUtil util = CommonUtil.getInstance();
%>


<spring:message code="common.userHistory.list.column0.label" var="label0" />
<spring:message code="common.userHistory.list.column1.label" var="label1" />
<spring:message code="common.userHistory.list.column2.label" var="label2" />
<spring:message code="common.userHistory.list.column3.label" var="label3" />
<spring:message code="common.userHistory.list.column4.label" var="label4" />
<spring:message code="common.userHistory.list.column5.label" var="label5" />
<spring:message code="common.userHistory.list.column6.label" var="label6" />
<spring:message code="common.userHistory.list.column7.label" var="label7" />
<spring:message code="common.userHistory.actionPanelButton.excel" var="excel" />
<spring:message code="common.userHistory.excel.column" var="excelColumn" />
  
  
<bbNG:learningSystemPage ctxId="ctx" hideCourseMenu="{requestScope.hideCourseMenu}"  hideEditToggle="{requestScope.hideEditToggle}">

<bbNG:pageHeader>
	<bbNG:breadcrumbBar>
		<bbNG:breadcrumb href="${pageContext.request.contextPath}/index?course_id=${param.course_id}">
			<spring:message code="index.pageTitle"/>
		</bbNG:breadcrumb>
	<c:if test="${not fn:contains(role, 'STUDENT')}">
		<bbNG:breadcrumb href="${pageContext.request.contextPath}/stats/view?course_id=${param.course_id}">
			<spring:message code="common.history.breadcrumb.title"/>
		</bbNG:breadcrumb>
	</c:if>
		<bbNG:breadcrumb>
			<spring:message code="common.userHistory.breadcrumb.title"/>
		</bbNG:breadcrumb>
	</bbNG:breadcrumbBar>
	
	<bbNG:pageTitleBar>
		<spring:message code="common.userHistory.pageTitle"/>
	</bbNG:pageTitleBar>
</bbNG:pageHeader>

<bbNG:actionControlBar >
	<bbNG:actionButton  title="${excel}" 
		url="${pageContext.request.contextPath}/excel/view-user?course_id=${param.course_id}&user_id=${param.user_id}&column=${excelColumn}"  
		primary="true" />
	<bbNG:actionPanelButton type="SEARCH" alwaysOpen="true">
		<spring:message code="common.userHistory.actionPanelButton.desc"  />
	</bbNG:actionPanelButton>
</bbNG:actionControlBar>	
	
	<!-- list start -->
	<bbNG:inventoryList className="java.util.TreeMap" collection="${resultList}" objectVar="userHistory">
		
		<bbNG:listElement name="userId" label="${label0}" isRowHeader="true">
			<bbNG:beanComparator property="userId" />${param.user_id}
		</bbNG:listElement>
		
		<bbNG:listElement name="title" label="${label1}"> 
			${userHistory.folder} - ${userHistory.title}
		</bbNG:listElement>
		
		<bbNG:listElement name="WatchingTime" label="${label2}">
			<div class="user">
		<c:choose>			
			<c:when test="${userHistory.cumulative_time eq null}">0</c:when>
			<c:otherwise>
				<a href="${pageContext.request.contextPath}/stats/view-user-detail?course_id=${param.course_id}&user_id=${userHistory.user_id}&pk1=${userHistory.online_content_pk1}" >
					<%=util.secondToTime(userHistory.get("cumulative_time").toString())%>
				</a>
			</c:otherwise>
		</c:choose>
			</div>
		</bbNG:listElement>
		
		<bbNG:listElement name="MinimumTime" label="${label3}">
			<%=util.secondToTime(userHistory.get("minimumtime").toString())%>
		</bbNG:listElement>
		
		<bbNG:listElement name="Duration" label="${label4}">
			<%=util.secondToTime(userHistory.get("duration").toString())%>
		</bbNG:listElement>
		
		<bbNG:listElement name="Percentage" label="${label5}">		
		<fmt:parseNumber var="percentage" type="number" value="${userHistory.percentage}" />
		<c:choose>			
			<c:when test="${percentage eq null || percentage eq 0}">0</c:when>
			<c:otherwise><fmt:formatNumber value="${percentage}" pattern=".00"/></c:otherwise>
		</c:choose>
		</bbNG:listElement>
		
		<bbNG:listElement name="Status" label="${label6}">
		<c:choose>
			<c:when test="${userHistory.pass eq 1}">P</c:when>
			<c:otherwise>F</c:otherwise>
	   	</c:choose>
		</bbNG:listElement>
		
	</bbNG:inventoryList>
	<!-- list end -->

<bbNG:okButton url="javascript:history.back();"/>
</bbNG:learningSystemPage>

