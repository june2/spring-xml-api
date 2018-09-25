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

<spring:message code="index.pageTitle" var="pageTitle" htmlEscape="false" />
<spring:message code="index.alert" var="pageAlert" />
<spring:message code="index.contentList.item1.title" var="item1Title" />
<spring:message code="index.contentList.item1.desc" var="item1Desc" />
<spring:message code="index.contentList.item2.title" var="item2Title" />
<spring:message code="index.contentList.item2.desc" var="item2Desc" />

<spring:message code="index.contentList.select1.title" var="select1Title" />
<spring:message code="index.contentList.select1.item1" var="select1Item1" />
<spring:message code="index.contentList.select1.item2" var="select1Item2" />
<spring:message code="index.contentList.select2.title" var="select2Title" />
<spring:message code="index.contentList.select3.title" var="select3Title" />


<bbNG:learningSystemPage ctxId="ctx" hideCourseMenu="{requestScope.hideCourseMenu}" hideEditToggle="{requestScope.hideEditToggle}">
	
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb href="index">
				<spring:message code="index.breadcrumb.title" />
			</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar title="${pageTitle }" titleColor="black" />
	</bbNG:pageHeader>
		
	
<c:choose>
	<c:when test="${empty folders}">
		<div>empty folders. create folder in Panopto</div>
	</c:when>
	<c:otherwise>
		<ul id="content_listContainer" class="contentList">
			<c:forEach var="folder" items="${folders}" varStatus="index">
			<li class="clearfix read">
				<img alt="Content Folder" src="/images/ci/sets/set01/folder_on.gif" class="item_icon">
				<div class="item clearfix">
					<h3>
						<a href="${pageContext.request.contextPath}/panopto-sessionsById?course_id=${course_id}&menuNo=${param.menuNo}&weekNo=${param.weekNo}&folderId=${folder.getId()}&toc_pk1=${param.toc_pk1}&folder_pk1=${param.folder_pk1}&userId=${param.userId}">
							<span style="color: #000000;">${folder.getName()}</span>
						</a>
					</h3>
				</div>
				<div class="details">
					<div class="vtbegenerateXd">
						<p>${folder.getDescription()}</p>
					</div>
				</div>
				<div class="moduleSample"></div>
			</li>
			</c:forEach>
		</ul>
	</c:otherwise>
</c:choose>
	
	

</bbNG:learningSystemPage>
