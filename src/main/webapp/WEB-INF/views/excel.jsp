<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
//MS excel로 다운로드/실행, filename에 저장될 파일명을 적어준다.
/* String title = request.getParameter("title").toString(); */
String title = request.getAttribute("title").toString();
String docName = URLEncoder.encode(title,"UTF-8"); // UTF-8로 인코딩

response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls;"); 
response.setHeader("Content-Description", "JSP Generated Data");
response.setHeader("Content-Type", "application/octet-stream");
response.setHeader("Content-Transfer-Encoding", "binary;");
response.setHeader("Pragma", "no-cache;");
response.setHeader("Expires", "-1;");

// ↓ 이걸 풀어주면 열기/저장 선택창이 뜨는 게 아니라 그냥 바로 저장된다.
//response.setContentType("application/vnd.ms-excel");
%>
<html lang="ko">
<head>
<title><%=title%></title>
</head>
<body> 
	<table border="1">
		<thead>
			<tr>
			<c:forEach var="list" items="${columnList}">
				<th>${list}</th>
			</c:forEach>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="list" items="${resultList}" varStatus="status">
			<tr align="center">
			<c:forEach var="map" items="${list}" varStatus="status">
				<td>${map.value}</td>
			</c:forEach>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>