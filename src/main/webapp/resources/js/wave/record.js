/*** Web Audio context ***/
var ctx;
var TARGET_SAMPLE_RATE = 8000;
var channelId;
var keywordTimer;
var pb;
try { 
    ctx = new AudioContext();
} catch (e) {
    try {
	    ctx = new webkitAudioContext();
    } catch (e) {
	alert("Web Audio init failed: "+e);
    }
} 

/*** Other local variables ***/
var rec = []; /* recorded samples */
var dirty = false; /* whether we have new audio */

function gebi(el) { return document.getElementById(el); }
var posbar;
var recording = false;
var recog = false;

function record () {
	if(voiceId == null) {
		alert("인식 파일 생성을 먼저 해주세요.");
		return false;
	} else {
	    if (!playback.playing) {
	    	connect();
	    	keywordTimer = setInterval(checkTextArea, 3000);
	//    	recording = true;
	    }
	}
}

function stop () {
    if (recording)
    	recording = false;
    
    sendFinish();
    clearInterval(keywordTimer);
    
    if (playback.playing)
    	playback.stop();
}

function play () {
    if (playback.playing || recording)
	return;

    if (!playback.start(rec, finished))
	return;
    if(wave.getbar() == null) {
    	posbar = wave.addbar(0, 'blue', 0);
    } else {
    	posbar = wave.getbar();
    }

    /* when we're playing connect up the monitor to the output rather
     * than the input.  Sadly this runs afoul of crbug.com/176808. */
}

function score() {
	var ppl = '11.30';
	$('#confirm_keyword').val(ppl);
}

function finished() {
    wave.removebar(posbar);
    posbar = null;
}

function clear() {
    stop();
    rec.length = 0;
    wave.invalidate();
}

var buf = null;
var myBuffer = null;

/* a buffer full of audio awaits us */ 
function audio(e) {
	var inputSource = e.inputBuffer;
    buf = e.inputBuffer.getChannelData(0);
    
    if (recording) {
    	for (var i=0; i<buf.length; i++) {
		    rec.push(buf[i]);
    	}
    	sendAudio(downsampleBuffer(buf, ctx.sampleRate, TARGET_SAMPLE_RATE));
	}
    dirty = true;
}

function downsampleBuffer(buffer, sampleRate, outSampleRate) {
	if(sampleRate == outSampleRate) {
		return buffer;
	}
	
	if(sampleRate < outSampleRate) {
		throw "downsampling rate show bo smaller than original sample rate.";
	}
	
	var sampleRateRatio = sampleRate / outSampleRate;
	var newLength = Math.round(buffer.length / sampleRateRatio);
	var result = new Int16Array(newLength);
	var offsetResult = 0;
	var offsetBuffer = 0;
	while(offsetResult < result.length) {
		var nextOffsetBuffer = Math.round((offsetResult + 1) * sampleRateRatio);
		var accum = 0, count = 0;
		for(var i = offsetBuffer ; i < nextOffsetBuffer && i < buffer.length ; i++ ) {
			accum += buffer[i];
			count++;
		}
		result[offsetResult] = Math.min(1, accum / count) * 0x7FFF;
		offsetResult++;
		offsetBuffer = nextOffsetBuffer;
	}
	return result.buffer;
}

function convertTypeArray(src, type) {
	var buffer = new ArrayBuffer(src.byteLength);
	var baseView = new src.constructor(buffer).set(src);
	return new type(buffer);
}

/* add callbacks */
/*
var callbacks = {
	create: create,
    start: record,
    stop: stop,
    clear: clear,
    play: play,
    zin: function() { wave.zoomby(200); },
    zout: function() { wave.zoomby(50); },
    fit: function() { wave.zoomby(0); },
    i: function() {wave.invalidate(); },
};
*/

var callbacks = {
    start: record,
    stop: stop
//    ,
//    btnScore:score
};

for (var i in callbacks) {
    if (callbacks.hasOwnProperty(i))
    	gebi(i).addEventListener('click', callbacks[i], false);
}

var autozoom;
// var azel=gebi("autozoom");
// (azel.onchange = function () { autozoom=azel.checked; })();

