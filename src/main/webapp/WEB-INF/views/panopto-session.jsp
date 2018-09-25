<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- checkGuest="Y" , since this tool can be made available to course guest and guest users and tool availability is checked rather than using entitlement-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
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

<!-- javascript -->
<bbNG:jsFile href="/javascript/jquery/jquery.js" />
<bbNG:jsBlock>
<script>$.noConflict();</script>	
<script type="text/javascript">
	jQuery(document).ready(function($){	
		
	});		
	
	function validateForm(name, duration, url){
		
		document.frm.title.value = name;
		document.frm.duration.value = duration;
		document.frm.view_url.value = url;

		document.frm.action = "${pageContext.request.contextPath}/panopto-import";
		document.frm.submit();
	}	
	
	function isNull(obj){
		 return (typeof obj != "undefined" && obj!=null && obj !="" &&obj !="0") ? false : true ;
	}
</script>
</bbNG:jsBlock>
	
	<!-- Panopto content parameter -->
	<form name="frm" method="post">
		<input name="title" type="hidden" value="" />
		<input name="duration" type="hidden" value="" />
		<input name="view_url" type="hidden" value="" />
		<input name="course_id" type="hidden" value="${param.course_id}" />		
		<input name="toc_pk1" type="hidden" value="${param.toc_pk1}" />
		<input name="folder_pk1" type="hidden" value="${param.folder_pk1}" />
		<input name="menuNo" type="hidden" value="${param.menuNo}" />
		<input name="userId" type="hidden" value="${param.userId}" />		
	</form>
	
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb href="index">
				<spring:message code="index.breadcrumb.title" />
			</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar title="${pageTitle}" titleColor="black" />
	</bbNG:pageHeader>


<c:choose>
	<c:when test="${empty sessions}">
		<div>empty sessions. create session in Panopto</div>
	</c:when>
	<c:otherwise>
		<ul id="content_listContainer" class="contentList">
			<c:forEach var="session" items="${sessions}" varStatus="index">
			<li class="clearfix read" >
				<img alt="Content Folder" src="https://${servername}${session.getThumbUrl()}" class="item_icon" style="width:120px;">
				<div class="item clearfix">
					<h3  style="padding-left:75px;">					
						<a href="#" onclick="javascript:validateForm('${session.getName()}', '${session.getDuration()}', '${session.getId()}');">
							<span style="color: #000000;">${session.getName()}</span>
						</a>
					</h3>
				</div>
				<div class="details">
					<div class="vtbegenerateXd" style="padding-left:75px;">
						<p>${session.getDescription()}</p>
					</div>
				</div>
				<div class="moduleSample"></div>
			</li>
			</c:forEach>
		</ul>
	</c:otherwise>
</c:choose>

</bbNG:learningSystemPage>