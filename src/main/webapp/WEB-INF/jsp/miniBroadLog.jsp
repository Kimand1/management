<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>      
<table>
	<tr>
		<th>디바이스 아이디</th>
		<th>접수대 아이디</th>
		<th>작성 프로그램</th>
		<th>방송상태</th>
		<th>로그시간</th>
		<!-- <th>로그 원본</th> -->
	</tr>
	<c:forEach var="result" items="${lists}" varStatus="status">
		<tr>
			<td>${result.devId }</td>
			<td>${result.JId }</td>
			<td>
				<c:if test="${result.proType eq 'G' }">게이트웨이</c:if>
				<c:if test="${result.proType eq 'R' }">레코더</c:if>
			</td>
			<td>
				<c:if test="${result.broadSttus eq '1' }">방송 시작</c:if>
				<c:if test="${result.broadSttus eq '2' }">방송 종료</c:if>
				<c:if test="${result.broadSttus eq '3' }">출동벨 수신</c:if>
				<c:if test="${result.broadSttus eq '255' }">방송이상</c:if>
			</td>			
			<td>${result.logTime }</td>
			<!-- <td>로그 원본</td> -->
		</tr>
	</c:forEach>	
</table>  