function connect() {
	var serverSeq = $('#selServer').val();
	var modelSeq = $('#selModel').val();
	
	var url = "/audio?voiceId="+voiceId+"&serverSeq="+serverSeq+"&modelSeq="+modelSeq;
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200) {
			console.log(this.responseText);
			if(this.responseText == 'FAIL') {
				alert("연결에 실패하였습니다.");
				return (false);
			}
			channelId = this.responseText;
			recording = true;
		}
	};
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "text/plain");
	xhttp.send();
}

/*** Set up audio input ***/
(navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia)
    .call(navigator, { audio: true }, startmedia, function(e) {
			  alert('No live audio input: ' + e);
		       });

var node;
var input;
var monitor = (ctx.createScriptProcessor ||
		ctx.createJavaScriptNode)
	.call(ctx,
	      2048, /* buffer size */
	      1,    /* channels in */
	      1     /* channels out */
	     );
monitor.connect(ctx.destination); /* I have no idea why this is needed */
monitor.onaudioprocess = audio;
var bufferLength;
var dataArray;
function startmedia(stream) {
    input = ctx.createMediaStreamSource(stream);
    input.connect(monitor);
}

function sendAudio(buffer) {
	var url = "/audio/"+voiceId+"/?channel="+channelId;
	if(recording) {
		var xhttp = new XMLHttpRequest();
		var convBuf = null;
		if(buffer.constructor === Float32Array) {
			convBuf = convertTypeArray(buffer, Uint8Array);
		}
		xhttp.onreadystatechange = function() {
			if(xhttp.readyState == 4 && xhttp.status == 200) {
				var recogText = xhttp.responseText;
				if(recogText.length > 0) {
					getParsingText(recogText);
				}
			}
		};
		xhttp.open("POST", url, true);

		if(convBuf != null) {
			xhttp.send(convBuf);
		} else {
			xhttp.send(buffer);
		}
	}
}

function getParsingText(data) {
	if(data == null) {
		return false;
	}
	if(data.length <= 0) {
		return false;
	}
	if(data.indexOf("<s>") == -1) {
		return false;
	} else {
		var dat = {};
		dat.tag = ',';
		dat.recogResult = data;
		var url = '/recog/'+voiceId;
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if(xhttp.readyState == 4 && xhttp.status == 200) {
				var recogText = xhttp.responseText;
				console.log(recogText);
				recogText = replaceAll(recogText, ',', '<br>');
				if(recogText.length > 0) {
					$('#recogRs').empty();
					$('#recogRs').html(recogText);
				}
			}
		};
		var json = JSON.stringify(data);
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send(json);
	}
	
}

function checkTextArea() {
	if($('#recogRs').text().length > 5) {
		var url = '/kwrds/'+voiceId+'/detect?recogContents='+$('#recogRs').text();
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if(xhttp.readyState == 4 && xhttp.status == 200) {
				retreiveSymbol();
		    	showPoi($('#recogRs').text());
			}
		};
		xhttp.open("GET", url, true);
		xhttp.setRequestHeader("Content-type", "text/plain");
		xhttp.send();
	}
}

function retreiveSymbol() {
	var url = '/detect/'+voiceId;
	$.ajax({
	    url: url,
	    dataType: 'json',
	    type: 'GET',
	    contentType: 'application/json',
	    processData: false,
	    success: function( data, textStatus, jQxhr ){
	    	showDetectKeywords(data);
	    },
	    error: function( jqXhr, textStatus, errorThrown ){
	        console.log( errorThrown );
	    }
	});
}

function showDetectKeywords(data) {
	$('#key_1_sub').empty();
	$('#key_2_sub').empty();
	$('#key_3_sub').empty();
//	var fire = $("input[name=hdTxt_Fire]:hidden");
//	var rescue = $("input[name=hdTxt_Rescue]:hidden");
//	var firstAid = $("input[name=hdTxt_Firstaid]:hidden");
	
	var keywords = '';
	for(var i=0 ; i < data.length ; i++) {
		if(data[i].ctgryId == "CR0001") {
			$('#key_1_sub').html(data[i].kwrdNm);
			var fireTxt = data[i].kwrdNm;
			fireKeyword = replaceAll(fireTxt, ',', '');
			keywords += data[i].kwrdNm + ',';
		}
		if(data[i].ctgryId == "CR0002") {
			$('#key_2_sub').html(data[i].kwrdNm);
			var rescueTxt = data[i].kwrdNm;
			rescueKeyword = replaceAll(rescueTxt, ',', '');
			keywords += data[i].kwrdNm + ',';
		}
		if(data[i].ctgryId == "CR0003") {
			$('#key_3_sub').html(data[i].kwrdNm);
			var firstAidTxt = data[i].kwrdNm;
			firstAidKeyword = replaceAll(firstAidTxt, ',', '');
			keywords += data[i].kwrdNm + ',';
		}
	}
	mark();
	
	var subKeyword = keywords.substring(0, (keywords.length-1))
	var url = '/info?keywords='+subKeyword;
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200) {
			showManual(this.responseText);
		}
	};
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "text/plain");
	xhttp.send();
}

