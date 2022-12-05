var gListCount = 1;
var gSearchArray = {};

var playObj = 'audioPlayObj';
var voiceInfoPath;
var voiceStreamPath;
var recogResultPath;
var voiceInfo;
var wavesurfer;
var currentP = 0;

var indexI1=1;
var indexI2=1;

var recogResultCount;
var voiceId;

var currentPage_g;
var dataPerPage;
var totalDataCount_g;



var voiceId2;
var voiceInfoPath2;
var voiceStreamPath2;
var recogResultPath2;
var recogResultCount2;
var wavesurfer2;

var interval1;
var interval2;

var fileDownloadCheck="";


function setRecogList(pageCount, pageNo) {
	fileDownloadCheck = "";
	
	var todayStr = dateFns.format(new Date(), "YYYY-MM-DD");
	currentPage_g = pageNo;
	dataPerPage = pageCount;
	
	
	gSearchArray = {};
	gListCount = 1;
	$("#listTbody").html("");

	gSearchArray["extNo"] = inid;
	
	gSearchArray["pageCount"] = pageCount;
	gSearchArray["pageNo"] = pageNo;
	gSearchArray["searchStart"] = $("#datepicker1").datepicker().val();
	gSearchArray["searchEnd"] = $("#datepicker2").datepicker().val();
	if (!gSearchArray["searchStart"]) gSearchArray["searchStart"] = todayStr;
	if (!gSearchArray["searchEnd"]) gSearchArray["searchEnd"] = todayStr;
	
	var h = $("#hourSelect").siblings("a").find(".txt").attr("id");
	h = h.replace("h_","");
	gSearchArray["startHour"] = h.slice(0,2);
	gSearchArray["endHour"] = h.slice(2,4);
	
	
	
	var s = new Date(gSearchArray["searchStart"]);
	var e = new Date(gSearchArray["searchEnd"]);
	
	var now = new Date();
	var bt2 = now.getTime()-s.getTime();
	if(Math.abs(bt2/(1000*60*60*24))>30){
		alert("검색은 최근 한달까지만 가능합니다");
		return;
	}
	
	var bt = e.getTime()-s.getTime();
	if(Math.abs(bt/(1000*60*60*24))>30){
		alert("검색은 최근 한달까지만 가능합니다");
		return;
	}
	
	commonFunction.spinnerShow();
	
	addRecogList();
}

