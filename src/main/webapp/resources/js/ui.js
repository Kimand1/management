function dropJs(colA, colB, num){
	var diffpos= 0;
	var startpos, range=90;
	var isEnable = false;
	var colA = colA;
	var colB = colB;
	function on_mouse_down(e) {
		startpos = window.innerHeight/2;
		isEnable = true;
		return false;
		
	}
	function on_mouse_up(e) {
		isEnable = false;
		return false;
	}
	function on_mouse_move(e) {
		if (isEnable) {
			pos = event.clientY;
			diffpos = startpos-pos;
			var height = window.innerHeight/2;
			if (diffpos > - (height-range) && diffpos < (height-range)) {
				document.getElementById(colA).style.height = parseInt(height - 66 - diffpos) + "px";
				document.getElementById(colB).style.height = parseInt(height + diffpos)  + "px";

			}
		}
	}

	document.getElementById("mouse").onmousedown = on_mouse_down;
	document.onmouseup = on_mouse_up;
	document.onmousemove = on_mouse_move;
}



//datepicker 달력
function datepicker(){
	$.datepicker.regional['ko'] = {
		closeText: '',
		prevText: '이전달',
		nextText: '다음달',
		currentText: '오늘',
		monthNames: ['01','02','03','04','05','06월','07','08','09','10','11','12'],
		monthNamesShort: ['01','02','03','04','05','06월','07','08','09','10','11','12'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin:['일','월','화','수','목','금','토'],
		dateFormat: 'yy-mm-dd',
		firstDay: 0,
		isRTL: false,
		changeYear: false,
		changeMonth: false,
		showMonthAfterYear: true,
		showButtonPanel: false,
		yearSuffix: '',
		monthSuffix: '',

	};
	$.datepicker.setDefaults($.datepicker.regional['ko']);
	$( '.datepic' ).datepicker({dateFormat: 'yy-mm-dd'});
}