function showManual(data) {
	$('.menual_fire').hide();
	$('.menual_high_fever').hide();
	$('.menual_fracture').hide();
	$('.menual_machine_intervention').hide();
	$('.menual_drinker').hide();
	$('.menual_car_intervention').hide();
	if(typeof(data) !='undefined') {
		$('.menual_plain').hide();
		$('.menual_'+data).show();
		$('.menual_'+data).css("height", "210px");
		if(data == 'blank') {
			$('#blank').show();
			$('#telList').hide();
			$('#plainTelList').hide();
		} else {
			$('#blank').hide();
			$('#telList').show();
			$('#plainTelList').hide();
		}
	} else {
		$('#plainTelList').show();
		$('#blank').hide();
		$('#telList').hide();
	}
}

var popupWindow;

function showPoi(query) {
	var top = window.screenTop + 405;
	var left = window.screenLeft + 1000;
	var url = "/poiinfo?keywords=" + query;
	$.ajax({
		url: url,
		type: "GET",
		success: function (data, textStatus, jQxhr) {
			console.log("poi response : " + data);
			if (data != null) {
				if (!popupWindow || popupWindow.closed) {
					popupWindow = window.open("daummap", "검색 결과 : " + data, "width=850, height=500, top=" + top + ", left=" + left);
					popupWindow.searchQuery = data;
					popupWindow.searchQueryCount = 1;
				} else {
					popupWindow.name = "검색 결과 : " + data;
					popupWindow.searchQuery = data;
					popupWindow.searchQueryCount = 1;
					popupWindow.search();
				}
			}
		},
		error: function(jqXhr, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
}

function sendFinish() {
	var url = "/audio/"+voiceId+"/fin?channel="+channelId;
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200) {
			getRecogText();
			/*
			var recogText = this.responseText;
			if(recogText.length > 0) {
				$('#recogRs').val(recogText);
			}
			*/
		}
	};
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "text/plain");
	xhttp.send();
	$("#popup3").bPopup();
    pb = new ProgressBar.Line('#popup3_percent', {
   	 strokeWidth: 8,
   	 easing: 'easeInOut',
   	 color: '#0091d6',
   	 trailColor: '#eee',
   	 trailWidth: 8
    });
    var progressInt = setInterval(progress, 1000);
    
}

function progress() {
	pb.set(0);
    pb.animate(1);
}

function getRecogText() {
	var url = "/recog/"+voiceId+"/txt";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(this.readyState == 4 && this.status == 200) {
			var recogText = this.responseText;
			if(recogText.length > 0) {
//				$('#recogRs').val(recogText);
				$('#micDiv').hide();
				$('#playerDiv').show();
				$('#realtimeDiv').hide();
				$('#resultDiv').show();
				getVoiceInfo();
				getVoiceStream();
				getRecogResult();
				recog = true;
				if(escapeFlg) {
					window.location.href = '/voice';
				}
			}
		}
	};
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-type", "text/plain");
	xhttp.send();
}



function f2i(floats) {
	var ints=[];
	var count = floats.length;
	for (var i = 0; i < count; i++) {
		ints[i] = floats[i] < 0 ? floats[i] * 32768 : floats[i] * 32767;   
	}
	return ints;
}

/*** Waveform object ***/
var wave = new Wave(gebi("waveform"), {
	buf: rec,
	zoom: 1/500, /* 500 samples per pixel */
	bound: false,
	waveStyle: '#4bf3a7',
	undefStyle: 'black',
//			axisStyle: 'black',
//			axisStyle:'#0083ca',
	/*waveStyle: 'indigo',
	undefStyle: 'white',*/

/* replace waveform with points at >5 pixels per sample */
	pointFunc: Wave.Lollipops,
	pointThreshold: 5,
	waveThreshold: 5,
});

