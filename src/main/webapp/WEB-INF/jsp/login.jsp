<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방송시스템 모니터링 솔루션</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<jsp:include page="favicon.jsp"></jsp:include>
<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/ui.js"></script>
<link href="resources/css/style.css" rel="stylesheet" type="text/css" />
<script>
	var chk=0;
	function logingo(){
		var f = document.frm;
		var url = "loginAction.do";
		$.post(url, { 
				adminId : f.adminId.value,
				adminPass : f.adminPass.value
			}, function(newitems){	
				if(newitems==true){
					location.href="/loginOk.do";
				}else if(newitems==false){
					chk++;
					alert("아이디나 비밀번호가 틀렸습니다.("+chk+"회)");
				}
			}
		);
		return false;
	}
</script>
</head>

<body>

	<div class="login_ui">
		<div class="box">
			<h1>방송시스템 모니터링 솔루션</h1>
			<form name="frm" onsubmit="return logingo()">
			<ul>
				<li><input type="text" placeholder="아이디" name="adminId"/></li>
				<li><input type="password" placeholder="비밀번호" name="adminPass"/></li>
			</ul>			
			<!-- <p class="chk"><span class="chk_box"><input type="checkbox" id="chk" /><label for="chk">저장</label></span></p> -->
			<p class="btn"><button type="submit">로그인</button></p>
			</form>
			<p class="copy">COPYRIGHT ⓒ NectarSoft 2022</p>
		</div>
	</div>
	
</body>
</html>