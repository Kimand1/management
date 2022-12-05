<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:forEach var="fire" items="${fireList}" varStatus="status">
	<c:if test="${fire.fidx eq fidx }">
		<c:forEach var="dev" items="${devList }" varStatus="status2">
			<c:if test="${dev.fidx eq fire.fidx }">
				<c:set var="devSttus" value="0"/>
				<c:forEach var="result" items="${lists}" varStatus="status3">
					<c:if test="${result.devId eq dev.devId }">
						<c:if test="${result.broadSttus eq 2 }">
							<c:set var="devSttus" value="${devSttus + 1 }"/>
						</c:if>
					</c:if>
				</c:forEach>
				<c:set var="sttus" value="2"/>
				<c:if test="${devSttus gt 0 }">
					<c:set var="sttus" value="3"/>	
				</c:if>
				<div class="st${sttus}" title="${dev.dname }" onclick="javascript:goLogPage('${dev.devId}');">
					${dev.devId }<br/><br/>
					<span class="dname" style="color:black">${dev.dname }</span>
				</div>			
			</c:if>
		</c:forEach>
	</c:if>
</c:forEach>
<%-- <c:forEach var="result" items="${lists}" varStatus="status">
	<div class="st${result.broadSttus}" title="">${result.devId }</div>	
</c:forEach> --%>