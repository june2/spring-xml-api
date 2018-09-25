<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- checkGuest="Y" , since this tool can be made available to course guest and guest users and tool availability is checked rather than using entitlement-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false"%>

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<spring:message code="common.history.list.column0.label" var="label0" />
<spring:message code="common.history.list.column1.label" var="label1" />
<spring:message code="common.history.list.column2.label" var="label2" />
<spring:message code="common.history.list.column3.label" var="label3" />
<spring:message code="common.history.list.column4.label" var="label4" />
<spring:message code="common.history.list.column5.label" var="label5" />
<spring:message code="common.history.list.column6.label" var="label6" />
<spring:message code="common.history.list.desc" var="listDesc" />
<spring:message code="common.history.actionPanelButton.excel" var="excel" />
<spring:message code="common.history.excel.column" var="excelColumn" />
<spring:message code="common.history.allContentCnt" var="allContentCnt" />


<bbNG:learningSystemPage ctxId="ctx" hideCourseMenu="{requestScope.hideCourseMenu}" hideEditToggle="{requestScope.hideEditToggle}">

	<bbNG:pageHeader>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb href="${pageContext.request.contextPath}/index?course_id=${param.course_id}">
				<spring:message code="index.pageTitle" />
			</bbNG:breadcrumb>
			<bbNG:breadcrumb>
				<spring:message code="common.history.breadcrumb.title" />
			</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>
			<spring:message code="common.history.pageTitle" />
		</bbNG:pageTitleBar>
	</bbNG:pageHeader>

	<bbNG:actionControlBar>
		<bbNG:actionButton title="${excel}" 
			url="${pageContext.request.contextPath}/excel/view?course_id=${param.course_id}&column=${excelColumn}" 
			primary="true" />
		<bbNG:actionPanelButton type="SEARCH" alwaysOpen="true">
			<spring:message code="common.history.action.desc" />
		</bbNG:actionPanelButton>
	</bbNG:actionControlBar>

	<div>
		<b>${allContentCnt}</b>${resultMap.total} 
	</div>
		
	<!-- list start -->
	<bbNG:inventoryList description="${listDesc}" className="java.util.TreeMap" collection="${resultList}" objectVar="history">
			
		<bbNG:listElement name="userId" label="${label1}" isRowHeader="true">
			<bbNG:beanComparator property="userId" />
			<div class="user">
				<a href="${pageContext.request.contextPath}/stats/view-user?course_id=${param.course_id}&user_id=${history.user_id}" >
					${history.user_id}
				</a>
			</div>
		</bbNG:listElement>
			
		<bbNG:listElement name="lastName" label="${label2}">
			<bbNG:beanComparator property="lastName" />
			${history.lastname}
		</bbNG:listElement>
			
		<bbNG:listElement name="firstName" label="${label3}">
			<bbNG:beanComparator property="firstName" />
			${history.firstname}
		</bbNG:listElement>
			
		<bbNG:listElement name="Overall Percentage" label="${label4}">
			<fmt:parseNumber var="total" type="number" value="${resultMap.total}" />
		 	<fmt:parseNumber var="pass" type="number" value="${history.pass}" />
		 	<fmt:parseNumber var="percentage" type="number" value="${(pass/total)*100}" />		
		<c:choose>			 	
		 	<c:when test="${percentage eq 0}">0</c:when>
			<c:otherwise><fmt:formatNumber value="${percentage}" pattern=".00"/></c:otherwise>
		</c:choose>					 			 
		</bbNG:listElement>
			
		<bbNG:listElement name="Count Pass" label="${label6}">
		<c:choose><c:when test="${history.pass eq null}">0</c:when>
			<c:otherwise>${history.pass}</c:otherwise></c:choose>	 / ${resultMap.total}
		</bbNG:listElement>
			
		<bbNG:listElement name="Overall Status" label="${label5}">
			<c:choose>
				<c:when test="${percentage >= cutoff}">P</c:when>
				<c:otherwise>F</c:otherwise>
		   </c:choose>		
		</bbNG:listElement>
		
	</bbNG:inventoryList>
	<!-- list end -->
		
	<bbNG:okButton url="javascript:history.back();" />
</bbNG:learningSystemPage>






