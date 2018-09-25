<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/bbUI" prefix="bbUI" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 

<%@page import="com.blackboard.consulting.util.CommonUtil" %>


<spring:message code="configWeek.pageTitleBar.title" var="pageTitleBar" htmlEscape="false"/>
<spring:message code="configWeek.pageTitleBar.title" var="breadcrumb" />
<spring:message code="configWeek.step1.title" var="step1" />
<spring:message code="configWeek.createContents.button1" var="button1" />
<spring:message code="configWeek.list1.column0" var="column0" />
<spring:message code="configWeek.list1.column1" var="column1" />
<spring:message code="configWeek.list1.column2" var="column2" />


<bbNG:learningSystemPage entitlement="system.admin.VIEW">

<!-- CSS Block -->
<bbNG:cssFile href="${pageContext.request.contextPath}/css/jquery-ui.css"/>
<bbNG:cssFile href="${pageContext.request.contextPath}/css/jquery-ui-timepicker-addon.css"/>
<bbNG:cssBlock>
	<style type="text/css">
		th, td{
			text-align:center;
		}
	</style>
</bbNG:cssBlock>

<!-- javascript -->
<bbNG:jsFile href="js/jquery.js" />
<bbNG:jsFile href="js/jquery-ui.js" />
<bbNG:jsFile href="js/jquery-ui-timepicker-addon.js" />
<bbNG:jsBlock>
<script>$.noConflict();</script>
<script type="text/javascript">

jQuery(document).ready(function($){

	var size = parseInt('${fn:length(resultList)}');
	var handleName = '/webapps/' + '<%=CommonUtil.pluginHandle %>';	
	
	for(var i=1; i<=size; i++){
		createTimePicker('start_date'+i, 'end_date'+i);
	};
	
	$('#term').change(function(){
        var term = $('#term option:selected').val();
        window.location.href = handleName+"/admin/configWeek?term="+term;
    });
	
	$('#save').click(function(){
		
		var data = [];
		var term = $("#term").val();		
		
		for(var i=1; i<=size; i++){
			var obj = {
					term:term, week:i, 
					start_date:$('#start_date'+i).val().replace('.0', ''), 
					end_date:$('#end_date'+i).val().replace('.0', '')
				}
			data.push(obj);
		};
		
		if(data.length > 0){
			$.ajax({
				type: "post",
				url: handleName+"/admin/configWeek?term="+term,
				/* data: data, */
				dataType: 'json',
			 	headers : {
	                'Accept' : 'application/json',
	                'Content-Type' : 'application/json'
	            },
	            data : JSON.stringify(data),
				success: function(args){
					alert("저장 되었습니다.");
					location.reload();
				}
			    ,error: function(e) {	
			    	alert("오류가 발생했습니다."); 
			    }
			});
		}
		
	});	
	
	$('#addWeek').click(function(){
		
		size += 1;
		
		var html = 
		'<tr>' +
			'<td></td>' +
			'<td>' +size+ '주차</td>' +
			'<td>' +
				'<input type="text" id="start_date' +size+ '">' +
			'</td>' +
			'<td>' +
				'<input type="text" id="end_date' +size+ '">' +
			'</td>' +
		'</tr>';
		
		$('#table_body').append(html);
		createTimePicker('start_date'+size, 'end_date'+size);
		
	});

	
	$('#deleteWeek').click(function(){
		
		var pk1 = $(this).val();
		
		$.ajax({
			type: "delete",
			url: handleName+"/admin/configWeek?term="+term,
			dataType: 'json',
		 	headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
            },
            data : pk1,
			success: function(args){
				alert("삭제 되었습니다.");
				location.reload();
			}
		    ,error: function(e) {	
		    	alert("오류가 발생했습니다."); 
		    }
		});	
	});
	
	
	function createTimePicker(start, end){
		/* var startDateTextBox = $("#"+start);
		var endDateTextBox = $("#"+end);
	
		$.timepicker.datetimeRange(
			startDateTextBox,
			endDateTextBox,
			{
				minInterval: (1000*60*60), // 1hr
				dateFormat: 'yy-mm-dd', 
				timeFormat: 'HH:mm:ss',
				hour: 23,
			  	minute: 59,
			  	second :59,
				start: {}, // start picker options
				end: {} // end picker options					
			}
		); */
		
		var st = $("#"+start);
		var end = $("#"+end);
	
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
		  	second: 59,
		 	onClose: function( selectedDate ) {
				$(st).datepicker( "option", "maxDate", selectedDate );
		  	}
		});
	}
	
});
</script>
</bbNG:jsBlock>

<bbNG:pageHeader>
   	<bbNG:pageTitleBar title="${pageTitleBar}" /> <!-- Configure OnlineAttendance -->
   	<bbNG:breadcrumbBar environment="sys_admin"  navItem="admin_plugin_manage">
		<bbNG:breadcrumb title="${breadcrumb}" /> <!-- Configure AttendantManagementSystem -->
   	</bbNG:breadcrumbBar>
</bbNG:pageHeader>

<div id="dataCollectionContainer">
	
	<div class="submitStepTop">
		<div class="taskbuttondiv_wrapper">
			<p class="taskbuttondiv" >
				<input class="button-2" type="button" value="취소" onclick="history.back();"> 
				<input class="submit button-1" type="button" value="제출" id="save">
			</p>
		</div>
	</div>

	<div id="step1" class=" ">
		<h3 id="steptitle1" class="steptitle">
			<span id="stepnumber1">1.</span> &nbsp; ${step1}
		</h3>
		<div id="stepcontent1" class="stepcontent">
			<div id="listContainer" class="inventoryListContainerDiv" >

				<div class="rumble_top checked_top clearfix">					
					<ul class="nav clearfix" id="listContainer_nav_batch_top">
						<li>
							<select id="term" style="margin-top:3px;">
								<c:forEach items="${termList}" var="obj">
						  			<option value="${obj.pk1}" 
						  			<c:if test="${obj.pk1 eq termPk}">selected</c:if>>
						  			${obj.name}</option>
						  		</c:forEach>
							</select>
						</li>
						<li><a id="addWeek">${button1}</a></li>
					</ul>
				</div>

				<!-- Begin Item Listing -->
				<div>
					<table class="inventory sortable $wrappingTableClass">
						<thead class="inventoryListHead">
							<tr>
								<th scope="col"></th>
								<th scope="col">${column0}</th>
								<th scope="col">${column1}</th>
								<th scope="col">${column2}</th>
							</tr>
						</thead>
						<tbody id="table_body">
							<c:forEach items="${resultList}" var="obj"  varStatus="index">
							<tr>
								<td>
								<c:if test="${index.last}">
									<button type="button" id="deleteWeek" value="${obj.pk1}" >삭제</button>
								</c:if>
								</td>
								<td>${obj.week}주차 </td>
								<td><input type="text" id="start_date${obj.week}" value="${obj.start_date}"></td>
								<td><input type="text" id="end_date${obj.week}" value="${obj.end_date}"></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- End Item Listing -->

			</div>
		</div>
	</div>
	
</div>

</bbNG:learningSystemPage>