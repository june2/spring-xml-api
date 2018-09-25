<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><!-- checkGuest="Y" , since this tool can be made available to course guest and guest users and tool availability is checked rather than using entitlement-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ page session="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/bbUI" prefix="bbUI" %>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%> 

<spring:message code="common.userLogs.list.column0.label" var="label0" />
<spring:message code="common.userLogs.list.column1.label" var="label1" />
<spring:message code="common.userLogs.list.column2.label" var="label2" />
<spring:message code="common.userLogs.list.column3.label" var="label3" />
<spring:message code="common.userLogs.actionPanelButton.excel" var="excel" />
<spring:message code="common.userLogs.excel.column" var="excelColumn" />


<bbNG:learningSystemPage ctxId="ctx" hideCourseMenu="{requestScope.hideCourseMenu}" hideEditToggle="{requestScope.hideEditToggle}">

	<bbNG:pageHeader>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb href="${pageContext.request.contextPath}/index?course_id=${param.course_id}">
				<spring:message code="index.pageTitle" />
			</bbNG:breadcrumb>
			<c:if test="${not fn:contains(role, 'STUDENT')}">
				<bbNG:breadcrumb
					href="${pageContext.request.contextPath}/stats/view?course_id=${param.course_id}">
					<spring:message code="common.history.breadcrumb.title" />
				</bbNG:breadcrumb>
			</c:if>
			<bbNG:breadcrumb href="${pageContext.request.contextPath}/stats/view-user?course_id=${param.course_id}&user_id=${param.user_id}">
				<spring:message code="common.userHistory.breadcrumb.title" />
			</bbNG:breadcrumb>
			<bbNG:breadcrumb>
				<spring:message code="common.userLogs.breadcrumb.title" />
			</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>

		<bbNG:pageTitleBar>
		${title} -	<spring:message code="common.userLogs.pageTitle" />
		</bbNG:pageTitleBar>

	</bbNG:pageHeader>

	<bbNG:actionControlBar>
		<bbNG:actionButton title="${excel}" 
			url="${pageContext.request.contextPath}/excel/view-user-detail?course_id=${param.course_id}&user_id=${param.user_id}&url=${param.url}&column=${excelColumn}&pk1=${param.pk1}" 
			primary="true" />
		<bbNG:actionPanelButton type="SEARCH" alwaysOpen="true">
			<spring:message code="common.userLogs.actionPanel.desc" />
		</bbNG:actionPanelButton>
	</bbNG:actionControlBar>

	<!-- list start -->
	<bbNG:inventoryList className="java.util.TreeMap" collection="${resultList}" objectVar="list">
		
		<bbNG:listElement name="title" label="${label0}" isRowHeader="true">
			<bbNG:beanComparator property="title" />${list.title}
		</bbNG:listElement>
		
		<bbNG:listElement name="watching_date" label="${label1}">
			<bbNG:beanComparator property="watching_date" />${list.watching_date }
		</bbNG:listElement>
		
		<bbNG:listElement name="watching_start" label="${label2}">
			<bbNG:beanComparator property="watching_start" />${list.watching_start}
		</bbNG:listElement>
		
		<bbNG:listElement name="watching_end" label="${label3}">
			<bbNG:beanComparator property="watching_end" />${list.watching_end}
		</bbNG:listElement>

	</bbNG:inventoryList>
	<!-- list end -->

	<bbNG:okButton url="javascript:history.back();" />
</bbNG:learningSystemPage>


