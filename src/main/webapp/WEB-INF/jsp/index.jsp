<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>방송시스템 모니터링 솔루션</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<jsp:include page="favicon.jsp"></jsp:include>
<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/ui.js"></script>
<link href="/resources/css/style.css?ver=1.0" rel="stylesheet" type="text/css" />
<link href="/resources/css/layout.css" rel="stylesheet" type="text/css" />


<link rel="stylesheet" type="text/css" href="/resources/css/percent.css">
<link rel="stylesheet" type="text/css" href="/resources/css/jqcloud.css" />
<link rel="stylesheet" type="text/css" href="/resources/css/simple-donut.css">
<link rel="stylesheet" type="text/css" href="/resources/css/jquery-gauge.css">

<script type='text/javascript' src="/resources/js/jqcloud.js"></script>
<script type='text/javascript' src='/resources/js/simple-donut-jquery.js'></script>
<script type='text/javascript' src='/resources/js/gauge.min.js'></script>
</head>

<body>

	<div class="resualt_ui">
		<jsp:include page="header.jsp">
			<jsp:param name="pgidx" value="1"></jsp:param>
		</jsp:include>

		<section class="resource_body">
			<div class="colg">
				<div class="cola">
					<!-- <h2>서버 리소스</h2> -->
					<dl class="server_graph">
						<div>
							<!-- <dt>서버 1</dt> -->
							<dd>
								<ul class="donut-ul" style="text-align: center;">
								<li><dt id="ser1" class="bangser">DB 서버</dt><!-- <br/><span>127.0.0.1</span> --></li>
								<li>
									<div id="specificCPUChart0" class="donut-size CPU">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">CPU</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
								<li>
									<div id="specificRAMChart0" class="donut-size RAM">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">RAM</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
								<li>
									<div id="specificHDDChart0" class="donut-size HDD">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">HDD</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
							</ul>
							</dd>
							
							<dd>
								<ul class="donut-ul" style="text-align: center;">
								<li><dt id="ser2" class="bangser">방송서버 1</dt><!-- <br/><span>127.0.0.1</span> --></li>
								<li>
									<div id="specificCPUChart1" class="donut-size CPU">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">CPU</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
								<li>
									<div id="specificRAMChart1" class="donut-size RAM">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">RAM</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
								<li>
									<div id="specificHDDChart1" class="donut-size HDD">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">HDD</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
							</ul>
							</dd>
							<dd>
								<ul class="donut-ul" style="text-align: center;">
								<li><dt id="ser3" class="bangser">방송서버 2</dt><!-- <br/><span>127.0.0.1</span> --></li>
								<li>
									<div id="specificCPUChart2" class="donut-size CPU">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">CPU</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
								<li>
									<div id="specificRAMChart2" class="donut-size RAM">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">RAM</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
								<li>
									<div id="specificHDDChart2" class="donut-size HDD">
										<div class="pie-wrapper">
											<span class="label">
												<span class="name">HDD</span>
												<span class="num">0</span>
												<span class="smaller">%</span>
											</span>
											<div class="pie">
												<div class="left-side half-circle"></div>
												<div class="right-side half-circle"></div>
											</div>
											<div class="shadow"></div>
										</div>
									</div>
								</li>
							</ul>
							</dd>
						</div>						
					</dl>
				</div>
				<div class="colb" style="display:none;">
					<h2>최근 로그</h2>
					<select id="logType" onchange="javascript:getBroadMiniList();">
						<option value="3" selected="selected">방송 정보 로그</option>
						<option value="1">디바이스 상태 로그</option>
						<option value="2">프로그램 상태 로그</option>
					</select>
					<div class="keyword" style="text-align:center" id="broadLog">
						
					</div>
				</div>
			</div>
			<div class="colg">
				<div class="cola">
					<!-- <h2>디바이스 상태</h2> -->
					<div class="dec"><!-- <i class="st2">대기</i> --> <i class="st1">정상</i> <i class="st3">에러</i></div>
					<div class="state_g" id="devSttus">
						<!-- <div class="st2">1</div><div class="st1">2</div><div class="st1">3</div><div class="st1">4</div><div class="st1">5</div>
						<div class="st1">6</div><div class="st1">7</div><div class="st1">8</div><div class="st1">9</div><div class="st3">10</div>
						<div class="st1">11</div><div class="st2">12</div><div class="st1">13</div><div class="st1">14</div><div class="st1">15</div>
						<div class="st1">16</div><div class="st1">17</div><div class="st2">18</div><div class="st1">19</div><div class="st1">20</div>
						<div class="st2">1</div><div class="st1">2</div><div class="st1">3</div><div class="st1">4</div><div class="st1">5</div>
						<div class="st1">6</div><div class="st1">7</div><div class="st1">8</div><div class="st1">9</div><div class="st3">10</div>
						<div class="st1">11</div><div class="st2">12</div><div class="st1">13</div><div class="st1">14</div><div class="st1">15</div>
						<div class="st1">16</div><div class="st1">17</div><div class="st2">18</div><div class="st1">19</div><div class="st1">20</div> -->
					</div>
				</div>
				<!-- <div class="colb">
					<div class="col">
						<h2>실시간 채널 사용량</h2>
						<div class="use_graph" style="text-align:center">
							<img src="resources/images/tmp/graph2.jpg" alt="" />
						</div>
					</div>
					<div class="col">
						<h2>음성 인식 현황</h2>
						<div class="voice_graph" style="text-align:center">
							<img src="resources/images/tmp/graph3.jpg" alt="" />
						</div>
					</div>
				</div> -->
			</div>
		</section>

		<footer>COPYRIGHT ⓒ NectarSoft 2021</footer>
	</div><!-- //resualt_ui -->
	