/* Make a stripy pattern (because we can) */
function pattern() {
    var c = document.createElement("canvas");
    var ctx = c.getContext("2d");
    c.width = c.height = 20; /* adjust to change stripe distance */
    ctx.scale(c.width/4,c.height/4);
    ctx.fillStyle="#555";
    ctx.fillRect(0,0,4,4);
    ctx.fillStyle="#aaa";
    ctx.transform(1,-1,1,1,0,0);
    ctx.fillRect(-3,0,6,1);
    ctx.fillRect(-3,2,6,1);
    return c;
}

/* update the waveform */
function update () {
    if (dirty) {
		dirty = false;
	
		if (recording) {
		    /* scroll if we need to */
		    var scrolled = wave.scrollto(rec.length, 0, 100);
		    
		    if (scrolled && autozoom)
			wave.zoomby(80); /* zoom out if we need to */
		    
		    /* draw new data */
		    wave.draw();
		}
	
		/* draw meter */
		var sum = 0;
		for (var i=0; i<buf.length; i++)
		    sum += buf[i] * buf[i];
		sum /= buf.length;
		/* this isn't in units of anything in particular */
		var vu = sum && Math.log(sum)/Math.log(10) / 10 + 1;
		vo = vu < 0 ? 0 : vu;
    }
    if (playback.playing) {
		/* set the bar to the current playback position */
		var pos = playback.offset();
		posbar.setpos(pos);
	
		/* and make sure it's in view */ 
        wave.scrollto(pos, 5, 5);
    }

    window.requestAnimationFrame(update);
}

/* kick off the updates */
window.requestAnimationFrame(update);


/*** Playback object ***/
var playback = (function (ctx) {
     var playback = {
		 playing: false,
		 timestarted: undefined,
		 node: null,
		 ctx: ctx
     };

     playback.offset = function() {
		 if (!this.playing)
		     return undefined;
		 
		 return (this.ctx.currentTime - this.timestarted) * this.ctx.sampleRate;    
     };    

     /* start playback */
     playback.start = function(buf, cb) {
		 if (!buf || !buf.length)
		     return false;
		 this.node = this.ctx.createBufferSource();
		 this.node.buffer = makebuf(buf);
		 this.node.connect(this.ctx.destination);
		 this.timestarted = this.ctx.currentTime;
		 this.finished = cb;
		 this.node.onended = this.stop;
		 this.node.start(0);
		 this.playing = true;
		 return true;
     };
     
     /* stop playback */
     playback.stop = function() {
		 /* can't rely on 'this' being set correctly */
		 try {
		     playback.node.stop(0);
		 } catch (e) {}
		 playback.playing = false;
		 if (playback.finished)
		     playback.finished();
		 playback.node = null;
     };
     
     /* turn an array of sample data into an AudioBuffer for playback */
     function makebuf(rec) {
		 var recbuf = ctx.createBuffer(1, rec.length, ctx.sampleRate);
		 var buf = recbuf.getChannelData(0);
		 for (var i=0; i<rec.length; i++)
		     buf[i] = rec[i];
		 
		 return recbuf;
     }
     
     return playback;
})(ctx);

function mark() {
    // Read the keyword
    console.log(fireKeyword);
    console.log(rescueKeyword);
    console.log(firstAidKeyword);
    // Remove previous marked elements and mark
    // the new keyword inside the context
    $("#recogRs")
	.mark(fireKeyword, {className: 'fire'})
	.mark(rescueKeyword, {className: 'rescue'})
	.mark(firstAidKeyword, {className: 'emergency'});   
}
  

/*
window.onbeforeunload = function(e) {
	if (popupWindow && !popupWindow.closed) {
		popupWindow.close();
	}
	if(recording) {
		return confirm("아직 음성인식이 수행중입니다. 정말로 나가시겠습니까?");
		if(recordConfirm) {
			stop();
		} else {
			return false;
		}
	}
	if(recog) {
		return alert("음성인식 결과 처리중입니다. 잠시만 기다려주세요.");
		return false;
	}
}
*/

$(window).on('unload', function() {
	if (popupWindow && !popupWindow.closed) {
		popupWindow.close();
	}
});
