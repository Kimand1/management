var websocket;

var recogResultAll=[];

var sequence = 0;
var extId; //접수대 내선번호

var extVoiceId;
var callerVoiceId;

var addrSeq = 0;
var addrTimer;
var finishTimer;

var fullAddrList = [];
var addrSet = [];

var clickAddrSet = [];

//안바뀌는 통화 개수
var recentResult = 1;

//주소검색 할 통화 개수
var voiceSearchCount = 2;

//최근 2개 통화 seq
var lastThreeCount_send = [];
var lastThreeCount_receive = [];

//최근 2개 통화 text
var lastThreeResult_send = [];
var lastThreeResult_receive = [];

//들어온 통화가 2개 이상인지 체크
var lastThreeLengthCheck_send=0;
var lastThreeLengthCheck_receive=0;

//최근2개를 제외한 2개 통화 text
var noThreeResult_send = [];
var noThreeResult_receive = [];

//중복으로 검색하는거 체크
var textOverlapCheck= "";

//최근2개를 제외한 5개 통화 text ta용
var noFiveResult_send_ta = [];
var noFiveResult_receive_ta = [];

//중복으로 보내는거 체크 ta용
var textOverlapCheck_send_ta = "";
var textOverlapCheck_receive_ta = "";

//두번 들어오는거 체크
var twiceReturnCheck_receive=0;
var twiceReturnCheck_send=0;

var websocketIp = window.location.hostname;
var websocketPort = "9001";

var getBaseStationAddrTimer;

var locationInformationCodeList = [];
var specialDisasterKeyword = [];

var keywordNew = [];
var keywordNewSize = 0;

var taAddressList = [];

var a = 0;
var b = 0;
var d = 0;




$(document).ready(function() {

	//토스트 메세지 시간 설정 //신명화 - script error로 주석처리
   /* $('.toast').toast({
    	delay: 1000
    });*/
    
    //웹소켓 연결
	socket();
	
	//새로고침
	$("#refresh").off("click");
	$("#refresh").on("click", function(e){
		location.reload();
	});
	
	//화면 하단 탭
	$('#tab ul.tabs li.clickable').click(function(){
		if($(this).hasClass("clickable")){
			var tab_id = $(this).attr('data-tab');
			
			$('#tab ul.tabs li').removeClass('current');
			$('#tab .tab-content').removeClass('current');
			
			$('#tab ul.tabs li').removeClass('fadeIn');
			
			$(this).addClass('current');
			$("#tab #"+tab_id).addClass('current');
			$("#tab #"+tab_id).addClass('fadeIn');
			
			if(tab_id=="tab-2"){
				getRecentResult();
			}
		}
	});
	
	//높이 조절 관련 변수
	a = document.getElementById("div1").offsetHeight+"px";
    b = document.getElementById("div2").offsetHeight+"px";
    d = document.getElementById("tab-1").offsetHeight;
    
});


//높이조절 함수
function drag_handler(event) {
    if(event.pageY > 350 && event.pageY < (a.replace("px","")*1)+(b.replace("px","")*1)-100){		
        a = a.replace("px","")*1;
        b = b.replace("px","")*1;
        document.getElementById("div1").style.height = (event.pageY-document.getElementById("div3").offsetHeight-document.getElementsByClassName("navbar")[0].offsetHeight)+"px";
        var temp = document.getElementById("div1").offsetHeight+"px";
        temp = temp.replace("px","")*1;
        temp = (a + b - temp);
        document.getElementById("div2").style.height = temp + "px";
        
        a = document.getElementById("div1").offsetHeight+"px";
        b = document.getElementById("div2").offsetHeight+"px";
        document.getElementById("tab-1").offsetHeight = d;
    }
}

//웹소켓
function socket(){
	var str = 'ENTER|{"ext":"'+inid+'"}';
	//var wsUrl = "ws://"+wizerDataSearchUrl+":9001/ws?msg="+str;
	var wsUrl = "ws://"+websocketIp+":"+websocketPort+"/ws?msg="+str; //여기서 127.0.0.1을 쓰면 웹클라이언트(웹에 접속한 PC)의 ip가 되어버려서 안됨
	
	websocket = new WebSocket(wsUrl);
	
    websocket.onopen = function (evt) { onOpen(evt) };
    websocket.onclose = function (evt) { onClose(evt) };
    websocket.onmessage = function (evt) { onMessage(evt) };
    websocket.onerror = function (evt) { onError(evt) };
    
    
    websocket.onclose = function(){
    	$("#connection").text("연결끊김");
        $("#connection").css('color','#ff0000');
        
		setTimeout(socket, 300);
	}
}

//웹소켓 오픈 시
function onOpen(evt) {
	recogResultAll=[];
	
    console.log("SOCKET CONNECTED");
    $("#connection").text("연결중");
    $("#connection").css('color','#004eff');
}

