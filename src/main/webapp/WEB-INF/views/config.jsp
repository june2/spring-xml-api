<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/bbUI" prefix="bbUI" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<spring:message code="config.pageTitleBar.title" var="pageTitleBar" htmlEscape="false"/>
<spring:message code="config.breadcrumb.title" var="breadcrumb" />
<spring:message code="config.step1.title" var="step1Title" />
<spring:message code="config.step1.sub1" var="step1sub1" />
<spring:message code="config.step1.sub2" var="step1sub2" />

<spring:message code="config.step2.title" var="step2Title" />
<spring:message code="config.step2.sub1" var="step2sub1" />
<spring:message code="config.step2.sub2" var="step2sub2" />
<spring:message code="config.step2.sub3" var="step2sub3" />

<spring:message code="config.step3.title" var="step3Title" />
<spring:message code="config.step3.sub1" var="step3sub1" />
<spring:message code="config.step3.sub2" var="step3sub2" />
<spring:message code="config.step3.sub3" var="step3sub3" />
<spring:message code="config.step3.sub4" var="step3sub4" />

<spring:message code="config.step4.title" var="step4Title" />
<spring:message code="config.step4.sub1" var="step4sub1" />

<spring:message code="config.step5.title" var="step5Title" />
<spring:message code="config.step5.sub1" var="step5sub1" />
<spring:message code="config.step5.sub2" var="step5sub2" />
<spring:message code="config.step5.sub3" var="step5sub3" />
<spring:message code="config.step5.sub4" var="step5sub4" />
<spring:message code="config.step5.sub5" var="step5sub5" />
<spring:message code="config.step5.sub6" var="step5sub6" />
<spring:message code="config.step5.sub7" var="step5sub7" />
<spring:message code="config.step5.sub8" var="step5sub8" />

