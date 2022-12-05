var server_recog_seq = 2;
//var server_ai_seq = 1;

var monitorTime = 10000;

function getServerInfo() {
	$.ajax({
		method : "POST",
		url : "/server/recogs",
		success : function(data) {
			if (data) {
				getSvrResource();
				setInterval(getSvrResource, monitorTime);
				
				/*
				getSvrResource2();
				setInterval(getSvrResource2, monitorTime);
				*/
				
				getSvrChlStts();
				setInterval(getSvrChlStts, monitorTime);
				getTotalChannelUsage();
				setInterval(getTotalChannelUsage, monitorTime);
			}
		},
		error : function(request, status, error) {
			alert("error => " + error);
		}
	});
}

function getRecogCounts() {
	var url = '/monitor/recogcnt';
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	var today = data[0].count;
	    	var week = data[1].count;
	    	var month = data[2].count;
	    	
	    	//var max = Math.ceil(Math.max(today, week, month)/10) * 10;
	    	var max = month;
	    	
	    	/*
	    	$('#spanDaily').remove('span');
	    	$('#spanDaily').html(String.format('<span class="fill pct pct0"></span>'));
	    	$('#dailyCnt').text(String.format('통화량 {0}건', today.count));
	    	
	    	$('#spanWeekly').remove('span');
	    	$('#spanWeekly').html(String.format('<span class="fill pct pct0"></span>'));
	    	$('#weeklyCnt').text(String.format('통화량 {0}건', week.count));
	    	
	    	$('#spanMonthly').remove('span');
	    	$('#spanMonthly').html(String.format('<span class="fill pct pct0"></span>'));
	    	$('#monthlyCnt').text(String.format('통화량 {0}건', month.count));
	    	*/
	    	document.getElementById("spanDaily").textContent ='통화량'+today+'건';
            document.getElementById("dailyCnt").style.width = Math.round(today / max * 100)+"%";


            document.getElementById("spanWeekly").textContent ='통화량 '+week+'건';
            document.getElementById("weeklyCnt").style.width = Math.round(week / max * 100)+"%";

            document.getElementById("spanMonthly").textContent ='통화량 '+month+'건';
            document.getElementById("monthlyCnt").style.width = Math.round(month / max * 100)+"%";
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}

function getSvrResource() {
	var serverSeq = server_recog_seq;
	var url = String.format('/monitor/{0}/resource', serverSeq);
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	drawSvgGauge(data);
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}


function getSvrResource2() {
	/*
	var serverSeq = server_ai_seq;
	var url = String.format('/monitor/{0}/resource', serverSeq);
	*/
	var url ='/monitor/local/resource';
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	drawSvgGauge2(data);
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}

function getSvrChlStts() {
	var serverSeq = server_recog_seq;
	var url = String.format('/monitor/{0}/channels', serverSeq);
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	$("#channelBox").empty();
	    	for (var i = 0; i < data.length; i++) {
	    		if(data[i].channelStatus == 1) {
	    			$("#channelBox").append(String.format('<li><span class="img img01"></span><span>{0}</span></li>',data[i].channelNum+1));
	    		} else if(data[i].channelStatus == 2) {
	    			$("#channelBox").append(String.format('<li><span class="img img02"></span><span>{0}</span></li>',data[i].channelNum+1));
	    		} else if(data[i].channelStatus == 3) {
	    			$("#channelBox").append(String.format('<li><span class="img img03"></span><span>{0}</span></li>',data[i].channelNum+1));
	    		}
	    	}
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}

function getTotalChannelUsage() {
	var serverSeq = server_recog_seq;
	var url = String.format('/monitor/{0}/channel/usage', serverSeq);
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	if(data != null) {
	    		setGauge(data);
	    	}
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}


