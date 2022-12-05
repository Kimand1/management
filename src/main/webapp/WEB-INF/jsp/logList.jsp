<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>          
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>방송시스템 모니터링 솔루션</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<jsp:include page="favicon.jsp"></jsp:include>
<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="resources/js/ui.js"></script>
<link href="resources/css/style.css" rel="stylesheet" type="text/css" />
<link href="/resources/css/layout.css" rel="stylesheet" type="text/css" />
<script>
function checkForm(){
	//alert("전송");
	var f = document.frm;	
	if(f.pc.value=="N"){
		f.pageNum.value = "1";
	}
	return true;
}

function goPage(pageno){
	var f = document.frm;
	f.pageNum.value = pageno;
	f.pc.value = "Y";
	f.submit();
}
function viewLog(){
	if(document.getElementById("viewChk").value=="0"){
		$(".logstr").show();	
		$(".box").width("100%");
		document.getElementById("viewChk").value="1";
	}else{
		$(".logstr").hide();
		$(".box").width("1050px");
		document.getElementById("viewChk").value="0";
	}
	
}
</script>
</head>
<body>
	<input type="hidden" id="viewChk" value="0"/>
	<div class="resualt_ui">
		<jsp:include page="header.jsp">
			<jsp:param name="pgidx" value="2"></jsp:param>
		</jsp:include>
		
		<section class="call_body">
			<form name="frm" method="post" onsubmit="return checkForm()" action="/logList.do">
			<input type="hidden" name="pc" value="N"/>
			<input type="hidden" name="pageNum" value="${pageNum }"/>
			<input type="hidden" name="totalCnt" value="${totalCnt }"/>
			<div class="had"> 
				<label for="fDay1">시작날짜</label>
				<input type="datetime-local" name="bgnde" value="${broadlogVO.bgnde }" />
				<label for="fDay2">종료날짜</label>
				<input type="datetime-local" name="endde" value="${broadlogVO.endde }"/> 
				<label for="logType">로그 종류</label>
				<select id="logType" name="searchLogType">
					<option value="">전체</option>
					<option value="3" <c:if test="${broadlogVO.searchLogType  eq '3'}">selected</c:if>>방송 정보 로그</option>
					<option value="1" <c:if test="${broadlogVO.searchLogType  eq '1'}">selected</c:if>>디바이스 상태 로그</option>
					<option value="2" <c:if test="${broadlogVO.searchLogType  eq '2'}">selected</c:if>>프로그램 상태 로그</option>
				</select>
				<label for="proType">작성 프로그램 종류</label>
				<select id="proType" name="searchProType">
					<option value="">전체</option>
					<option value="G" <c:if test="${broadlogVO.searchProType  eq 'G'}">selected</c:if>>게이트웨이</option>
					<option value="R" <c:if test="${broadlogVO.searchProType  eq 'R'}">selected</c:if>>레코더</option>
					<option value="M" <c:if test="${broadlogVO.searchProType  eq 'M'}">selected</c:if>>매니저</option>
					<option value="C" <c:if test="${broadlogVO.searchProType  eq 'C'}">selected</c:if>>콘솔</option>
				</select>
			</div>
			<div class="had2" > 	
				<label for="dNum">디바이스 아이디</label>
				<input type="text" id="dNum" name="searchDevId" value="${broadlogVO.searchDevId }"/>
				<label for="searchStationId">스테이션 아이디</label>
				<input type="text" id="searchStationId" name="searchStationId" value="${broadlogVO.searchStationId }"/>				
				<label for="fNum">접수대번호</label>
				<input type="text" id="fNum" name="searchJId" value="${broadlogVO.searchJId }"/>
				<label for="fTime">목록 갯수</label>
				<select id="fTime" name="pageSize">
					<option value="10">10개</option>
					<option value="50" <c:if test="${pageSize  eq '50'}">selected</c:if>>50개</option>
					<option value="100" <c:if test="${pageSize  eq '100'}">selected</c:if>>100개</option>
					<option value="1000" <c:if test="${pageSize  eq '1000'}">selected</c:if>>1000개</option>
					<option value="10000" <c:if test="${pageSize  eq '10000'}">selected</c:if>>10000개</option>
				</select>
				<button class="btn" type="submit">검색</button>
			</div>
			</form>
			
			<div class="body">
				<div class="box">
					<div class="tbl_g">
						<div class="tbl">
							<div><button type="button" onclick="javascript:viewLog();" class="btn">로그 원문 보기</button></div>
							<table>
								<thead>
									<tr>
										<th>로그 종류</th>
										<th>
											<c:choose>
												<c:when test="${broadlogVO.searchLogType eq '2'}">오브젝트 아이디</th><th>프로그램코드</c:when>
												<c:otherwise>디바이스 아이디</c:otherwise>
											</c:choose>											
										</th>
										<c:if test="${broadlogVO.searchLogType eq '3' or broadlogVO.searchLogType eq '' or broadlogVO.searchLogType eq null}">
										<th>스테이션 아이디</th>
										<th>접수대 아이디</th>
										<th>작성 프로그램</th>
										</c:if>
										<th>상태</th>
										<c:if test="${broadlogVO.searchLogType eq '3' or broadlogVO.searchLogType eq '' or broadlogVO.searchLogType eq null}">
										<th>방송 종류</th>
										</c:if>
										<th>로그시간</th>
										<th class="logstr" style="display:none;">로그원문</th>
										<th class="logstr" style="display:none;">서버번호/파일명</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach var="result" items="${lists}" varStatus="status">
									<tr>
										<td>
											<c:choose>
												<c:when test="${result.logType eq '3' }">방송 정보 로그</c:when>
												<c:when test="${result.logType eq '1' }">디바이스 상태 로그</c:when>
												<c:when test="${result.logType eq '2' }">프로그램 상태 로그</c:when>
												<c:otherwise>${result.logType}</c:otherwise>
											</c:choose>
										</td>
										<td>
											<c:choose>
												<c:when test="${fn:contains(result.devId, '[S]') }">null</c:when>
												<c:otherwise> ${result.devId }</c:otherwise>
											</c:choose>
										</td>
										<c:if test="${broadlogVO.searchLogType eq '2'}">
											<td>
												<c:choose>
													<c:when test="${result.proType eq 'G' }">게이트웨이</c:when>
													<c:when test="${result.proType eq 'R' }">레코더</c:when>
													<c:when test="${result.proType eq 'M' }">매니저</c:when>
													<c:when test="${result.proType eq 'C' }">콘솔</c:when>
													<c:otherwise>${result.proType}</c:otherwise>
												</c:choose>
											</td>
										</c:if>
										<c:if test="${broadlogVO.searchLogType  eq '3' or broadlogVO.searchLogType eq '' or broadlogVO.searchLogType eq null}">
											<td>
												<c:choose>
													<c:when test="${fn:contains(result.devId, '[S]') }">
														<c:out value="${fn:replace(result.devId, '[S]', '') }"/> 
													</c:when>
													<c:otherwise>
														<c:forEach var="dev" items="${devList }" varStatus="status2">
															<c:if test="${result.devId eq dev.devId }">${dev.stationId }</c:if>
														</c:forEach>
													</c:otherwise>
												</c:choose>											
											</td>
											<td>${result.JId }</td>
											<td>
												<c:choose>
													<c:when test="${result.proType eq 'G' }">게이트웨이</c:when>
													<c:when test="${result.proType eq 'R' }">레코더</c:when>
													<c:when test="${result.proType eq 'M' }">매니저</c:when>
													<c:when test="${result.proType eq 'C' }">콘솔</c:when>
													<c:otherwise>${result.proType}</c:otherwise>
												</c:choose>
											</td>
										</c:if>
										<td>
											<c:choose>
												<c:when test="${result.logType eq '3' }">
													<c:choose>
														<c:when test="${result.broadSttus eq '1' }">방송 시작</c:when>
														<c:when test="${result.broadSttus eq '2' }">방송 종료</c:when>
														<c:when test="${result.broadSttus eq '3' }">출동벨 수신</c:when>
														<c:when test="${result.broadSttus eq '4' }">방송 실패</c:when>
														<c:when test="${result.broadSttus eq '255' }">방송종료</c:when>
													</c:choose>
												</c:when>
												<c:when test="${result.logType eq '1' }">
													<c:choose>
														<c:when test="${result.broadSttus eq '1' }">디바이스 연결</c:when>
														<c:when test="${result.broadSttus eq '2' }">연결 끊어짐</c:when>
														<c:when test="${result.broadSttus eq '255' }">연결이상</c:when>
														<c:otherwise>연결이상${result.broadSttus}</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${result.logType eq '2' }">
													<c:choose>
														<c:when test="${result.broadSttus eq '1' }">연결</c:when>
														<c:when test="${result.broadSttus eq '2' }">연결 끊어짐</c:when>
													</c:choose>
												</c:when>
												<c:otherwise>
													${result.broadSttus}
												</c:otherwise>
											</c:choose>
										</td>			
										<c:if test="${broadlogVO.searchLogType  eq '3' or broadlogVO.searchLogType eq '' or broadlogVO.searchLogType eq null}">
											<td>
												<c:choose>											
													<c:when test="${result.logType eq '3'  && result.broadSttus eq '1' }">
														<c:choose>
															<c:when test="${result.broadType eq '0' }">화재</c:when>
															<c:when test="${result.broadType eq '1' }">구조</c:when>
															<c:when test="${result.broadType eq '2' }">구급</c:when>
															<c:when test="${result.broadType eq '3' }">일반</c:when>
															<c:when test="${result.broadType eq '4' }">예고</c:when>
															<c:when test="${result.broadType eq '6' }">생활안전</c:when>
															<c:otherwise>${result.broadType}</c:otherwise>
														</c:choose>
													</c:when>
													<c:when test="${result.logType eq '3'  && result.broadSttus eq '2' }">
														<c:choose>
															<c:when test="${result.broadType eq '1' }">일반 종료</c:when>
															<c:when test="${result.broadType eq '1' }">Time out</c:when>
															<c:otherwise>${result.broadType}</c:otherwise>
														</c:choose>
													</c:when>
													<c:when test="${result.logType eq '3'  && result.broadSttus eq '4' }">
														<c:choose>
															<c:when test="${result.broadType eq '0' }">디바이스 끊어짐</c:when>
															<c:when test="${result.broadType eq '1' }">디바이스 없음</c:when>
															<c:when test="${result.broadType eq '2' }">콘솔 끊어짐</c:when>
															<c:when test="${result.broadType eq '3' }">콘솔 없음</c:when>
															<c:when test="${result.broadType eq '4' }">디바이스 끊어짐</c:when>
															<c:when test="${result.broadType eq '5' }">디바이스 없음</c:when>
															<c:when test="${result.broadType eq '6' }">콘솔 끊어짐</c:when>
															<c:when test="${result.broadType eq '7' }">콘솔 없음</c:when>
															<c:otherwise>${result.broadType}</c:otherwise>														
														</c:choose>
													</c:when>
												</c:choose>
											</td>
										</c:if>
										<td>${result.logTime }</td>
										<td class="logstr" style="display:none;">${result.logStr}</td>
										<td class="logstr" style="display:none;">${result.serverNo}</td>
									</tr>
								</c:forEach>	
								</tbody>
								<!-- <caption>거통화이력</caption>
								<colgroup>
									<col width="33.3%" span="3" />
								</colgroup>
								<thead>
								<tr>
									<th scope="col">통화시작시간</th>
									<th scope="col">접수대번호</th>
									<th scope="col">신고자번호</th>
								</tr>
								</thead>
								<tbody>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>01011111111</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>01011111111</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								<tr>
									<td>2022-10-20 18:07:30</td>
									<td>0232</td>
									<td>0100</td>
								</tr>
								</tbody> -->
							</table>
						</div>
						<div class="paging">
							<c:if test="${startPage*1 gt pageBlock*1 }">
								<a href="javascript:goPage(${startPage*1 - pageBlock});" class="prev">이전</a>
							</c:if>
							<ul>
								<c:forEach var="i" begin="${startPage }" end="${endPage }">
									<c:choose>
										<c:when test="${i*1 eq pageNum*1 }"><li><strong>${i }</strong></li></c:when>
										<c:otherwise><li><a href="javascript:goPage(${i });">${i }</a></li></c:otherwise>
									</c:choose>									
								</c:forEach>
							</ul>
							<c:if test="${endPage*1 lt pageCount*1}">
								<a href="javascript:goPage(${startPage + pageBlock });" class="next">다음</a>
							</c:if>
						</div><!-- //paging -->
					</div>
					<div class="callmemo_g" style="display:none;">
						<p class="tit">통화내용</p>
						<div class="callmemo">
							<div class="me"><p>네</p></div>
							<div class="me"><p>119입니다</p></div>
							<div class="you"><p>마산 합포구 삼일 오대로 백이십칠</p></div>
							<div class="you"><p>경남 창원시 마산 합포구 삼일 오대로 백십사</p></div>
							<div class="me"><p>아 예</p></div>
							<div class="me"><p>아 이거 어떻게 치료를 하셔야 할지 몰라서 그러는데요</p></div>
							<div class="you"><p>마산 합포구 삼일 오대로 백이십칠</p></div>
							<div class="you"><p>경남 창원시 마산 합포구 삼일 오대로 백십사</p></div>
							<div class="me"><p>네</p></div>
							<div class="me"><p>119입니다</p></div>
							<div class="you"><p>마산 합포구 삼일 오대로 백이십칠</p></div>
							<div class="you"><p>경남 창원시 마산 합포구 삼일 오대로 백십사</p></div>
							<div class="me"><p>아 예</p></div>
							<div class="me"><p>아 이거 어떻게 치료를 하셔야 할지 몰라서 그러는데요</p></div>
							<div class="you"><p>마산 합포구 삼일 오대로 백이십칠</p></div>
							<div class="you"><p>경남 창원시 마산 합포구 삼일 오대로 백십사</p></div>
						</div>
					</div>
				</div>
			</div>
		</section>

		<footer>COPYRIGHT ⓒ NectarSoft 2021</footer>
	</div><!-- //resualt_ui -->

	<script type="text/javascript">
	<!--
		datepicker();
	//-->
	</script>
	
</body>
</html>