<bbNG:learningSystemPage>
	<!-- css -->
	<bbNG:cssBlock>
	</bbNG:cssBlock>

	<!-- javascript -->
	<bbNG:jsFile href="js/jquery.js" />
	<bbNG:jsBlock>
		<script type="text/javascript">		
			// scheduler form
			function stopScheduler(){
				document.stopSchedulerFrm.submit();
			}
			
			function initScheduler(){
				document.initSchedulerFrm.syncPrd.value = document.getElementById("syncPrd").value;
				document.initSchedulerFrm.cutoff.value = document.getElementById("cutoff").value;
				document.initSchedulerFrm.dbType.value = document.getElementById("dbType").value;
				document.initSchedulerFrm.submit();
			}
			
			
			function isNull(obj){
				 return (typeof obj != "undefined" && obj!=null && obj !="" &&obj !="0.0" &&obj !="0.00") ? false : true ;
			}
		</script>
	</bbNG:jsBlock>

	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="${pageTitleBar}" />
		<!-- Configure OnlineAttendance -->
		<bbNG:breadcrumbBar environment="sys_admin" navItem="admin_plugin_manage">
			<bbNG:breadcrumb title="${breadcrumb}" />
			<!-- Configure AttendantManagementSystem -->
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>
	
	<form method="post" name="initSchedulerFrm" action="${pageContext.request.contextPath}/admin/initScheduler">
		<input type="hidden" name="syncPrd" value="${configMap.syncPrd}"/>
		<input type="hidden" name="cutoff" value="${configMap.cutoff}"/>
		<input type="hidden" name="dbType" value="${configMap.dbType}"/>
		<input type="hidden" name="applicationKey" value="${configMap.applicationKey}"/>
		<input type="hidden" name="servername" value="${configMap.servername}"/>
		<input type="hidden" name="provider" value="${configMap.provider}"/>
		<input type="hidden" name="panoptoId" value="${configMap.panoptoId}"/>
		<input type="hidden" name="panoptoPwd" value="${configMap.panoptoPwd}"/>
	</form>
	
	<form method="post" name="stopSchedulerFrm" action="${pageContext.request.contextPath}/admin/stopScheduler">
	</form>

	<form:form method="post" name="configForm">
		<bbNG:dataCollection>

			<%-- <bbNG:step title="${step1Title}">					
				<div>
					<ol>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step1sub1}
							</div>
							<div class="field">
								<input name="bounceUrl" type="text" value="https://${pageContext.request.serverName}${pageContext.request.contextPath}/sso" size="120" readonly="readonly" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step1sub2}
							</div>
							<div class="field">
								<input name="instance" type="text" value="${configMap.instance}" size="120" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step1sub2}
							</div>
							<div class="field">
								<input name="applicationKey" type="text" value="${configMap.applicationKey}" size="120" />
							</div>
						</li>						
					</ol>
				</div>
			</bbNG:step> --%>
			<%-- <bbNG:step title="${step2Title}">
				<div>
					<ol>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step2sub1}
							</div>
							<div class="field">
								<input name="secretKey" type="text" value="${configMap.secretKey}" size="120" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step2sub2}
							</div>
							<div class="field">
								<input name="clientId" type="text" value="${configMap.clientId}" size="120" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step2sub3}
							</div>
							<div class="field">
								<input name="ip" type="text" value="${configMap.ip}" size="120" />
							</div>
						</li>
					</ol>
				</div>
			</bbNG:step> --%>
			<bbNG:step title="${step3Title}">
				<div>
					<ol>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step3sub1}
							</div>
							<div class="field">
								<input name="servername" type="text" value="${configMap.servername}" size="120" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step3sub2}
							</div>
							<div class="field">
								<input name="provider" type="text" value="${configMap.provider}" size="120" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step3sub3}
							</div>
							<div class="field">
								<input name="panoptoId" type="text" value="${configMap.panoptoId}" size="120" />
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step3sub4}
							</div>
							<div class="field">
								<input name="panoptoPwd" type="password" value="${configMap.panoptoPwd}" size="120" />
							</div>
						</li>
					</ol>
				</div>
			</bbNG:step>
			<%-- <bbNG:step title="${step4Title}">
				<div>
					<ol>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step4sub1}
							</div>
							<div class="field">
								<input name="listPrd" type="text" value="${configMap.listPrd}" size="120" />
							</div>
						</li>
					</ol>
				</div>
			</bbNG:step> --%>
			<bbNG:step title="${step5Title}">
				<div>
					<ol>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step5sub8}
							</div>
							<div class="field">
						<c:choose>
							<c:when test="${isRunning}">
								<input type="hidden" name="dbType" value="${configMap.dbType}"/>								
								${configMap.dbType}
							</c:when>
							<c:otherwise>
								<input name="dbType" id="dbType" type="text" value="${configMap.dbType}" size="120" />
							</c:otherwise>
						</c:choose>		
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step5sub1}
							</div>
							<div class="field">
						<c:choose>
							<c:when test="${isRunning}">
								<input type="hidden" name="cutoff" value="${configMap.cutoff}"/>								
								${configMap.cutoff}
							</c:when>
							<c:otherwise>
								<input name="cutoff" id="cutoff" type="text" value="${configMap.cutoff}" size="120" />
							</c:otherwise>
						</c:choose>		
							</div>
						</li>
						<li class="required">
							<div class="label">
								<img src="/images/ci/icons/required.gif" />${step5sub2}
							</div>
							<div class="field">
						<c:choose>
							<c:when test="${isRunning}">		
								<input type="hidden" name="syncPrd" value="${configMap.syncPrd}"/>														
								${configMap.syncPrd}
							</c:when>
							<c:otherwise>
								<input name="syncPrd" id="syncPrd" type="text" value="${configMap.syncPrd}" size="120" />
							</c:otherwise>
						</c:choose>		
							</div>
						</li>
						<li class="required" style="margin-left:13px;">
							<div class="label">${step5sub3}</div>
							<div class="field" style="margin-left:-13px;">${schedulerMap.initTime} </div>
						</li>
						<li class="required" style="margin-left:13px;">
							<div class="label">${step5sub4}</div>
							<div class="field" style="margin-left:-13px;">${schedulerMap.lastTime}</div>
						</li>
						<li class="required" style="margin-left:13px;">
							<div class="label">${step5sub5}</div>
							<div class="field" style="margin-left:-13px;">
						<c:choose>
							<c:when test="${isRunning}">
								<input class="submit button-1" type="button" value="${step5sub6}" onclick="stopScheduler();">
							</c:when>
							<c:otherwise>
								<input class="submit button-1" type="button" value="${step5sub7}" onclick="initScheduler();">
							</c:otherwise>
						</c:choose>								
							</div>
						</li>
					</ol>
				</div>
			</bbNG:step>


			<bbNG:stepSubmit />

		</bbNG:dataCollection>
	</form:form>

</bbNG:learningSystemPage>