var chkGauge = 0;
var gauge = null;
function setGauge(data) {
	var figure = data.max;    // 데이터값
	var count = data.count;
	var figure_percent = (figure / count) * 100;  // 백분율
	//console.log(figure);
	$(".channel_use figure").text(figure);
	if(chkGauge ==0){
		var opts = {
		  angle: 0, // The span of the gauge arc
		  lineWidth: 0.36, // The line thickness
		  radiusScale: 1, // Relative radius
		  pointer: {
		    length: 0.6, // // Relative to gauge radius
		    strokeWidth: 0.080, // The thickness
		    color: '#000000' // Fill color
		  },
		  limitMax: true,     // If false, max value increases automatically if value > maxValue
		  limitMin: true,     // If true, the min value of the gauge will be fixed
		  colorStart: '#bd1521',   // Colors
		  colorStop: '#006633',    // just experiment with them
		  strokeColor: '#E0E0E0',  // to see which ones work best for you
		  generateGradient: true,
		  highDpiSupport: true,     // High resolution support
		  staticZones: [
			   {strokeStyle: "#006633", min: 0, max: count/10*1}, // Red from 100 to 130
			   {strokeStyle: "#008d36", min: count/10*1, max: count/10*2}, // Yellow
			   {strokeStyle: "#39aa35", min: count/10*2, max: count/10*3}, // Green
			   {strokeStyle: "#94c11f", min: count/10*3, max: count/10*4}, // Yellow
			   {strokeStyle: "#dedc00", min: count/10*4, max: count/10*5},  // Red
			   {strokeStyle: "#fbea10", min: count/10*5, max: count/10*6},  // Red
			   {strokeStyle: "#f8b133", min: count/10*6, max: count/10*7},  // Red
			   {strokeStyle: "#f39200", min: count/10*7, max: count/10*8},  // Red
			   {strokeStyle: "#e94e1b", min: count/10*8, max: count/10*9},  // Red
			   {strokeStyle: "#bd1521", min: count/10*9, max: count/10*10}  // Red
			],
			staticLabels: {
				  font: "15px sans-serif",  // Specifies font
				  labels: [0, count/2, count],  // Print labels at these values
				  color: "#000000",  // Optional: Label text color
				  fractionDigits: 0  // Optional: Numerical precision. 0=round off.
				},
		  // renderTicks is Optional
		  /*renderTicks: {
		    divisions: 3,
		    divWidth: 1.1,
		    divLength: 0.7,
		    divColor: '#ffffff',
		    subDivisions: 3,
		    subLength: 0.5,
		    subWidth: 0.6,
		    subColor: '#ffffff'
		  }*/			  
		};
		var target = document.getElementById('useGage'); // your canvas element
		gauge = new Gauge(target).setOptions(opts); // create sexy gauge!
		gauge.maxValue = count; // set max gauge value
		gauge.setMinValue(0);  // Prefer setter over gauge.minValue = 0
		gauge.animationSpeed = 32; // set animation speed (32 is default value)
	}
	chkGauge = 1;
	
	gauge.set(figure); // set actual value
	
	/*$('.gauge').gauge({
		values: {
			0 : '0',
			10: '',
			20: '',
			30: '',
			40: '',
			50: '',
			60: '',
			70: '',
			80: '',
			90: '',
			100: count
		},
		colors: {
			0 : '#006633',
			10 : '#ffffff',
			11 : '#008d36',
			20: '#ffffff',
			21: '#39aa35',
			30: '#ffffff',
			31: '#94c11f',
			40: '#ffffff',
			41: '#dedc00',
			50: '#ffffff',
			51: '#fbea10',
			60: '#ffffff',
			61: '#f8b133',
			70: '#ffffff',
			71: '#f39200',
			80: '#ffffff',
			81: '#e94e1b',
			90: '#ffffff',
			91: '#bd1521'
		},
		angles: [
			180,
			360
		],
		lineWidth: 30,
		arrowWidth: 30,
		arrowColor: '#1e272d',
		inset:true,
		
		value:figure_percent
	});*/
}


function drawSvgGauge(data) {
	updateDonutChart('#specificCPUChart', data.perCpu, true); //CPU
	updateDonutChart('#specificRAMChart', data.perMem, true); //RAM
	updateDonutChart('#specificHDDChart', data.perDsk, true); //HDD
}

function drawSvgGauge2(data) {
	updateDonutChart('#specificCPUChart2', data.perCpu, true); //CPU
	updateDonutChart('#specificRAMChart2', data.perMem, true); //RAM
	updateDonutChart('#specificHDDChart2', data.perDsk, true); //HDD
}


function getDetectKeywordStatistics() {
	//var gap = $("#ulSearchGap").siblings('a').find('.txt').attr('id');
	var gap = $("#ulSearchGap").val();
	//var gap = '30';
	
    var url = String.format('/monitor/statistics?gap={0}', gap);
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	$('#tagCloud').jQCloud('destroy');
	    	if(data == null) {
	    		alert("데이타가 없습니다.");
	    	} else {
	    		var cloud = data.cloud;
	    		var statistics = data.statistics;
	    		$("#tagCloud").jQCloud(cloud);
	    		/*
	    		$("#detectList").empty();
	    		var html = '';
	    		for(var i=0 ; i < statistics.length ; i++) {
	    			html += '<li>';
	    			html += '<dl>';
	    			html += String.format('<dt>{0}</dt>', statistics[i].kwrdNm);
	    			html += '<dd>';
	    			html += String.format('<span class="fill pct pct{0}"><p>{1}건</p></span>', statistics[i].kwrdRatio, statistics[i].kwrdCnt);
	    			html += '</dd>';
	    			html += '</dl>';
	    			html += '</li>';
	    		}
	    		$("#detectList").html(html);
	    		*/
	    	}
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}

$(document).ready(function() {
	
	$(".dashboardPage").css('color','rgba(0,0,0,.8)');
	
	getRecogCounts();
	setInterval(getRecogCounts, monitorTime);
	
	getServerInfo();

	getDetectKeywordStatistics();
	setInterval(getDetectKeywordStatistics, monitorTime);
	
	$("#ulSearchGap").change(function (){
		getDetectKeywordStatistics();
	});
});

/*
$(window).on("load",function(){
	
	getServerInfo();
	
	$("#ulSearchGap").change(function (){
		getDetectKeywordStatistics();
	});
	
	
	getDetectKeywordStatistics();
	setInterval(getDetectKeywordStatistics, monitorTime);
});
*/

