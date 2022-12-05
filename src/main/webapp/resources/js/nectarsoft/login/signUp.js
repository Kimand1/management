var autoLoginTimer;

$(document).ready(function() {
		
	$("#signUp").off("click");
	$("#signUp").on("click", function(e){
		var dat = {};
		dat.extId=$("#inputExtId").val();
		
		var url = "/signUp";
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		    if(xhttp.readyState == 4 && xhttp.status == 200) {
	        	location.reload();
		    }
		};
		var json = JSON.stringify(dat);
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send(json);
	});
	
	//enter키 기능 구현
    $('#inputExtId').keypress(function (event) {
        if (event.which == 13 && !($("#inputExtId").val() == '')) {
            $('#signUp').click();
            return false;
        }
    });
	
});
