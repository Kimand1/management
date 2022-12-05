<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>      
<table>
	<tr>
		<th>디바이스 아이디</th>
		<th>디바이스상태</th>
		<th>로그시간</th>
		<!-- <th>로그 원본</th> -->
	</tr>
	<c:forEach var="result" items="${lists}" varStatus="status">
		<tr>
			<td>${result.devId }</td>			
			<td>
				<c:if test="${result.broadSttus eq '1' }">디바이스 연결</c:if>
				<c:if test="${result.broadSttus eq '2' }">연결 끊어짐</c:if>
				<c:if test="${result.broadSttus eq '255' }">연결이상</c:if>
			</td>			
			<td>${result.logTime }</td>
			<!-- <td>로그 원본</td> -->
		</tr>
	</c:forEach>	
</table>  