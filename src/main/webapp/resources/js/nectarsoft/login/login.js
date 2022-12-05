var autoLoginTimer;

$(document).ready(function() {
	if(remember=='Y'){
		setTimeout(autoLogin,2000);
	}
	
	
	/*
	$("#inputTel").val(localStorage.getItem("id"));
	$("#inputPw").val(localStorage.getItem("pw"));
	
	if(localStorage.getItem("id")!=null){
		$("#checkRemember").prop("checked",true);
	}
	*/
	
	
	$("#inputTel").val(inid);
	$("#inputPw").val(inpw);
	
	if(remember=='Y'){
		$("#checkRemember").prop("checked",true);
	}
	
	
	$("#login").off("click");
	$("#login").on("click", function(e){
		var pw;
		if(encryptPwFlag==1){
			pw = $("#inputPw").val();
		}else{
			pw = SHA256($("#inputPw").val());
		}
		var dat = {};
		dat.extId=$("#inputTel").val();
		dat.password=pw;
		
		if($("#checkRemember").is(":checked")){
			dat.remember = 'Y';			
		}else{
			dat.remember = 'N';
		}
		
		
		var url = "/login";
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		    if(xhttp.readyState == 4 && xhttp.status == 200) {
		        var data = JSON.parse(xhttp.responseText);
		        if(data.CODE=="SUCCESS"){
		        	/*
		        	if($("#checkRemember").is(":checked")){
		        		localStorage.setItem("id",$("#inputTel").val());
		        		localStorage.setItem("pw",$("#inputPw").val());
		        	}else{
		        		localStorage.clear();
		        	}*/
		        	location.reload();
		        	return;
		        }else{
		        	alert(data.DETAIL);
		        }
		    }
		};
		var json = JSON.stringify(dat);
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send(json);
	});
	
	
	

	
	
	//enter키 기능 구현
    $('#inputTel').keypress(function (event) {
        if (event.which == 13 && !($("#inputTel").val() == '')) {
            $('#login').click();
            return false;
        }
    });
    $('#inputPw').keypress(function (event) {
        if (event.which == 13 && !($("#inputPw").val() == '')) {
            $('#login').click();
            return false;
        }
    });
	
});



function autoLogin(){
	if($("#checkRemember").is(":checked")){
		$("#login").click();
	}
}