function addRecogList() {
	$.ajax({
		method : "POST",
		url : "/voices",
		data : JSON.stringify(gSearchArray),
		contentType : "application/json; charset=UTF-8",
		success : function(data) {
			console.log(data.list);
			if(data.count == 0) {
				$("#listTbody").empty();
				alert("조회 결과가 없습니다.");
				commonFunction.spinnerClose();
				return (false);
			}
			var tbodyHtml = "";
			var totalCnt = data.count; // 전체 로우 수
			totalDataCount_g = totalCnt;
			
			var totalPage = Math.ceil(data.count / gSearchArray["pageCount"]); // 전체 페이지 수
			var searchCnt = data.list.length; // 검색된 로우 수

			for (var i = 0; i < searchCnt; i++) {
				++gListCount;
				//tbodyHtml += "<tr onclick='loadDetailPage(\"" + data.list[i].voiceId + "\")'>";
				tbodyHtml += "<tr onclick='loadDetailPageBefore(\"" + data.list[i].rxId + "\",\""+ data.list[i].txId + "\",\""+data.list[i].creatDt+"\")'>";
				tbodyHtml += "<td>" + data.list[i].callNo + "</td>";
				tbodyHtml += "<td>" + data.list[i].creatDt + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#listTbody").empty();
			$("#listTbody").html(tbodyHtml);
			
			paging();
			
			commonFunction.spinnerClose();
		},
		error : function(request, status, error) {
			alert("error => " + error);
			
			commonFunction.spinnerClose();
		}
	});
}


function loadDetailPageBefore(rxId, txId, creatDt){
	fileDownloadCheck = rxId+"|"+txId+"|"+creatDt;
	
	loadDetailPage(rxId); //수신
	loadDetailPage2(txId); //발신
	
}



function loadDetailPage2(voiceId2) {
	voiceInfoPath2 = "/voices/"+ voiceId2;
	voiceStreamPath2 = voiceInfoPath2 + "/load";
	recogResultPath2 = "/recog/" + voiceId2;
	
	$("#recogResult2").mCustomScrollbar('destroy');

	$("#waveform2").empty();
	$("#recogResult2").empty();
	
	getVoiceInfo2();
	getVoiceStream2();
	getRecogResult2();
		
}


function getVoiceInfo2() {
	$.ajax({
		url: voiceInfoPath2,
		type: "get",
		success: function(data, status, jqxhr) {
			voiceInfo = data;
			makeWaveSurfer2();
		},
		error: function(jqxhr, status, err) {
			console.error(err);
		}
	});
}

function getVoiceStream2() {
	$.ajax({
		url: voiceStreamPath2,
		type: "get",
		success: function(data, status, jqxhr) {
		},
		error: function(jqxhr, status, err) {
			console.error(err);
		}
	});
}

function getRecogResult2() {
	$.ajax({
		url: recogResultPath2,
		type: "get",
		success: function(data, status, jqxhr) {
			console.log(data);
			var tbody = $("#recogResult2").empty();
			for (var i = 0; i < data.length; i++) {
				tbody.append("<p id='cnResult2_" + (i + 1)
						+ "' title='" + (data[i].voiceStartTime/100)
						+ "' onclick='playerSeekTo(" + (data[i].voiceStartTime/100) + ",this); return false;'>"
						+ data[i].recogResultText + "</p>");
			}
			recogResultCount2 = data.length - 0;
			
			$("#recogResult2").mCustomScrollbar({
				autoHideScrollbar:true,
				theme:"light-thin",
				advanced:{
					updateOnContentResize: true,
					autoScrollOnFocus: false
				} 
			});
			
			if($("#recogResult2").find(".current").length==0){
				$("#cnResult2_").addClass("current");
			}
		},
		error: function(jqxhr, status, err) {
			console.error(err);
		}
	});
}

function makeWaveSurfer2() {
	wavesurfer2 = WaveSurfer.create({
		container: document.querySelector("#waveform2"),
		height: 145,
		fillParent: true,
		interact: true,
		cursorColor: "#FFFFFF",
		cursorWidth: 2,
		progressColor: "#00ff8e",
		waveColor: "#bfbfbf"
	});
	wavesurfer2.load(voiceStreamPath2);
	wavesurfer2.on("audioprocess", function () {
		var currentTime = wavesurfer2.getCurrentTime();
        var resultTime1 = 0;
        var resultTime2 = 0;
        var offset = 235;

        for(var indexI = 1; indexI <= recogResultCount2; indexI++) {
        	var resultTime1 = $("#cnResult2_" + indexI).attr('title') - 0;
        	var resultTime2 = $("#cnResult2_" + (indexI + 1)).attr('title') - 0;
        	if (resultTime1 <= currentTime && resultTime2 > currentTime) {
        		if (currentP != indexI) {
        	        for(var clearIndex = 1; clearIndex <= recogResultCount2; clearIndex++){
        	        	$("#cnResult2_" + clearIndex).attr('class','');
        	        }
        			$("#cnResult2_" + indexI).attr("class", "current");
        			indexI2 = indexI;
        			/*
        			var elTop = $("#cnResult2_" + indexI).offset().top - $("#record_txtBox2 .mCSB_container").offset().top;
        			if (elTop < offset) {
        				$("#recogResult2").mCustomScrollbar("scrollTo", 0);
        			} else {
        				$("#recogResult2").mCustomScrollbar("scrollTo", elTop - offset);
        			}*/
        			
            		currentP = indexI;
            		break;
        		}
        	}
        }
	});

	wavesurfer2.on("play", function () {
		$('.ctr_play').hide();
		$('.ctr_pause').show();
		
		interval2 =	setInterval(intervalTest2, 1000);
	});

	wavesurfer2.on("pause", function () {
		$('.ctr_play').show();
		$('.ctr_pause').hide();
		

		clearInterval(interval2);
		interval2 = null;
	});

	wavesurfer2.on("finish", function () {
		$("ul.gr_control_1 > li:nth-child(2) > a").css("background", "url(/resources/img/audio_play.png) no-repeat center center");
		$("#recogResult2").find(".current").removeClass("current");
	});
}



function playerPlay2() {
	
	wavesurfer2.playPause();
}

function playerStop2() {
	wavesurfer2.stop();
	$(wavesurfer2).trigger("finish");
	
	$("#recogResult2").find(".current").removeClass("current");
	$("#cnResult2_1").addClass("current");
	
	$("#recogResult2").mCustomScrollbar("scrollTo", 0);
}

function playerSeekTo2(sec, obj) {
	$("#recogResult2").find(".current").removeClass("current");
	wavesurfer2.seekTo(sec/wavesurfer2.getDuration());
	$(obj).addClass("current");
}


function loadDetailPage(voiceId) {
	voiceInfoPath = "/voices/"+ voiceId;
	voiceStreamPath = voiceInfoPath + "/load";
	recogResultPath = "/recog/" + voiceId;

	$("#recogResult").mCustomScrollbar('destroy');
	
	
	$("#waveform").empty();
	$("#recogResult").empty();
	
	getVoiceInfo();
	getVoiceStream();
	getRecogResult();
	$(".icon_volume").on("click",function(){ // 재생
		$(".icon_volume").hide();
		$('.icon_volume_off').show();
		$(".volume_cover").css({display:"none"})
		wavesurfer.setMute(true);
		return false
	})
	$(".icon_volume_off").on("click",function(){ //일시정지
		$('.icon_volume_off').hide();
		$(".icon_volume").show();
		$(".volume_cover").css({display:"block"})
		wavesurfer.setMute(false);
		return false
	});
	
	
}





function getVoiceInfo() {
	$.ajax({
		url: voiceInfoPath,
		type: "get",
		success: function(data, status, jqxhr) {
			voiceInfo = data;
			makeWaveSurfer();
		},
		error: function(jqxhr, status, err) {
			console.error(err);
		}
	});
}

function getVoiceStream() {
	$.ajax({
		url: voiceStreamPath,
		type: "get",
		success: function(data, status, jqxhr) {
		},
		error: function(jqxhr, status, err) {
			console.error(err);
		}
	});
}

function getRecogResult() {
	$.ajax({
		url: recogResultPath,
		type: "get",
		success: function(data, status, jqxhr) {
			console.log(data);
			var tbody = $("#recogResult").empty();
			for (var i = 0; i < data.length; i++) {
				tbody.append("<p id='cnResult_" + (i + 1)
						+ "' title='" + (data[i].voiceStartTime/100)
						+ "' onclick='playerSeekTo(" + (data[i].voiceStartTime/100) + ",this); return false;'>"
						+ data[i].recogResultText + "</p>");
			}
			recogResultCount = data.length - 0;
			
			$("#recogResult").mCustomScrollbar({
				autoHideScrollbar:true,
				theme:"light-thin",
				advanced:{
					updateOnContentResize: true,
					autoScrollOnFocus: false
				} 
			});
			
			if($("#recogResult").find(".current").length==0){
				$("#cnResult_1").addClass("current");
			}
		},
		error: function(jqxhr, status, err) {
			console.error(err);
		}
	});
}




function makeWaveSurfer() {
	wavesurfer = WaveSurfer.create({
		container: document.querySelector("#waveform"),
		height: 145,
		fillParent: true,
		interact: false,
		cursorColor: "#FFFFFF",
		cursorWidth: 2,
		progressColor: "#00ff8e",
		waveColor: "#bfbfbf"
	});
	wavesurfer.load(voiceStreamPath);
	wavesurfer.on("audioprocess", function () {
		var currentTime = wavesurfer.getCurrentTime();
        var resultTime1 = 0;
        var resultTime2 = 0;
        var offset = 235;

        for(var indexI = 1; indexI <= recogResultCount; indexI++) {
        	var resultTime1 = $("#cnResult_" + indexI).attr('title') - 0;
        	var resultTime2 = $("#cnResult_" + (indexI + 1)).attr('title') - 0;
        	if (resultTime1 <= currentTime && resultTime2 > currentTime) {
        		if (currentP != indexI) {
        	        for(var clearIndex = 1; clearIndex <= recogResultCount; clearIndex++){
        	        	$("#cnResult_" + clearIndex).attr('class','');
        	        }
        			$("#cnResult_" + indexI).attr("class", "current");
        			
        			indexI1 = indexI;
        			
        			/*
        			var elTop = $("#cnResult_" + indexI).offset().top - $("#record_txtBox1 .mCSB_container").offset().top;
        			if (elTop < offset) {
        				$("#recogResult").mCustomScrollbar("scrollTo", 0);
        			} else {
        				$("#recogResult").mCustomScrollbar("scrollTo", elTop - offset);
        			}
        			*/
            		currentP = indexI;
            		break;
        		}
        	}
        }
	});

	wavesurfer.on("play", function () {
		$('.ctr_play').hide();
		$('.ctr_pause').show();
		
		
		interval1 =	setInterval(intervalTest, 1000);
	});

	wavesurfer.on("pause", function () {
		$('.ctr_play').show();
		$('.ctr_pause').hide();
		

		clearInterval(interval1);
		interval1 = null;
	});

	wavesurfer.on("finish", function () {
		$("ul.gr_control_1 > li:nth-child(2) > a").css("background", "url(/resources/img/audio_play.png) no-repeat center center");
		$("#recogResult").find(".current").removeClass("current");
	});

	$("#volumebar").width(50);
	$("#volumebar").trigger("mousedown");
}


function intervalTest(){
	var offset = 235;
	
	var elTop = $("#cnResult_" + indexI1).offset().top - $("#record_txtBox1 .mCSB_container").offset().top;
	if (elTop < offset) {
		$("#recogResult").mCustomScrollbar("scrollTo", 0);
	} else {
		$("#recogResult").mCustomScrollbar("scrollTo", elTop - offset);
	}
}


function intervalTest2(){
	var offset = 235;
	
	var elTop = $("#cnResult2_" + indexI2).offset().top - $("#record_txtBox2 .mCSB_container").offset().top;
	if (elTop < offset) {
		$("#recogResult2").mCustomScrollbar("scrollTo", 0);
	} else {
		$("#recogResult2").mCustomScrollbar("scrollTo", elTop - offset);
	}
}





function playerPlay() {
	
	wavesurfer.playPause();
	playerPlay2();
}

function playerStop() {
	wavesurfer.stop();
	$(wavesurfer).trigger("finish");
	
	$("#recogResult").find(".current").removeClass("current");
	$("#cnResult_1").addClass("current");
	
	
	$("#recogResult").mCustomScrollbar("scrollTo", 0);
	
	playerStop2();
	
	clearInterval(interval1);
	interval1 = null;
	
	clearInterval(interval2);
	interval2 = null;
}

function playerSeekTo(sec, obj) {
	$("#recogResult").find(".current").removeClass("current");
	wavesurfer.seekTo(sec/wavesurfer.getDuration());
	$(obj).addClass("current");
	
	playerSeekTo2(sec, obj);
}


function setVol(vol) {
	console.log("setVol " + vol)
	wavesurfer.setVolume(vol);
	$(".volume_cover").css("display", "block");
	$(".volume_cover").css("width", (vol * 100) + "%");
	$(".volume_cover span").css("display", "none");
	if (vol >= 0.2) {
		$(".volume_cover").find("span:eq(0)").css("display", "block");
	}
	if (vol >= 0.4) {
		$(".volume_cover").find("span:eq(1)").css("display", "block");
	}
	if (vol >= 0.6) {
		$(".volume_cover").find("span:eq(2)").css("display", "block");
	}
	if (vol >= 0.8) {
		$(".volume_cover").find("span:eq(3)").css("display", "block");
	}
	if (vol == 1) {
		$(".volume_cover").find("span:eq(4)").css("display", "block");
	}
}

$("#volumebar_bg").mousedown(function(e) {
	if (e.clientX) {
		var _selectPointer = $(this);
		var _pointX = parseInt(e.clientX) - parseInt(_selectPointer.offset().left);
		var _currentWidth = $("#volumebar_bg").width();

	    var _clientX = _pointX;
		if (_clientX >= _currentWidth) _clientX = _currentWidth;
		if (_clientX <= 0) _clientX = 0;
		$("#volumebar").width(_clientX);
		wavesurfer.setVolume(_clientX/_currentWidth);
	}

	return false;
});



function paging(){
	var pageCount = 5;
  
	var totalPage = Math.ceil(totalDataCount_g/dataPerPage);
	var pageGroup = Math.ceil(currentPage_g/pageCount); 
  
	var last = pageGroup * pageCount;
	if(last > totalPage)
		last = totalPage;
	
	var first = 1+(5*(pageGroup-1));
	
	var next = last + 1;
	var prev = first - 1;
  
	if(totalPage < 1)
		first = last;
	
	
	var html = "";
	
	if(first > pageCount){
		html += "<li class='page-item'><a class='page-link' href='#' onclick='setRecogList(12,"+prev+")'>Prev</a></li>";
	}else{
		html += "<li class='page-item disabled'><a class='page-link' href='#'>Prev</a></li>";
	}
	for(var j=first; j<=last; j++){
		if(currentPage_g == j){
			html += "<li class='page-item active' aria-current='page'><a class='page-link' href='#' onclick='setRecogList(12,"+j+")'>"+j+"</a></li>";
		}else if(j>0){
			html += "<li class='page-item'><a class='page-link' onclick='setRecogList(12,"+j+")' href='#'>"+j+"</a></li>";
		}
	}
	//if(next>pageCount && next<totalPage){
	if(next>pageCount && next<=totalPage){
		html += "<li class='page-item'><a class='page-link' onclick='setRecogList(12,"+next+")' href='#'>Next</a></li>";
	}else{
		html += "<li class='page-item disabled'><a class='page-link' onclick='setRecogList(12,"+next+")' href='#'>Next</a></li>";
	}
	
	
	var pages = $("#pagination");
	pages.empty();
	pages.append(html);
}





function voiceFileDownload(){
	if(fileDownloadCheck==""){
		alert("다운로드 할 음성파일을 선택해주세요.");
	}else{
		
		var array = fileDownloadCheck.split("|");
		if(confirm(array[2]+"의 음성파일을 다운로드 하시겠습니까?")){
			
			var url = "/voices/download?voiceId="+array[0];
			$('#downlink').attr('src',url);
			
			
			
			
			url = "/voices/download?voiceId="+array[1];
			setTimeout(function(){
				$('#downlink').attr('src',url);
			},200);
			
		}
	}
	
}



$(document).ready(function() {
	$("#hourSelect li").on("click", function(e) {
		var txt = $(this).find("a").text();
		var id = $(this).attr("id");
		$(this).parent().parent().parent("ul").siblings("a").find(".txt").text(txt);
		$(this).parent().parent().parent("ul").siblings("a").find(".txt").attr('id',id);
		$(this).parent().parent().parent("ul").css({display:"none"})
		$(this).parent().parent().parent().parent(".selectBox").removeClass("on");
		return false;
	});
	$("#datepicker1").datepicker("setDate", new Date());
	$("#datepicker2").datepicker("setDate", new Date());
	$("#searchKeyword").val('');
	
	
	$("#searchKeyword").keypress(function(e) { 
	    if (e.keyCode == 12){
	    	setRecogList(12, 1);
	    }    
	});
	
});

$(window).on("load", function() {
	/*
	$("#listContainer").mCustomScrollbar({
		callbacks : {
			onTotalScroll : function() {
				++gSearchArray["pageNo"];
				addRecogList();
			},
		}
	});
	*/
	setRecogList(12, 1);
	
	//paging();
});