//웹소켓으로 메세지가 왔을 때
function onMessage(evt) {
	//var resultMap = JSON.parse(evt.data);
	var nDate = new Date();
	var year = nDate.getFullYear();
	var month = nDate.getMonth() + 1;
	var date = nDate.getDate();
	var hours = nDate.getHours();
	var minutes = nDate.getMinutes();
	var seconds = nDate.getSeconds();
	var milliseconds = nDate.getMilliseconds();
	//if (consoledisp_t) console.log("["+year+"-"+month+"-"+date+" "+hours+":"+minutes+":"+seconds+"."+milliseconds+"]   "+evt.data);

	var sData = evt.data.split("|")[1];
	var obj;
	if (sData != undefined) {
		obj = JSON.parse(sData);

		//20220914 voiceId = obj.ext;
		
		extId = obj.ext;
		

		if (obj.recog.text == "START") {
			
			//20220914
			if (obj.recog.type == "0") {
				if(addrTimer != null) {
					clearInterval(getBaseStationAddrTimer);
					getBaseStationAddrTimer = null;
					finishAddr();
				}
				
				reset();

				//extId = obj.ext;
				extVoiceId = obj.recog.voiceId;
				startAddr();
				getBaseStationAddrTimer = setInterval(getBaseStationAddr, 500);

			} else {
				//extId = obj.ext;
			}


			/* 20220914
			if(addrFlag==0){
				//초기화
				reset();
				
				addrFlag++;
				startAddr();
				
				getBaseStationAddrTimer = setInterval(getBaseStationAddr, 500);
			}else{
				addrFlag++;
			}
			*/

			//console.log("START "+addrFlag);
			return;
		} else if (obj.recog.text == "FINISH") {
			//console.log("FINISH["+obj.recog.type+"] | extId : " + extVoiceId + ", callerVoiceId : " + callerVoiceId);

			//20220914
			if (obj.recog.type == "0") {
				var n = new Date();
				endTime = n.getFullYear() + "-" + (n.getMonth() + 1) + "-" + n.getDate() + " " + n.getHours() + ":" + n.getMinutes() + ":" + n.getSeconds();

				noThreeResult_receive = lastThreeResult_receive;
				noThreeResult_send = lastThreeResult_send;

				clearInterval(getBaseStationAddrTimer);
				getBaseStationAddrTimer = null;
				finishAddr();
				resultSave();
			}


			/* 20220914
			if(addrFlag>1){
				addrFlag--;
			}else{
				addrFlag=0;
				var n = new Date();
				endTime = n.getFullYear()+"-"+(n.getMonth()+1)+"-"+n.getDate()+" "+n.getHours()+":"+n.getMinutes()+":"+n.getSeconds();
				
				noThreeResult_receive = lastThreeResult_receive; 
				noThreeResult_send = lastThreeResult_send;
				
				//20220818
				//addr(); 
				//clearInterval(getBaseStationAddrTimer);
				//getBaseStationAddrTimer = null;
				//if(finishTimer == null) {
				//	finishTimer = setInterval(checkFinish, 1000);
				//}
				
				
				// 20220818
				clearInterval(getBaseStationAddrTimer);
				getBaseStationAddrTimer = null;
				finishAddr();
				resultSave();
			}
			 */

			//console.log("FINISH "+addrFlag);
			return;
		} else if (obj.recog.text == undefined) {
			return;
		}




		var html = "";
		if (obj.recog.type == "0") {//발신자

			var twiceReturnCheck = parseInt(obj.recog.recogSeq);
			if (twiceReturnCheck - twiceReturnCheck_receive == 1000000) {
				return;
			} else {
				twiceReturnCheck_receive = twiceReturnCheck
			}


			extVoiceId = obj.recog.voiceId;
			if (lastThreeCount_receive.indexOf(obj.recog.recogSeq)>-1) { //있으면 해당 seq가 id인 div 텍스트 내용 변경ㅇ 
				$("#r_" + obj.recog.recogSeq).text(obj.recog.text);
				var idx = lastThreeCount_receive.indexOf(obj.recog.recogSeq);
				lastThreeResult_receive[idx] = obj.recog.text;
			} else { //없으면 리스트에 seq추가 및 화면에 div 생성
				if (lastThreeCount_receive.length < recentResult) {//리스트 length가 3미만이면 그냥 추가
					if (lastThreeLengthCheck_receive < recentResult) {
						lastThreeCount_receive.push(obj.recog.recogSeq);
						lastThreeResult_receive.push(obj.recog.text);
						lastThreeLengthCheck_receive++;
					} else {
						var text = lastThreeResult_receive.shift();

						if (noThreeResult_receive.length < voiceSearchCount) {
							noThreeResult_receive.push(text);
						} else {
							noThreeResult_receive.shift();
							noThreeResult_receive.push(text);
						}


						if (noFiveResult_receive_ta.length < 5) {
							noFiveResult_receive_ta.push(text);
						} else {
							noFiveResult_receive_ta.shift();
							noFiveResult_receive_ta.push(text);
						}


						lastThreeCount_receive.shift();
						lastThreeCount_receive.push(obj.recog.recogSeq);
						lastThreeResult_receive.push(obj.recog.text);
					}
				} else { //리스트 length가 3이상이면 첫번째 seq 제거 후 추가
					var text = lastThreeResult_receive.shift();

					if (noThreeResult_receive.length < voiceSearchCount) {
						noThreeResult_receive.push(text);
					} else {
						noThreeResult_receive.shift();
						noThreeResult_receive.push(text);
					}

					if (noFiveResult_receive_ta.length < 5) {
						noFiveResult_receive_ta.push(text);
					} else {
						noFiveResult_receive_ta.shift();
						noFiveResult_receive_ta.push(text);
					}

					lastThreeCount_receive.shift();
					lastThreeCount_receive.push(obj.recog.recogSeq);
					lastThreeResult_receive.push(obj.recog.text);

				}
				/*console.log("lastThreeResult_receive : " +lastThreeResult_receive);
				console.log("noThreeResult_receive : " +noThreeResult_receive);*/
				html = "<div class='text-left'>";
				html += "<div id='r_" + obj.recog.recogSeq + "' class='shadow msg_container_receive'>";
				html += obj.recog.text;
				html += "</div></div>";
				$(".msg_card_body").append(html);
				document.getElementsByClassName("msg_card_body")[0].scrollTop = document.getElementsByClassName("msg_card_body")[0].scrollHeight;
			}

		} else if (obj.recog.type == "1") {//수신자
			var twiceReturnCheck = parseInt(obj.recog.recogSeq);
			if (twiceReturnCheck - twiceReturnCheck_send == 1000000) {
				return;
			} else {
				twiceReturnCheck_send = twiceReturnCheck
			}


			callerVoiceId = obj.recog.voiceId;

			if (lastThreeCount_send.indexOf(obj.recog.recogSeq)>-1) { //있으면 해당 seq가 id인 div 텍스트 내용 변경ㅇ 
				$("#s_" + obj.recog.recogSeq).text(obj.recog.text);
				var idx = lastThreeCount_send.indexOf(obj.recog.recogSeq);
				lastThreeResult_send[idx] = obj.recog.text;
			} else { //없으면 리스트에 seq추가 및 화면에 div 생성
				if (lastThreeCount_send.length < recentResult) {//리스트 length가 3미만이면 그냥 추가
					if (lastThreeLengthCheck_send < recentResult) {
						lastThreeCount_send.push(obj.recog.recogSeq);
						lastThreeResult_send.push(obj.recog.text);
						lastThreeLengthCheck_send++;
					} else {
						var text = lastThreeResult_send.shift();
						if (noThreeResult_send.length < voiceSearchCount) {
							noThreeResult_send.push(text);

						} else {
							noThreeResult_send.shift();
							noThreeResult_send.push(text);
						}


						if (noFiveResult_send_ta.length < 5) {
							noFiveResult_send_ta.push(text);
						} else {
							noFiveResult_send_ta.shift();
							noFiveResult_send_ta.push(text);
						}


						lastThreeCount_send.shift();
						lastThreeCount_send.push(obj.recog.recogSeq);
						lastThreeResult_send.push(obj.recog.text);
					}
				} else { //리스트 length가 3이상이면 첫번째 seq 제거 후 추가
					var text = lastThreeResult_send.shift();

					if (noThreeResult_send.length < voiceSearchCount) {
						noThreeResult_send.push(text);
					} else {
						noThreeResult_send.shift();
						noThreeResult_send.push(text);
					}


					if (noFiveResult_send_ta.length < 5) {
						noFiveResult_send_ta.push(text);
					} else {
						noFiveResult_send_ta.shift();
						noFiveResult_send_ta.push(text);
					}


					lastThreeCount_send.shift();
					lastThreeCount_send.push(obj.recog.recogSeq);
					lastThreeResult_send.push(obj.recog.text);
				}
				/*console.log("lastThreeResult_send : " +lastThreeResult_send);
				console.log("noThreeResult_send : " +noThreeResult_send);
				console.log("-----------------------------------");*/
				html = "<div class='text-right'>";
				html += "<div id='s_" + obj.recog.recogSeq + "' class='shadow msg_container_send'>";
				html += obj.recog.text;
				html += "</div></div>";
				$(".msg_card_body").append(html);
				document.getElementsByClassName("msg_card_body")[0].scrollTop = document.getElementsByClassName("msg_card_body")[0].scrollHeight;
			}

		}


		var dat = {};
		dat.no = obj.recog.sendSeq;
		dat.recogText = obj.recog.text;
		recogResultAll.push(dat);
		//analysis();


		var resultCode = evt.data.split("|")[0];
		//20220914 if (resultCode == "RESULT" && addrFlag > 0) {
		if (resultCode == "RESULT") {
			addr();
		}


		if (obj.recog.type == "0") {//발신
			ta("0");
		} else {
			ta("1");
		}
	}
}