<div id="popup1" class="colPop" style="display:none;">
	<div style="position: relative;height: 30px;width: 100%;background-color: #43427c;background-size: cover;color: white;">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="popFname">경산</span>
		<button class="tb" type="button" onclick="javascript:closePop();">X</button>
	</div>
	<div class="state_g" id="devSttusIn" style="width: 100%;padding-top: 10px;">	
		
	</div>
</div>

<script>
$(document).ready(function(){
	getSvrResource(0);
	getSvrResource(1);
	getSvrResource(2);
	//getBroadMiniList();
	getDevSttus();
	chkMaster();
	/*setInterval(function() {
		
	}, 30000);*/
	setInterval(function() {
		chkMaster();
		getDevSttus();
		getSvrResource(0);
		getSvrResource(1);
		getSvrResource(2);
	}, 10000);	
});
function getSvrResource(dataSeq) {
	var url = ['/api/pcMonitor', '/api/pcMonitor1', '/api/pcMonitor2'];
	
	$.ajax({
	    url: url[dataSeq],
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	drawSvgGauge(dataSeq, data);
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}
function drawSvgGauge(dataSeq, data) {
	updateDonutChart('#specificCPUChart'+dataSeq, data.perCpu, true); //CPU
	updateDonutChart('#specificRAMChart'+dataSeq, data.perMem, true); //RAM
	updateDonutChart('#specificHDDChart'+dataSeq, data.perDsk, true); //HDD
}
function getBroadMiniList(){
	var url = '/miniBroadLogTable.do';
	var chk = document.getElementById("logType").value;
	if(chk=="1"){
		url = "/miniDevLogTable.do";
	}else if(chk=="2"){
		url = "/miniProgLogTable.do";
	}
	$.get(url, '', function(newitems){	
		document.getElementById("broadLog").innerHTML = newitems;
	});
}
function getDevSttus(){
	var url = '/dashboardDevSttus.do';
	$.get(url, '', function(newitems){	
		document.getElementById("devSttus").innerHTML = newitems;
	});
}

function showDev(fidx, fname){
	document.getElementById("popFname").innerHTML = fname;
	var url = '/dashboardDevSttusIn.do?fidx='+fidx;
	$.get(url, '', function(newitems){	
		document.getElementById("devSttusIn").innerHTML = newitems;
	});
	$("#popup1").show();
}
function closePop(){
	document.getElementById("devSttusIn").innerHTML = "";
	$("#popup1").hide();
}
function goLogPage(devId){
	var url = "/logList.do?searchDevId="+devId;
	location.href=url;
}

function chkMaster(){
	var url = '/nowMaster.do';
	$.get(url, '', function(newitems){
		 $(".bangser").removeClass("this_server");
		$("#ser"+newitems).addClass("this_server");
	});
}
</script>
</body>
</html>