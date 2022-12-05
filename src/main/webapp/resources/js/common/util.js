/************************************************************************
/************************************************************************
 Global 
*************************************************************************/
 var __DebugOn = false;
 
 var weekday=new Array(7);
 weekday[0]="Sun";
 weekday[1]="Mon";
 weekday[2]="Tue";
 weekday[3]="Wed";
 weekday[4]="Thu";
 weekday[5]="Fri";
 weekday[6]="Sat";
 
 /************************************************************************

 *************************************************************************/
 	function timeSt(dt, opt) {
	 	var d = new Date(dt);
	 	var yyyy = d.getFullYear();
	 	var MM = d.getMonth()+1;
	 	var dd = d.getDate();
	 	var hh = d.getHours();
	 	var mm = d.getMinutes();
	 	var ss = d.getSeconds();
	 	var returnVal = '';
	 	if(opt == "1") {
	 		returnVal = (yyyy + '' + addzero(MM) + '' + addzero(dd) + '' + addzero(hh) + '' + addzero(mm) + '' + addzero(ss));
	 	}else if(opt == "2") {
	 		returnVal = (yyyy + '/' + addzero(MM) + '/' + addzero(dd) + ' ' + addzero(hh) + ':' + addzero(mm) + ':' + addzero(ss));
	 	}else if(opt == "3") {
	 		returnVal = (yyyy + '.' + addzero(MM) + '.' + addzero(dd));
	 	}else if(opt == "4") {
	 		returnVal = (yyyy + '' +addzero(MM) + '' + addzero(dd));
	 	}else if(opt == "5") {
	 		returnVal = yyyy+addzero(MM);
	 	}else if(opt == "6"){
	 		returnVal = (yyyy + '-' +addzero(MM) + '-' + addzero(dd));
	 	}
	 	return returnVal;
	};
	
	function date_add(sDate, nDays) {
	    var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
	 
	    var d = new Date(yy, mm - 1, dd + nDays);
	 
	    yy = d.getFullYear();
	    mm = d.getMonth() + 1; mm = (mm < 10) ? '0' + mm : mm;
	    dd = d.getDate(); dd = (dd < 10) ? '0' + dd : dd;
	 
	    return '' + yy + mm + dd;
	}
	
	function date_reduce(sDate, nDays) {
		var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
	    var ddd = new Date();
	    //ddd.setFullYear(yy, mm-1, dd -nDays);
	    //ddd.setMonth(mm-1, dd - nDays);
	    ddd.setDate(dd-nDays);
	    
	 
	    yy = ddd.getFullYear();
	    mm = ddd.getMonth() + 1; mm = (mm < 10) ? '0' + mm : mm;
	    dd = ddd.getDate(); dd = (dd < 10) ? '0' + dd : dd;
	 
	    return '' + yy + mm + dd;
	}
	
	function getDayString(sDate, nDays) {
		var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
	 
	    var d = new Date(yy, mm - 1, dd);
	    return weekday[d.getDay()];
	}
	
	function prevMonth(month) {
	   var d = new Date();
	   var monthOfYear = d.getMonth();
	   d.setMonth(monthOfYear - month);
	   return timeSt(d, "4");
	}
	
