<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- checkGuest="Y" , since this tool can be made available to course guest and guest users and tool availability is checked rather than using entitlement-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>


<spring:message code="panopto.imported.pageTitle" var="pageTitle"/>
<spring:message code="panopto.imported.list.description" var="description"/>
<spring:message code="panopto.imported.actionButton" var="actionButton" />
<spring:message code="panopto.imported.button" var="button" />
<spring:message code="panopto.imported.deleteButton" var="deleteButton" />
<spring:message code="panopto.imported.list.description" var="desc" />
<spring:message code="panopto.imported.list.column0.label" var="label0" />
<spring:message code="panopto.imported.list.column1.label" var="label1" />
<spring:message code="panopto.imported.list.column2.label" var="label2" />
<spring:message code="panopto.imported.list.column3.label" var="label3" />
<spring:message code="panopto.imported.list.column4.label" var="label4" />
<spring:message code="panopto.imported.list.column4.value.notcreated" var="notcreated"/>
<spring:message code="panopto.imported.list.column4.value.created" var="created"/>
<spring:message code="panopto.imported.start_date" var="start_date" />
<spring:message code="panopto.imported.end_date" var="end_date" />
<spring:message code="panopto.imported.h" var="hr" />
<spring:message code="panopto.imported.m" var="min" />
<spring:message code="panopto.imported.s" var="sec" />


<bbNG:learningSystemPage>

	<!-- css -->
	<bbNG:cssFile href="${pageContext.request.contextPath}/css/jquery-ui.css"/>
	<bbNG:cssFile href="${pageContext.request.contextPath}/css/jquery-ui-timepicker-addon.css"/>
	
	<!-- javascript -->
	<bbNG:jsFile href="js/jquery.js" />
	<bbNG:jsFile href="js/jquery-ui.js" />
	<bbNG:jsFile href="js/jquery-ui-timepicker-addon.js" />
	<bbNG:jsBlock>
	<script>$.noConflict();</script>
	<script type="text/javascript">
	jQuery(document).ready(function($){
				
		$.each(${pk1List},function(index,value){
			
			var st = document.getElementById( "start_date"+value );
			var end = document.getElementById( "end_date"+value );
		
			$(st).datetimepicker({
				dateFormat : "yy-mm-dd",
			  	defaultDate: "+1w",
				onClose: function( selectedDate ) {
					$(end).datepicker( "option", "minDate", selectedDate );
			  	}				
			});
						
			$(end).datetimepicker({
				dateFormat : "yy-mm-dd",
			  	defaultDate: "+1w",
				hour: 23,
			  	minute: 59,
			  	second :59,
				onClose: function( selectedDate ) {
					$(st).datepicker( "option", "maxDate", selectedDate );
			  	}				
			});		
		}); // end each			
	});	

	function createContents(){

		var inputElements = document.getElementsByName('chk');
		var checkedlist = [];
		
		for(var i=0; inputElements[i]; ++i){
			
			if(inputElements[i].checked){
				
				var dHour = document.getElementById('dHour'+inputElements[i].value).value;
				var dMin = document.getElementById('dMin'+inputElements[i].value).value;
				var dSec = document.getElementById('dSec'+inputElements[i].value).value;
				var dTime = parseFloat(dHour) * 3600 + parseFloat(dMin) *60 + parseFloat(dSec);
				
				var mHour = document.getElementById('mHour'+inputElements[i].value).value;
				var mMin = document.getElementById('mMin'+inputElements[i].value).value;
				var mSec = document.getElementById('mSec'+inputElements[i].value).value;
				var mTime = parseFloat(mHour) * 3600 +parseFloat(mMin) *60 + parseFloat(mSec);
				
				
				var stDate =  document.getElementById('start_date'+inputElements[i].value);
	  	    		var endDate =  document.getElementById('end_date'+inputElements[i].value);
	    		
		    		if(isNull(mTime)|| isNull(stDate.value) ||isNull(endDate.value) || stDate.value=='시작일' || stDate.value=='종료일' ){
					alert('시간을 입력하세요. Please input the time.'); return false;
				}
		    		
		    		if(parseFloat(mTime) > parseFloat(dTime)){
		    			alert('학습시간은 최대시간보다 작거나 같아야합니다.  Minimum time should be less or same than the duration.');
		    			return false;
		    		}
	    		
	    	 		checkedlist.push(inputElements[i].value);
	      	}
			
		}
		
		if(isNull(checkedlist)){
			alert('적어도 하나 이상의 컨텐츠를 선택하세요.(At least, please select over one content.)'); 
			return false;
		}
				
		document.frm.checkedlist.value = checkedlist;
		document.frm.submit();
	}


	function deleteContents(){

		var inputElements = document.getElementsByName('chk');
		var crs = document.getElementById('crsid').value;
		var checkedlist = [];
		
		for(var i=0; inputElements[i]; ++i){
		      if(inputElements[i].checked){
		    	  	checkedlist.push(inputElements[i].value);
		      }
		}		
		
		if(isNull(checkedlist)){
			alert('적어도 하나 이상의 컨텐츠를 선택하세요.(At least, please select over one content.)'); return false;
		}else{
			if(confirm('삭제할 경우 관련된 출결데이터도 삭제됩니다. 이 동작은 최종 실행이며 취소될 수 없습니다. 제거하시겠습니까?')==true){
				document.frm.checkedlist.value = checkedlist;				
				document.frm.method = 'DELETE';
				document.frm.action = '${pageContext.request.contextPath}/delete-contents';
				document.frm.submit();				
			}else{
				return false;
			} // end if	
		} // end if
	}
	

	function isNull(obj){
		 return (typeof obj != "undefined" && obj!=null && obj !="" &&obj !="0.0" &&obj !="0.00") ? false : true ;
	}