//웹소켓 연결종료
function onClose(evt) {
	console.log("WEBSOCKET CLOSED");
    websocket.close();
}

//웹소켓 에러
function onError(evt) {
	console.log("SOCKET CONNECT ERROR!!");
    websocket.close();
}

//TA 검색
function ta(type){
	var url = "/process/ta";
	var dat = {};
	
	var text = "";
	
	if(type=="0"){
		for(var i=0; i<noFiveResult_receive_ta.length; i++){
			text = text+noFiveResult_receive_ta[i]+" ";
		}
		
		if(textOverlapCheck_receive_ta==text){
			return;
		}
		textOverlapCheck_receive_ta = text;
	}else{
		for(var i=0; i<noFiveResult_send_ta.length; i++){
			text = text+noFiveResult_send_ta[i]+" ";
		}
		
		if(textOverlapCheck_send_ta==text){
			return;
		}
		textOverlapCheck_send_ta = text;
	}
	
	
	if(text=="")
		return;
	
	dat.text = text;
	
	/*
	dat.text = "국가지점번호가 다마사사공육일이오육 맞으시죠. 국가지점번호 다마사사오육공일육육 " +
			"전라북도 김제시 광활구길 삼십사" +
			"승강기번호는 칠공육삼 다시 삼팔일입니다. 전신주번호는 팔팔공칠에이사이이입니다. 과산화수소 과탄산나트륨 " +
			"천안아이씨 남구미아이씨 서논산 톨게이트 호남고속도로 하행 이십팔점오";
	*/
	
	
	var json = JSON.stringify(dat);
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var result = JSON.parse(response);
			
			taAddressList = result.address.addr;
			
			
			
			var cat = "";
			var type = "";
			if(false){
				if(result.kr_no.length>0){ //국가지점번호
					cat = "KRNO";
					type="GPS";
					for(var i=0; i<result.kr_no.length; i++){
						
						if(!(locationInformationCodeList.indexOf(result.kr_no[i].kr_no)>-1)){
							
							locationInformationCodeList.push(result.kr_no[i].kr_no);
							
							var location_information_code_html="";
							location_information_code_html+="<li";
							location_information_code_html+="><a href='#a' ondblclick='makeMarkerLocation(\""+result.kr_no[i].kr_no+"\",\""+cat+"\",\""+type+"\",\""+result.kr_no[i].gps+"\")' onclick='textCopy(this)'>";
							location_information_code_html+="<span class='locationCode'>"+result.kr_no[i].kr_no+"</span>";
							location_information_code_html+="<span>(국가지점번호)</span>";
							$("#locationInformationCode").html(location_information_code_html+$("#locationInformationCode").html());
							
							if($("#divCode").css("display")=="none"){
								$("#divCode").css("display","block");
							}
							
							/*
						if(!keywordNew.includes(result.kr_no[i].kr_no)){
							keywordNew.push(result.kr_no[i].kr_no);
						}
							 */
						}
					}
				}
			}
			
			if(false){
				if(result.com.length>0){ //특수재난 키워드
					for(var i=0; i<result.com.length; i++){
						if(!(specialDisasterKeyword.indexOf(result.com[i].comname)>-1)){
							specialDisasterKeyword.push(result.com[i].comname);
							var special_disaster_keyword_html="";
							/*special_disaster_keyword_html+="<li";
							special_disaster_keyword_html+="><a href='#a' onclick='disasterModal()'>";
							special_disaster_keyword_html+="<span class='poi_name'>"+result.com[i]+"</span>";*/
							
							special_disaster_keyword_html += '<button type="button" class="btn btn-outline-danger" onclick="disasterModalSummary(\''+result.com[i].index+'\',this)">';
							special_disaster_keyword_html += '<span class="disasterKeyword">'+result.com[i].comname+'</span>';
							special_disaster_keyword_html += '</button>';
							
							
							
							$("#specialDisasterKeyword").html(special_disaster_keyword_html+$("#specialDisasterKeyword").html());
							
							if($("#divDisaster").css("display")=="none"){
								$("#divDisaster").css("display","block");
							}
							/*
							if(!keywordNew.includes(result.com[i].comname)){
								keywordNew.push(result.com[i].comname);
							}
							*/
						}
					}
				}
			}
			
			if(false){
				if(result.highway.length>0){ //고속도로
					cat = "HIGHWAY";
					type="GPS";
					for(var i=0; i<result.highway.length; i++){
						if(!(locationInformationCodeList.indexOf(result.highway[i].highwayname+result.highway[i].distance)>-1)){
							locationInformationCodeList.push(result.highway[i].highwayname+result.highway[i].distance);
	
							var location_information_code_html="";
							location_information_code_html+="<li";
							location_information_code_html+="><a href='#a' ondblclick='makeMarkerLocation(\""+result.highway[i].highwayname+"\",\""+cat+"\",\""+type+"\",\""+result.highway[i].gpsx+", "+result.highway[i].gpsy+"\")' onclick='textCopy(this)'>";
							location_information_code_html+="<span class='locationCode'>"+result.highway[i].highwayname+"</span>";
							location_information_code_html+="<span>("+result.highway[i].distance+"km)</span>";
							location_information_code_html+="<span>(고속도로)</span>";
							$("#locationInformationCode").html(location_information_code_html+$("#locationInformationCode").html());
	
							if($("#divCode").css("display")=="none"){
								$("#divCode").css("display","block");
							}
							
							/*
							if(!keywordNew.includes(result.highway[i].highwayname)){
								keywordNew.push(result.highway[i].highwayname);
							}
							*/
						}
					}
				}
			}
			
			if(false){
				if(result.ic_jct.length>0){ //IC/JCT
					cat = "ICJCT";
					type="GPS";
					for(var i=0; i<result.ic_jct.length; i++){
						if(!(locationInformationCodeList.indexOf(result.ic_jct[i].icname)>-1)){
							locationInformationCodeList.push(result.ic_jct[i].icname);
	
							var location_information_code_html="";
							location_information_code_html+="<li";
							location_information_code_html+="><a href='#a' ondblclick='makeMarkerLocation(\""+result.ic_jct[i].icname+"\",\""+cat+"\",\""+type+"\",\""+result.ic_jct[i].gpsx+", "+result.ic_jct[i].gpsy+"\")' onclick='textCopy(this)'>";
							location_information_code_html+="<span class='locationCode'>"+result.ic_jct[i].icname+"</span>";
							location_information_code_html+="<span>(IC/JCT)</span>";
							$("#locationInformationCode").html(location_information_code_html+$("#locationInformationCode").html());
	
							if($("#divCode").css("display")=="none"){
								$("#divCode").css("display","block");
							}
							
							/*
							if(!keywordNew.includes(result.ic_jct[i].icname)){
								keywordNew.push(result.ic_jct[i].icname);
							}
							*/
						}
					}
				}
			}
			
			if(false){
				if(result.tg.length>0){ //톨게이트
					cat = "TG";
					type="GPS";
					for(var i=0; i<result.tg.length; i++){
						if(!(locationInformationCodeList.indexOf(result.tg[i].unitname)>-1)){
							locationInformationCodeList.push(result.tg[i].unitname);
	
							var location_information_code_html="";
							location_information_code_html+="<li";
							location_information_code_html+="><a href='#a' ondblclick='makeMarkerLocation(\""+result.tg[i].unitname+"\",\""+cat+"\",\""+type+"\",\""+result.tg[i].gpsx+", "+result.tg[i].gpsy+"\")' onclick='textCopy(this)'>";
							location_information_code_html+="<span class='locationCode'>"+result.tg[i].unitname+"</span>";
							location_information_code_html+="<span>(톨게이트)</span>";
							$("#locationInformationCode").html(location_information_code_html+$("#locationInformationCode").html());
	
							if($("#divCode").css("display")=="none"){
								$("#divCode").css("display","block");
							}
						
							/*
							if(!keywordNew.includes(result.tg[i].unitname)){
								keywordNew.push(result.tg[i].unitname);
							}
							*/
						}
					}
				}
			}
			
			
			/*
			var html="";
			if(keywordNewSize!=keywordNew.length){
				keywordNewSize=keywordNew.length;
				for(var i=0; i<keywordNew.length; i++){
					html += '<span class="badge bg-secondary text-dark">'+keywordNew[i]+'</span>';
				}
				$(".keyword_card_body").empty();
			    $(".keyword_card_body").append(html);
			}
			*/
			
			if(result.address.elv_no.length>0){ //승강기
				addElvNo(result.address.elv_no);
			}
			if(result.address.tep_no.length>0){ //전신주
				addTepNo(result.address.tep_no);
			}
			
		}
	};
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}