/*	function calDayTerm(dt, term, otp) {
		var d = new Date(dt);
		var yyyy = d.getFullYear();
	 	var MM = d.getMonth()+1;
	 	var dd = d.getDate();
	 	var hh = d.getHours();
	 	var returnVal = '';
	 	if(opt == 'day') {
	 		
	 	}else if(opt == 'week') {
	 		
	 	}else if(opt == 'month') {
	 		
	 	}
	 	return returnVal;
	}*/

	function calcTimestamp(val) {
		var vals = val.split('.');
		var hh = parseInt(vals[0]/3600);
		var min = parseInt(vals[0]/60);
		var sec = vals[0]%60;
		
		var returnVal = '';
		returnVal = addzero(hh)+':'+addzero(min)+':'+addzero(sec);
		return returnVal;
	}

	function stringSplit(strData, strIndex){ 
		var stringList = new Array(); 
		while(strData.indexOf(strIndex) != -1){
			stringList[stringList.length] = strData.substring(0, strData.indexOf(strIndex)); 
			strData = strData.substring(strData.indexOf(strIndex)+(strIndex.length), strData.length); 
		} 
		stringList[stringList.length] = strData; 
		return stringList; 
	}
	
	function getThisWeek(sDate) {
		var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
	    var d = new Date(yy, mm - 1, dd);
		var startOfWeek = new Date(d.getFullYear(), d.getMonth(), d.getDate() - d.getDay()); 
		var startYear = startOfWeek.getFullYear();
		var startMonth = startOfWeek.getMonth()+1;
		var startDate = startOfWeek.getDate();
		
		var endOfWeek = new Date(d.getFullYear(),  d.getMonth(), d.getDate() + (6- d.getDay()));
		var endYear = endOfWeek.getFullYear();
		var endMonth = endOfWeek.getMonth()+1;
		var endDate = endOfWeek.getDate();
		
		return ''+startYear+addzero(startMonth)+addzero(startDate)+'_'+endYear+addzero(endMonth)+addzero(endDate);
	}
	
	function getWeekOfMonth(sDate) {
		var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
	    var d = new Date();
	    d.setFullYear(yy);
	    d.setMonth(mm-1);
	    d.setDate(1);
	    
//	    d = d.setDate(1);
	    var firstDay = d.getDay();
	    var totalDays = new Date(d.getFullYear(), d.getMonth() + 1, 0).getDate();
	    var totalWeek = Math.ceil((firstDay+totalDays) / 7);
	    var thisWeekOfMonth = Math.ceil((totalDays-dd) / 7);
	    return addzero(totalWeek-thisWeekOfMonth);
	};
	
	function weekAndDay(sDate) {
		var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
	    
	    var date = new Date();
	    date.setFullYear(yy);
	    date.setMonth(mm-1);
	    date.setDate(dd);
	    
	    var prefixes = ['01', '02', '03', '04', '05'];

	    var weekNum = 0 | date.getDate() / 7;
	    weekNum = ( date.getDate() % 7 === 0 ) ? weekNum - 1 : weekNum;

	    return prefixes[ weekNum ];
	}
	
	function fn_addMonth(sDate, pAddMonthCnt) {
		var yy = parseInt(sDate.substr(0, 4), 10);
	    var mm = parseInt(sDate.substr(4, 2), 10);
	    var dd = parseInt(sDate.substr(6), 10);
		var d = new Date(yy, mm-1, 1);
		d.setMonth(d.getMonth() + pAddMonthCnt);
		//var iLastDate = (new Date(d.getFullYear(), d.getMonth()+2, 0).getDate());
		//d.setDate((parseInt(dd)>iLastDate)?iLastDate:parseInt(dd));
		return ''+d.getFullYear() +addzero(d.getMonth()+1)+ addzero(d.getDate());
	}
	
	function msToTime(duration, opt) {
	    var milliseconds = parseInt((duration%1000)/100)
	        , seconds = parseInt((duration/1000)%60)
	        , minutes = parseInt((duration/(1000*60))%60)
	        , hours = parseInt((duration/(1000*60*60))%24);

	    hours = (hours < 10) ? "0" + hours : hours;
	    minutes = (minutes < 10) ? "0" + minutes : minutes;
	    seconds = (seconds < 10) ? "0" + seconds : seconds;
	    
	    var returnStr = '';
	    if(opt == '1') {
	    	returnStr = hours + ":" + minutes + ":" + seconds;
	    }else if(opt == '2'){
	    	returnStr = hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	    }else if(opt == '3') {
		    if(hours == '00') {
		    	if(minutes == '00') {
		    		returnStr = seconds + "sec";
		    	}else{
		    		returnStr = minutes + "min" + seconds + "sec";
		    	}
		    }else{
		    	returnStr = hours + "hour " + minutes + "min " + seconds + "sec";
		    }
	    }else if(opt == '4'){
	    	returnStr = minutes + ":" + seconds;
	    }
	    return returnStr;
	}
	
	/**
	 * Description : 날짜포멧 변경처리 YYYYMMDD -> YYYY?MM?DD로 반환
	 * @author : 디오텍 
	 * @date    : 2014. 8. 20.
	 * @returns : String
	 * 
	 */
	function formatDate(sDate, type){
		var returnValue = '';
		switch(type){
		case 1:
			returnValue = sDate.substr(0, 4) +'.' +sDate.substr(4, 2)+'.' +sDate.substr(6);
			break;
		case 2:
			returnValue = sDate.substr(0, 4) +'-' +sDate.substr(4, 2)+'-' +sDate.substr(6);
			break;
		}
		return returnValue;
	}
	
	/************************************************************************
	10보다 작으면 앞에 0을 붙임
	 *************************************************************************/
	function addzero(n) {
		if(10 > n) {
			return "0"+n;
		}
		return n;
	};

	function trim(str) {
		return str.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	}
	
	
	/**
	 * @param strDate1
	 * @param strDate2
	 */
	function dateDiff(strDate1, strDate2) {
		var date1 = new Date(parseInt(strDate1.substr(0, 4), 10), parseInt(strDate1.substr(4, 2), 10)-1, parseInt(strDate1.substr(6), 10));
		var date2 = new Date(parseInt(strDate2.substr(0, 4), 10), parseInt(strDate2.substr(4, 2), 10)-1, parseInt(strDate2.substr(6), 10));
		var diff = date2.getTime() - date1.getTime();
	 
	    if(diff >= 0) {
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	function timeDiff(strDate1, strDate2) {
			var date1 = new Date(parseInt(strDate1.substr(0, 4), 10), parseInt(strDate1.substr(4, 2), 10)-1, parseInt(strDate1.substr(6, 2), 10), parseInt(strDate1.substr(8, 2), 10), parseInt(strDate1.substr(10), 10));
			var date2 = new Date(parseInt(strDate2.substr(0, 4), 10), parseInt(strDate2.substr(4, 2), 10)-1, parseInt(strDate2.substr(6, 2), 10), parseInt(strDate2.substr(8, 2), 10), parseInt(strDate2.substr(10), 10));
			var diff = date2.getTime() - date1.getTime();
			 
		    if(diff >= 0) {
		    	return true;
		    }else{
		    	return false;
		    }
	}

	function replaceAll(str, searchStr, replaceStr) {
		return str.split(searchStr).join(replaceStr);
	}
	
	function sleep(milliseconds) {
		var start = new Date().getTime();
		for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds){
				break;
			}
		}
	}
	
	String.format = function() {
	    // The string containing the format items (e.g. "{0}")
	    // will and always has to be the first argument.
	    var theString = arguments[0];
	    
	    // start with the second argument (i = 1)
	    for (var i = 1; i < arguments.length; i++) {
	        // "gm" = RegEx options for Global search (more than one instance)
	        // and for Multiline search
	        var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
	        theString = theString.replace(regEx, arguments[i]);
	    }
	    
	    return theString;
	}
	
	function removeFieldItemAll(arr, field, value) {
		var i = 0;
	    while (i < arr.length) {
	        if(arr[i][field] === value) {
	            arr.splice(i, 1);
	        } else {
	            ++i;
	        }
	    }
	    return arr;
	}
	
	function removeItemOnce(arr, value) {
		var index = arr.indexOf(value);
	    if (index > -1) {
	        arr.splice(index, 1);
	    }
	    return arr;
	}
	
	function removeItemAll(arr, value) {
		var i = 0;
	    while (i < arr.length) {
	        if(arr[i] === value) {
	            arr.splice(i, 1);
	        } else {
	            ++i;
	        }
	    }
	    return arr;
	}