</script>
	</bbNG:jsBlock>


	<bbNG:pageHeader>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb href="${pageContext.request.contextPath}/index?course_id=${param.course_id}">
				<spring:message code="index.pageTitle" />
			</bbNG:breadcrumb>
			<bbNG:breadcrumb href="/webapps/blackboard/content/listContentEditable.jsp?content_id=${contentParentId}&course_id=${param.course_id}&mode=reset">
				<spring:message code="${tocTitle}" />
			</bbNG:breadcrumb>
			<bbNG:breadcrumb href="/webapps/blackboard/content/listContentEditable.jsp?content_id=${contentId}&course_id=${param.course_id}&mode=reset">
				<spring:message code="${contentTitle}" />
			</bbNG:breadcrumb>
			<bbNG:breadcrumb>
				${pageTitle}
			</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>

		<bbNG:pageTitleBar>${pageTitle}</bbNG:pageTitleBar>
	</bbNG:pageHeader>

	<bbNG:form name="frm" method="post" action="${pageContext.request.contextPath}/create-contents">
		<bbNG:actionControlBar>
			<bbNG:actionButton title="${actionButton}" 
							   url="${pageContext.request.contextPath}/panopto-folders?course_id=${param.course_id}&menuNo=${param.menuNo}&weekNo=${param.weekNo}&folderId=${folder.getId()}&toc_pk1=${toc_pk1}&folder_pk1=${folder_pk1}&userId=${userId}" 
							   primary="true" />
			<bbNG:actionPanelButton type="SEARCH" alwaysOpen="true">${description}</bbNG:actionPanelButton>
		</bbNG:actionControlBar>

		<bbNG:hiddenElement name="course_id" value="${param.course_id }" />
		<bbNG:hiddenElement name="crsid" value="${param.course_id }" />
		<bbNG:hiddenElement name="weekNo" value="${param.weekNo }" />
		<bbNG:hiddenElement name="contentTitle" value="" />
		<bbNG:hiddenElement name="contentUrl" value="" />
		<bbNG:hiddenElement name="contentDuration" value="" />
		<bbNG:hiddenElement name="checkedlist" value="" />
		<bbNG:hiddenElement name="type" value="xin" />


		<bbNG:inventoryList className="com.blackboard.consulting.model.ContentVO" collection="${contentList}" objectVar="contents">
			<jsp:useBean id="now" class="java.util.Date" />
			<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" var="today" />
			
			<c:choose>
				<c:when test="${isdelete eq true && contents.start_date < today &&  null ne contents.start_date }">
					<bbNG:listCheckboxElement showCheckbox="false" name="chk" value="${contents.pk1}" />
				</c:when>
				<c:otherwise>
					<bbNG:listCheckboxElement name="chk" value="${contents.pk1}" />
				</c:otherwise>
			</c:choose>


			<bbNG:listActionBar>
				<bbNG:listActionItem title="${button }" url="javascript:createContents();" />
				<bbNG:listActionItem title="${deleteButton }" url="javascript:deleteContents();" />
			</bbNG:listActionBar>

			<bbNG:listElement name="location" label="${label0 }">
				<c:choose>
					<c:when test="${contents.getAncestor_title() ne null}">
						${fn:replace(fn:replace(contents.getAncestor_title(),"COURSE_DEFAULT.",""),".CONTENT_LINK.label","")}&nbsp;>&nbsp;
						${contents.getParent_title()}
					</c:when>
				</c:choose>
			</bbNG:listElement>

			<bbNG:listElement name="title" label="${label1 }" isRowHeader="true">
				<bbNG:beanComparator property="title" />
					${contents.getTitle()}
				<%-- <c:choose>
					<c:when test="${contents.getContent_title() eq null}">${contents.getTitle()}</c:when>
					<c:otherwise>${contents.getContent_title()}</c:otherwise>
				</c:choose> --%>
			</bbNG:listElement>


			<bbNG:listElement name="status" label="${label4 }">
				<c:choose>
					<c:when test="${contents.getContent_pk1() eq '' || null eq contents.start_date }">
						<span>${notcreated}</span>
						<bbNG:hiddenElement name="status${contents.pk1}" id="status${contents.pk1}" value="" />
					</c:when>
					<c:otherwise>
						<span style="color: blue">${created}</span>
						<bbNG:hiddenElement name="status${contents.pk1}" id="status${contents.pk1}" value="setted" />
					</c:otherwise>
				</c:choose>
			</bbNG:listElement>


			<bbNG:listElement name="mintime" label="${label2 }">

				<bbNG:selectElement id="mHour${contents.getPk1()}" name="mHour${contents.getPk1()}">
					<bbNG:selectOptionElement value="0" optionLabel="${hr }" />
					<c:choose>
						<c:when test="${contents.getdHour() eq 0}">
							<bbNG:selectOptionElement value="0" optionLabel="0" isSelected="true" disable="true" />
						</c:when>
						<c:otherwise>
							<c:forEach var="mm" begin="0" end="60" step="1">
								<c:choose>
									<c:when test="${contents.getmHour() eq mm}">
										<bbNG:selectOptionElement value="${mm }" optionLabel="${mm }" isSelected="true" />
									</c:when>
									<c:otherwise>
										<bbNG:selectOptionElement value="${mm }" optionLabel="${mm }" />
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</bbNG:selectElement>${hr }
	
				<bbNG:selectElement id="mMin${contents.getPk1()}" name="mMin${contents.getPk1()}">
					<bbNG:selectOptionElement value="0" optionLabel="${min }" />
					<c:choose>
						<c:when test="${contents.getdMin() eq 0 && contents.getdHour() eq 0}">
							<bbNG:selectOptionElement value="0" optionLabel="0" isSelected="true" disable="true" />
						</c:when>
						<c:otherwise>
							<c:forEach var="mm" begin="0" end="60" step="1">
								<c:choose>
									<c:when test="${contents.getmMin() eq mm}">
										<bbNG:selectOptionElement value="${mm }" optionLabel="${mm }" isSelected="true" />
									</c:when>
									<c:otherwise>
										<bbNG:selectOptionElement value="${mm }" optionLabel="${mm }" />
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</bbNG:selectElement>${min }
			
				<bbNG:hiddenElement id="mSec${contents.getPk1()}" name="mSec${contents.getPk1()}" value="0" />
				<bbNG:hiddenElement id="content_pk1${contents.pk1}" name="content_pk1${contents.pk1}" value="${contents.getContent_pk1()}" />
				<bbNG:hiddenElement id="title${contents.pk1}" name="title${contents.pk1}" value="${contents.getContent_title()}" />
				<%-- <bbNG:selectElement id="mSec${contents.getPk1()}" name="mSec${contents.getPk1()}">
					<bbNG:selectOptionElement value="0" optionLabel="${sec }" />
					<c:choose>
						<c:when test="${contents.getdSec() eq 0 && contents.getdMin() eq 0 && contents.getdHour() eq 0}">
							<bbNG:selectOptionElement value="0" optionLabel="0" isSelected="true" disable="true" />
						</c:when>
						<c:otherwise>
							<c:forEach var="ms" begin="0" end="60" step="1">
								<c:choose>
									<c:when test="${contents.getmSec() eq ms}">
										<bbNG:selectOptionElement value="${ms }" optionLabel="${ms }" isSelected="true" />
									</c:when>
									<c:otherwise>
										<bbNG:selectOptionElement value="${ms }" optionLabel="${ms }" />
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</bbNG:selectElement>${sec } --%>
			
			</bbNG:listElement>

			<bbNG:listElement name="duration" label="${label3 }">
				<bbNG:textElement name="dHour${contents.getPk1()}" displayOnly="true" value="${contents.getdHour()}" /> 시간 
				<bbNG:textElement name="dMin${contents.getPk1()}" displayOnly="true" value="${contents.getdMin()}" /> 분
				<bbNG:hiddenElement name="dSec${contents.getPk1()}" value="0" />
				<%-- <bbNG:textElement name="dSec${contents.getPk1()}" displayOnly="true" value="${contents.getdSec()}" />초 --%>			
			</bbNG:listElement>

			<bbNG:listElement name="st_prd" label="${start_date }">
				<input type="text" id="start_date${contents.pk1}" name="start_date${contents.pk1}" 
						value="<c:choose>
									<c:when test="${empty contents.start_date}">${period.start_date}</c:when>
									<c:otherwise>${contents.start_date}</c:otherwise>
						   	  </c:choose>" />
			</bbNG:listElement>
			<bbNG:listElement name="end_prd" label="${end_date }">
				<input type="text" id="end_date${contents.pk1}" name="end_date${contents.pk1}"
						value="<c:choose>
									<c:when test="${empty contents.end_date}">${period.end_date}</c:when>
									<c:otherwise>${contents.end_date}</c:otherwise>
							   </c:choose>" />
			</bbNG:listElement>
			
		</bbNG:inventoryList>
	</bbNG:form>
	<bbNG:okButton url="${pageContext.request.contextPath}/index?course_id=${course_pk}" />
</bbNG:learningSystemPage>