//특수재난 요약 팝업
function disasterModalSummary(index){
	// window.screenX : 현재 스크린에서 브라우저 좌측상단 X좌표
    // window.screenY : 현재 스크린에서 브라우저 좌측상단 Y좌표
    var winPosX = window.screenX;
    var winPosY = window.screenY;

    var popupX;
    var popupY;
    
    var popupWidth = 600;
    var popupHeight = 400;
    
    popupX = Math.round(winPosX+ window.outerWidth);
    popupY = 0;
    
    var featureWindow = "width=" + popupWidth + ", height="+popupHeight
                      + ", left=" + popupX + ", top=" + popupY + ",alwaysRaised=yes";
	
    window.open("/disasterModalSummary?index="+index, "name(target1)", featureWindow);
}

//TA에서 검출 된 승강기 정보 ELK 검색 키워드에 추가
function addElvNo(elvNo){
	var dat = {};
	dat.voiceId = extId;
	dat.elvNo = elvNo;
	var json = JSON.stringify(dat);

	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/addElvNo";
	var url = "/api/addr/addElvNo";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
		}
	};
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//TA에서 검출 된 전신주 정보 ELK 검색 키워드에 추가
function addTepNo(tepNo){
	var dat = {};
	dat.voiceId = extId;
	dat.tepNo = tepNo;
	var json = JSON.stringify(dat);

	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/addTepNo";
	var url = "/api/addr/addTepNo";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
		}
	};
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//GIS에서 들어 온 신고자 기지국 정보 가져오는 함수
function getBaseStationAddr(){
	if(extVoiceId==null || extVoiceId==undefined)
		return;
	if($("#addressBaseStation").text()!=""){
		clearInterval(getBaseStationAddrTimer);
		getBaseStationAddrTimer = null;
		return;
	}
	var dat = {};
	dat.voiceId = extVoiceId;
	var json = JSON.stringify(dat);
	
	var url = "/process/getBaseStationAddr";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var result = JSON.parse(response);
			if(result.RESULT=="SUCCESS"){
				console.log("===========baseStation : "+result.address+" time : "+new Date()+"===========");
				clearInterval(getBaseStationAddrTimer);
				getBaseStationAddrTimer = null;
				
				var cat = result.cat;
	            var businessName = result.businessName;
	            var address = result.address;
	            var pnu = result.pnu;
	            var html ="<li><a href='#a' ondblclick='makeMarkerAddress(\""+businessName+"\",\""+address+"\",\""+cat+"\",\"y\",\"\",\""+pnu+"\")'><span>"+businessName+"</span></a></li>";

	            $("#addressBaseStation").empty();
	            $("#addressBaseStation").append(html);
	            
	            proj4.defs("EPSG:4326","+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
	            proj4.defs("EPSG:5179","+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs");


	            var wgs84 = new proj4.Proj("EPSG:4326");
	            var utmk = new proj4.Proj("EPSG:5179");


	            var x=parseFloat(result.gps_x); //5179 좌표계 x
	            var y=parseFloat(result.gps_y); //5179 좌표계 y

	            var pt = new proj4.Point(x,y); //포인트 생성
	            var trans =proj4.transform(wgs84,utmk,pt); //좌표계 변경
	            console.log(trans);
	            setLocation(trans,address);
	            
	            clearInterval(getBaseStationAddrTimer);
				getBaseStationAddrTimer = null;
			}
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//기지국 정보 ELK 검색 프로세스에 저장
function setLocation(trans,address){
	var dat = {};
	dat.voiceId = extId;
	dat.x = trans.x;
	dat.y = trans.y;
	dat.keyword = address;
	var json = JSON.stringify(dat);
	
	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/setLocation";
	var url = "/api/addr/setLocation";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//ELK 검색 시작
function startAddr(){
	var dat = {};
	dat.voiceId = extId;
	var json = JSON.stringify(dat);
	
	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/open";
	var url = "/api/addr/open";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var result = JSON.parse(response);
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//ELK 검색 종료
function finishAddr() {
	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/close";
	var url = "/api/addr/close";
	var dat = {};
	dat.voiceId = extId;
	var json = JSON.stringify(dat);
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var result = JSON.parse(response);
			console.log("finishAddr : "+result["MESSAGE"]);
			clearInterval(addrTimer);
			addrTimer = null;
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//음성인식 결과 저장
function resultSave(){
	var url = "/process/resultSave";
	var dat = {};
	dat.extId = extId;
	dat.extVoiceId = extVoiceId;
	dat.callerVoiceId = callerVoiceId;
	

	dat.resultHtml = $(".msg_card_body").html();
	
	//키워드
	var k = "";
	for(var i=0; i<$(".keyword_card_body").children().length; i++){
		k+=$(".keyword_card_body").children()[i].innerText+",";
	}
	if(k.length>0)
		k = k.substr(0,k.length-1);
	dat.keyword = k;
	
	dat.keywordHtml = $(".keyword_card_body").html();
	dat.addrHtml = $("#addressList").html();
	//주소리스트
	var s = "";
	for(var i=0; i<$("#address").children().length; i++){
		if($($("#address").children()[i]).html().indexOf("ROAD")>0){
			s+="ROAD|"+$("#address").children()[i].innerText+",";
		}else if($($("#address").children()[i]).html().indexOf("PARCEL")>0){
			s+="PARCEL|"+$("#address").children()[i].innerText+",";
		}
	}
	for(var i=0; i<$("#poi").children().length; i++){
		s+="POI|"+$("#poi").children()[i].innerText+",";
	}
	
	if(s.length>0)
		s = s.substr(0,s.length-1);
	dat.addrList = s;
	

	dat.clickAddrList = clickAddrSet.toString(); 
	
	
	
	//특수재난 키워드
	var sk = "";
	for(var i=0; i<$("#specialDisasterKeyword").children().length; i++){
		sk+=$("#specialDisasterKeyword").children()[i].innerText+",";
	}
	if(sk.length>0)
		sk = sk.substr(0,sk.length-1);
	dat.specialDisasterKeyword = sk;
	
	//위치정보코드
	var locationCode = "";
	for(var i=0; i<$("#locationInformationCode").children().length; i++){
		var temp = $("#locationInformationCode").children()[i].innerText;
		var code = temp.split("(")[0];
		var type = temp.split("(")[1].replace(")","");
		
		locationCode+=type+"|"+code+",";
	}
	if(locationCode.length>0)
		locationCode = locationCode.substr(0,locationCode.length-1);
	dat.locationInformationCode = locationCode;
	
	
	
	var json = JSON.stringify(dat);
	
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			console.log(response);
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
	
}

//음성 텍스트 ELK 검색 프로세스에 저장
function addr(){
	
	var recogTxt=[];
	var text = "";
	for(var i=0; i<noThreeResult_send.length; i++){
		recogTxt.push(noThreeResult_send[i]);
		text+=noThreeResult_send[i];
		
	}
	for(var i=0; i<noThreeResult_receive.length; i++){
		recogTxt.push(noThreeResult_receive[i]);
		text+=noThreeResult_receive[i];
	}
	if(textOverlapCheck==text){
		return;
	}
	textOverlapCheck = text;

	if(taAddressList.length>0){
		recogTxt.concat(taAddressList);
	}
	
	//recogTxt.push("황미창말길 100")	
	
	
	
	var dat = {};
	dat.keywords = recogTxt;
	dat.domain="aiadvisor";
	dat.voiceId=extId;
	dat.sequence = addrSeq++;
	dat.type = "addr";
	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/find";
	var url = "/api/addr/find";
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var result = JSON.parse(xhttp.responseText); 
			//console.log(result);
			if(addrTimer == null) {
				addrTimer = setInterval(getAddress, 500);
			}
		}
	};
	
	var json = JSON.stringify(dat);
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//ELK에서 검색 된 결과 가져오는 함수
function getAddress() {
	//var url = "http://"+wizerDataSearchUrl+":8081/api/addr/result";
	var url = "/api/addr/result";
	var dat = {};
	dat.voiceId = extId;
	var json = JSON.stringify(dat);
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var result = JSON.parse(xhttp.responseText); 
			if(result["CODE"] ==  "OK") {
				var addrStr = result["CATEGORY"];
				
				
				
				if(addrStr == "end") {
					clearInterval(addrTimer);
					addrTimer = null;
				} else {
					var keywordSet = result.KEYWORD;
					elkKeyword = keywordSet;
					
					//console.log(keywordSet);
					
					if(!(keywordSet==undefined || keywordSet.length == 0)) {
						var html = "";
						//var jusoMapping =keywordSet.keywordJusoMapping;
						var noMapping =keywordSet.keywordNoMapping; //주소,POI관련 키워드
						var additional =keywordSet.keywordAdditional; //추가데이터 관련 키워드
						
						if(noMapping!=undefined && noMapping.length>0){
							for(var i=0; i<noMapping.length; i++){
								//html += '<span class="badge bg-secondary text-dark">'+noMapping[i]+'</span>';
								
								if(!(keywordNew.indexOf(noMapping[i])>-1)){
									keywordNew.push(noMapping[i]);
								}
							}	
						}
						
						if(additional!=undefined && additional.length>0){
							for(var i=0; i<additional.length; i++){
								//html += '<span class="badge bg-secondary text-dark">'+additional[i]+'</span>';
								
								if(!(keywordNew.indexOf(additional[i])>-1)){
									keywordNew.push(additional[i]);
								}
							}	
						}
						
						
						if(keywordNewSize!=keywordNew.length){
							keywordNewSize=keywordNew.length;
							for(var i=0; i<keywordNew.length; i++){
								html += '<span class="badge bg-secondary text-dark">'+keywordNew[i]+'</span>';
							}
							$(".keyword_card_body").empty();
						    $(".keyword_card_body").append(html);
						}
								
					}
					
					var locationInformationCode = result.LOCATION_INFORMATION_CODE;
					if(locationInformationCode){
						var cat = "";
						var type = "";
						var location_information_code_html="";
						for(var i=0; i<locationInformationCode.length; i++){
							if(!(locationInformationCodeList.indexOf(locationInformationCode[i].NO)>-1)){
								locationInformationCodeList.push(locationInformationCode[i].NO);
								
								location_information_code_html+="<li";
								if(locationInformationCode[i].CATEGORY=="ELV"){
									cat = "ELV";
									type = "PNU";
									location_information_code_html+="><a href='#a' ondblclick='makeMarkerLocation(\""+locationInformationCode[i].NO+"\",\""+cat+"\",\""+type+"\",\""+locationInformationCode[i].PNU+"\")' onclick='textCopy(this)'>";
									location_information_code_html+="<span class='locationCode'>"+locationInformationCode[i].NO+"</span>";
									location_information_code_html+="<span>(승강기)</span>";
									//location_information_code_html+="<span class='poi_address'>승강기</span>";
								}else if(locationInformationCode[i].CATEGORY=="TEP"){
									cat = "TEP";
									type = "NO";
									location_information_code_html+="><a href='#a' ondblclick='makeMarkerLocation(\""+locationInformationCode[i].NO+"\",\""+cat+"\",\""+type+"\",\""+locationInformationCode[i].NO+"\")' onclick='textCopy(this)'>";
									location_information_code_html+="<span class='locationCode'>"+locationInformationCode[i].NO+"</span>";
									location_information_code_html+="<span>(전신주)</span>";
									//location_information_code_html+="<span class='poi_address'>전신주</span>";
								}
							}
						}
			            $("#locationInformationCode").html(location_information_code_html+$("#locationInformationCode").html());
					}
					
					var locList = result.RESULT;
					if(locList==undefined || locList.length == 0) {
						console.log("locList is null");
						return;
					}

					for(var i=0 ; i < locList.length ; i++) {
						var type = locList[i]['TYPE'];
						if(type == 'full') {
							fullAddrList.unshift(locList[i]);
						}
					}
					
					for(var i=0; i<fullAddrList.length; i++){
						if(fullAddrList[i]['ADDR']==null){
							fullAddrList.splice(i,1);
						}
					}
					
					
					//addrSet 중복제거
					var checkOverLap = [];
					
					for(var i=0; i<fullAddrList.length; i++){
						var flag = 0;
						if(checkOverLap.length==0){
							checkOverLap.push(fullAddrList[i]);
						}else{
							for(var j=0; j<checkOverLap.length; j++){
								if(checkOverLap[j]['CATEGORY']==fullAddrList[i]['CATEGORY']){
									if(checkOverLap[j]['CATEGORY']=="POI"){
										var c_addr = checkOverLap[j]['ADDR'];
										var c_nm = checkOverLap[j]['poiName'];
										var f_addr = fullAddrList[i]['ADDR'];
										var f_nm = fullAddrList[i]['poiName'];
										
										if((c_addr==f_addr && c_nm==f_nm)){
											flag++;
										}
									}else{
										var c_addr = checkOverLap[j]['ADDR'];
										var f_addr = fullAddrList[i]['ADDR'];
										
										if(c_addr==f_addr){
											flag++;
										}
									}
								}
								
								
							}
							if(flag==0){
								checkOverLap.push(fullAddrList[i]);
							}
						}
						
					}
					addrSet = checkOverLap
					
					sentenceReplace();
					
				}
			}else{
				//console.log(result);
			}
		}else{
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//주소 추천 리스트를 뽑기 위해 음성텍스트 후처리 
function sentenceReplace(){
	var url = "/process/sentenceReplace";
	var dat = {};

    var k = "";
    for(var i=0; i<$(".msg_container_send").length; i++){
		k+=$(".msg_container_send")[i].innerText+" ";
	}
	k = k.trim();
	
	for(var i=0; i<$(".msg_container_receive").length; i++){
		k+=$(".msg_container_receive")[i].innerText+" ";
	}
	k = k.trim();
	
	dat.datastr = k;
	
	var json = JSON.stringify(dat);
	
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var result = JSON.parse(response);
			makeAddrList(result.str);
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//ELK에서 검출 된 주소리스트 화면에 추가
function makeAddrList(fullStr) {

    var address_html=""; //도로명, 지번 주소
    var recommend_html=""; //도로명, 지번 주소
    var poi_html=""; //poi
    
    var head_address="";
    var append_address="";

    var parcelSet = [];
    var roadSet = [];
    var poiSet = [];
    var notPoiSet = [];
    
    var addressLength;
    
    var beforeSplice_notPoi = "";
    var afterSplice_notPoi = "";
    
    var beforeSplice_poi = "";
    var afterSplice_poi = "";

    for (var i=0; i<addrSet.length; i++){
        var cat = addrSet[i]['CATEGORY'];
		if(cat=="POI"){
            poiSet.push(addrSet[i]);
            beforeSplice_poi+=addrSet[i]['ADDR'];
        }else{
            notPoiSet.push(addrSet[i]);
            beforeSplice_notPoi+=addrSet[i]['ADDR'];
            if(cat=="PARCEL"){
                parcelSet.push(addrSet[i]);
            }else{
                roadSet.push(addrSet[i]);
            }
        }
    }
    
    //거리순으로 poi 정렬
    poiSet.sort(function(a,b){
        if(a.distance<b.distance) return -1;
        if(a.distance>b.distance) return 1;
    });
    
    if(poiSet.length>0 && notPoiSet.length>0){
    	for(var i=0; i<poiSet.length; i++){
    		for(var j=0; j<notPoiSet.length;j++){
    			if(notPoiSet[j]['ADDR']==poiSet[i]['ADDR'] || notPoiSet[j]['ADDR']==poiSet[i]['ADDR2']){
    				var temp = notPoiSet.splice(j,1)[0];
    				temp.COLOR = "#ffbaba";
    				notPoiSet.unshift(temp);
    				
    				temp = poiSet.splice(i,1)[0];
    				temp.COLOR = "#ffbaba";
    				poiSet.unshift(temp);
    			}
    		}
    	}
    }
    
    for (var i=0; i<notPoiSet.length; i++){
        afterSplice_notPoi+=notPoiSet[i]['ADDR'];
    }
    for (var i=0; i<poiSet.length; i++){
        afterSplice_poi+=poiSet[i]['ADDR'];
    }
    
    if(poiSet.length>0){
    	if($("#poi li").length!=poiSet.length || beforeSplice_poi!=afterSplice_poi){
	    	for(var i=0; i<poiSet.length; i++){
	    		
	            var cat = poiSet[i]['CATEGORY'];
	            var businessName = poiSet[i]['poiName'];
	            var address = poiSet[i]['ADDR'];
	            var addressType = poiSet[i]['addrTYPE'];
	            var distance = poiSet[i]['distance'];
	            var pnu = poiSet[i]['PNU'];
	            var color = poiSet[i]['COLOR'];
	            
	            if(address==null) continue;
	            
	            
	            
	            
	            
	            if(color!=null){
	            	recommend_html += "<li";
	            	recommend_html += " style='background-color:"+color +" !important'";
	            	recommend_html += "><a href='#a' onclick='textCopy(this)' ondblclick='makeMarkerAddress(\""+businessName+"\",\""+address+"\",\""+cat+"\",\"y\",\""+addressType+"\",\""+pnu+"\")'>";
		            recommend_html += "<span class='poi_name'>"+businessName+"</span><span>("+parseInt(distance)+"m)"+"</span>";
		            
		            recommend_html += "<span class='badge rounded-pill' style='color: #ff0000;float: right;margin: 0;padding: 0;background-color:transparent;'>추천</span>";
		            recommend_html += "<span class='poi_address fullAddress'>"+address+"</span>";
		            recommend_html+="</a></li>";
	            }else{
	            	poi_html+="<li";
	            	poi_html+="><a href='#a' onclick='textCopy(this)' ondblclick='makeMarkerAddress(\""+businessName+"\",\""+address+"\",\""+cat+"\",\"y\",\""+addressType+"\",\""+pnu+"\")'>";
		            poi_html+="<span class='poi_name'>"+businessName+"</span><span>("+parseInt(distance)+"m)"+"</span>";
		            poi_html+="<span class='poi_address fullAddress'>"+address+"</span>";
		            poi_html+="</a></li>";
	            }
	            
	    	}
	    	$("#poi").empty();
	        $("#poi").append(poi_html);
	        
	        if($("#divAddress").css("display")=="none"){
				$("#divAddress").css("display","block");
			}
    	}
    }
    
    
    
    
    
    if(notPoiSet.length>0){
    	if($("#address li").length!=notPoiSet.length || beforeSplice_notPoi!=afterSplice_notPoi){
    		addressLength=notPoiSet.length;
            for(var i=0; i<notPoiSet.length; i++){
                var cat = notPoiSet[i]['CATEGORY'];
                var businessName = notPoiSet[i]['ADDR'];
                var address = notPoiSet[i]['ADDR'];
                var pnu = notPoiSet[i]['PNU'];
                var color = notPoiSet[i]['COLOR'];
                
                
                var addrSplit = address.split(" ");
                var addrNewStr = "";
                var gubun = ["시","도","군","구"];
                for(var j=0; j<addrSplit.length; j++){
                	var tmp = addrSplit[j];
                	if(gubun.indexOf(tmp.substr(tmp.length-1,1))>-1)
                		continue;
                	addrNewStr+=addrSplit[j];
                	
                }
                
                fullStr = fullStr.replace(/(\s*)/g,"");
                
                /*if(fullStr.indexOf(addrNewStr.replace("-","다시"))>-1 || fullStr.indexOf(addrNewStr.replace("-","에"))>-1){
                	color = "#ffbaba";
                }*/
                if(fullStr.indexOf(addrNewStr)>-1 || fullStr.indexOf(addrNewStr)>-1){
                	color = "#ffbaba";
                }
                
                if(color!=null){
                	recommend_html += "<li";
                	recommend_html += " style='background-color:"+color+" !important'";
                	recommend_html += "><a href='#a' onclick='textCopy(this)'  ondblclick='makeMarkerAddress(\""+businessName+"\",\""+address+"\",\""+cat+"\",\"y\",\"\",\""+pnu+"\")'>";
                	recommend_html += "<span class='fullAddress'>"+businessName+"</span>";
                	recommend_html += "<span class='badge rounded-pill' style='color: #ff0000;border: 0;float: right;margin: 0;padding: 0;background-color:transparent;'>추천</span>";
                	recommend_html += "</a></li>";
                }else{
                	address_html+="<li";
                	address_html += "><a href='#a' onclick='textCopy(this)'  ondblclick='makeMarkerAddress(\""+businessName+"\",\""+address+"\",\""+cat+"\",\"y\",\"\",\""+pnu+"\")'>"
                	address_html += "<span class='fullAddress'>"+businessName+"</span>";
                	address_html += "</a></li>";
                }
            }
            $("#address").empty();
            $("#address").append(address_html);
            
            $("#recommend").empty();
            $("#recommend").append(recommend_html);
            
            if($("#divAddress").css("display")=="none"){
				$("#divAddress").css("display","block");
			}
    	}
    	
    }
    
}

//과거통화이력
function getRecentResult(){
	var url = "/process/getRecentResult";
	var dat = {};
	dat.extId = inid;
	var json = JSON.stringify(dat);
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var data = JSON.parse(xhttp.responseText);
			
			$('#dataTable').DataTable().destroy();
			$("#dataTable").DataTable({
				columns : [
					/*{ data : "seq", orderable: false},*/
					{ data : "createDt", orderable: false},
					{ data : "callerNo", orderable: false},
					{ data : "extVoiceId", orderable: false}
				],
				order: [[ 0, "asc" ]],
				data : data,
				paging : false,
			    lengthChange : false,
			    pageLength: 5,
			    info: false,
			    filter : false,
			    columnDefs: [
			    	{ width: '60%', targets: 0},
		            { width: '40%', targets: 1},
		            { width: '0%', targets: 2, visible : false}
		        ]
			});
			
			
			var table = $('#dataTable').DataTable();
			$('#dataTable tbody').off( 'click', 'tr');
			$('#dataTable tbody').on( 'click', 'tr', function () {
				var data = table.row( this ).data();
				getDataTableRowData(data);
				
				$('.dataTableSelect').removeClass('dataTableSelect');
				this.classList.add("dataTableSelect");
			});
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//과거통화이력 특정 레코드 정보
function getDataTableRowData(data){
	var url = "/process/getDataTableRowData";
	var json = JSON.stringify(data);
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			if(xhttp.responseText==""){
				$(".keyword_card_body2").html("데이터가 존재하지 않습니다");
				$("#addressList2").html("데이터가 존재하지 않습니다");
				$(".msg_card_body2").html("데이터가 존재하지 않습니다");
				
			}else{
				var data = JSON.parse(xhttp.responseText);
				
				$(".keyword_card_body2").html(data.keywordHtml);
				$("#addressList2").html(data.addrHtml);
				$(".msg_card_body2").html(data.resultHtml);
			}
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

//선택한 주소리스트 색상 표시 및 GIS 전송 함수 호출
function makeMarkerAddress(businessName,address,cat,click,addressType,pnu){
	if(cat=="POI"){
		clickAddrSet.push(cat+"|"+businessName+"|"+address);
	}else{
		clickAddrSet.push(cat+"|"+address);
	}
	
	if(click=="y" && cat!="POI"){
		$('.active.unread').removeClass('active unread');
		for(var i=0; i<$("#menu_addrList li").length; i++){
			if($("#menu_addrList li")[i].textContent==address){
				$("#menu_addrList li")[i].classList.add("active");
				$("#menu_addrList li")[i].classList.add("unread");
			}
		}
	}
	if(click=="y" && cat=="POI"){
		$('.active.unread').removeClass('active unread');
		for(var i=0; i<$("#menu_addrList li").length; i++){
			if($($($("#menu_addrList li")[i]).find("a")).find("span.poi_address").text()==address
					&&$($($("#menu_addrList li")[i]).find("a")).find("span.poi_name").text()==businessName){
				$("#menu_addrList li")[i].classList.add("active");
				$("#menu_addrList li")[i].classList.add("unread");
			}
		}
	}
	
	if(cat=="POI"){
		cat=addressType;
	}

	if(pnu!="null"){
		setAddress("PNU", pnu, null, null, null);
	}

}

//주소를 제외한 데이터(승강기, 전신주 등) 선택한 리스트 표시 및 GIS 전송 함수 호출
function makeMarkerLocation(locationName,cat,type,code){ //cat : TEP, ELV, ICJCT, HIGHWAY, KRNO
	clickAddrSet.push(cat+"|"+locationName);

	
	$('.active.unread').removeClass('active unread');
	for(var i=0; i<$("#locationInformationCode li").length; i++){
		if($($($("#locationInformationCode li")[i]).find("a")).find("span.locationCode").text()==locationName){
			$("#locationInformationCode li")[i].classList.add("active");
			$("#locationInformationCode li")[i].classList.add("unread");
		}
	}
	
	if(type=="PNU"){
		setAddress(type, code);
	}else if(type=="GPS"){
		setAddress(type, code);
	}else if(type=="NO"){
		setAddress(type, code);
	}
}

//GIS 전송 함수
function setAddress(type, code) {
	if(type=="PNU"){
		CefSharp.PostMessage({ "Type": "setAddress", Data: { pnuCD : code }, "Callback": null });
	}else if(type=="GPS"){ 
		var lon = code.split(",")[0].trim();
		var lat = code.split(",")[1].trim();
		
		CefSharp.PostMessage({ "Type": "setCoord", Data: { lon : lon, lat : lat}, "Callback": null });
	}else if(type=="NO"){
	    CefSharp.PostMessage({ "Type": "setPole", Data: { PoleCd : code }, "Callback": null });
	}
}

//초기화
function reset(){
	$(".msg_card_body").empty();
	$("#address").empty();
	$("#poi").empty();
	$(".keyword_card_body").empty();
	$("#specialDisasterKeyword").empty();
	$("#locationInformationCode").empty();
	$("#addressBaseStation").empty();
    $("#recommend").empty();
    
	/*$("#divAddress").css("display","none");
	$("#divCode").css("display","none");*/
	$("#divDisaster").css("display","none");
	
	recogResultAll=[];
	
	
	sequence = 0;
	addrSeq = 0;
	clearInterval(addrTimer);
	addrTimer=null;
	clearInterval(getBaseStationAddrTimer);
	getBaseStationAddrTimer=null;
	
	fullAddrList = [];
	addrSet = [];
	
	
	lastThreeCount_send = [];
	lastThreeCount_receive = [];
	lastThreeResult_send = [];
	lastThreeResult_receive = [];
	noThreeResult_send = [];
	noThreeResult_receive = [];
	
	noFiveResult_send_ta = [];
	noFiveResult_receive_ta = [];
	textOverlapCheck = "";
	textOverlapCheck_send_ta = "";
	textOverlapCheck_receive_ta = "";
	
	clickAddrSet = [];
	
	lastThreeLengthCheck_send=0;
	lastThreeLengthCheck_receive=0;
	
	//getPos();

	extVoiceId = null;
	callerVoiceId = null;
	
	clearInterval(finishTimer);
	finishTimer = null;
	
	twiceReturnCheck_receive=0;
	twiceReturnCheck_send=0;
	recentResult = 1;
	voiceSearchCount = 2;
	
	locationInformationCodeList = [];
	specialDisasterKeyword = [];
	
	
	keywordNew = [];
	keywordNewSize = 0;

	
	var tabs = $('ul.tabs').children();
	$.each(tabs, function() {
		if($(this).attr('data-tab') == 'tab-1') {
			$(this).trigger('click');
		}else if($(this).attr('data-tab') == 'tab-2') {
			if($(this).hasClass("nonClickable")){
				$(this).removeClass('nonClickable');
				$(this).addClass('clickable');
			}
		}
	});
}


//텍스트 복사
function textCopy(value){
	var textArea = document.createElement("textarea");//textarea 생성
	
	if($(value).children('.fullAddress').length>0){
		textArea.value = $(value).children('.fullAddress')[0].textContent;//textarea에 텍스트 입력
		textArea.text = $(value).children('.fullAddress')[0].textContent;//textarea에 텍스트 입력
		
	}else if($(value).children('.locationCode').length>0){
		textArea.value = $(value).children('.locationCode')[0].textContent;//textarea에 텍스트 입력
		textArea.text = $(value).children('.locationCode')[0].textContent;//textarea에 텍스트 입력
	}else if($(value).children('.disasterKeyword').length>0){
		$(value).children('.disasterKeyword')[0].textContent;
	}
	
	document.body.appendChild(textArea);
	textArea.select();//선택
    var returnValue = document.execCommand("Copy");//복사
    document.body.removeChild(textArea);
    console.log(returnValue);

	setTimeout(function(){
		$('.toast').addClass('show')
	},0);
	setTimeout(function(){
		$('.toast').removeClass('show');
	},1000);
    
}



