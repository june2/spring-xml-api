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
<%@ page import="java.util.*, java.io.*"%>

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
		if('${param.popup}' == 'true'){
			if(!confirm('${pageAlert}')){
				history.back();
			}
		}
		
		$('#menuNo').change(function(){
			
			if(!isNull(document.getElementById("menuNo").value))
			{
				var menu = document.getElementById("menuNo").value;
				var crsId = document.getElementById("crsBatchUid").value;
				
				$.ajax({
					type : "post",
					url : "app/getWeekList",
					data : {menu:menu, crsId:crsId},
					dataType : 'json',
					success:function(args){
	
						$("#weekNo")
					    .find('option')
					    .remove()
					    .end()
					    .append('<option value="">${select3Title}</option>');
	
						for(var i=0; i<args.weekList.length; i++) {
							$("#weekNo").append("<option value='"+args.weekList[i].weekId+"'>"+args.weekList[i].weekTitle+"</option>");		
						}
					}
				    ,error:function(e) {
				    	alert("error"); 
				    }
				});
			}
		});
		
	});	

	function validateForm(){

		var menu = document.getElementById("menuNo").value;		
		var week = document.getElementById("weekNo").value;	
		/* var contentType = document.getElementById("contentType").value;	
		
		if(isNull(contentType)){	
			alert('${select1Title}'); return false;
		} */
		if(isNull(menu)){	
			alert('${select2Title}'); return false;
		}
		if(isNull(week)){
			alert('${select3Title}'); return false;
		}
		
		document.frm.action = "imported-contents?course_id=${param.course_id}";
		document.frm.submit();
	}
	
	function isNull(obj){
		 return (typeof obj != "undefined" && obj!=null && obj !="" &&obj !="0") ? false : true ;
	}
</script>
</bbNG:jsBlock>
	
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb href="index">
				<spring:message code="index.breadcrumb.title" />
			</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar title="${pageTitle}" titleColor="black" />
	</bbNG:pageHeader>
	
	
	<c:if test="${role eq 'P'}">
	<div id="step1">
		<h3 id="steptitle1" class="steptitle">${item1Title}</h3>
		<form name="frm" method="get" onsubmit="return validateForm()">
		<bbNG:hiddenElement name="course_id" value="${param.course_id}" />
		<bbNG:hiddenElement name="crsBatchUid" value="${crsBatchUid}" />
		<div id="stepcontent1" class="stepcontent">
			<ol>
				<li>
					<span class="stepHelp" id="stephelp1" >${item1Desc}</span>
				</li>
			</ol>
			<ol>
				<li>
				<!-- weekly folder -->
				<%-- <select name="contentType" id="contentType">
					<option value="">${select1Title}</option>
				<c:if test="${configMap.BB eq true and role ne 'S' }">
					<option value="app/bb/selectContents?course_id=${param.course_id}">${select1Item1}</option>
				</c:if>
				<c:if test="${configMap.xinics eq true  and role ne 'S'}">
					<option value="app/xinics/selectContents?course_id=${param.course_id}">${select1Item2}</option>
				</c:if>
				</select> --%>

				<!-- course menu -->
				<bbNG:selectElement name="menuNo" id="menuNo" title="menuNo">
					<bbNG:selectOptionElement value="" optionLabel="${select2Title}" />
					<c:forEach var="toc" items="${tocList }">
						<bbNG:selectOptionElement value="${toc.getId().getExternalString() }" optionLabel="${toc.getTitle() }" />
					</c:forEach>
				</bbNG:selectElement>

				<!-- weekly folder -->
				<select name="weekNo" id="weekNo">
					<option value="">${select3Title}</option>
				</select>
				
				<input class="submit" type="submit" value="Go" >
				</li>
			</ol>
		</div>
		</form>
	</div>
	</c:if>

	<c:if test="${role eq 'P' or role eq 'B' or role eq 'S' or role eq 'T'}">
		<div id="step1">
			<h3 id="steptitle1" class="steptitle">${item2Title }</h3>
			<div id="stepcontent1" class="stepcontent">
				<ol>
					<li>
						<a href="${pageContext.request.contextPath}/stats/view?course_id=${param.course_id}">
							<i>${item2Desc }</i>
						</a>
					</li>
				</ol>
			</div>
		</div>
	</c:if>

	<%-- <bbNG:okButton url="javascript:history.back();"/> --%>
</bbNG:learningSystemPage>