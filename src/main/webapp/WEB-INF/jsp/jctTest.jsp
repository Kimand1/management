<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<body>
<script>
function sendthis(){
	
	var formData = new FormData($("form[name=frm]")[0]);
	  
	$.ajax({ 
		url : "./apiTest.do",
		enctype: 'multipart/form-data',
		contentType : false,
		type : "post",
		dataType : "html",
	  	processData: false,
		async : false, 
		data : formData,
		beforeSend : function(xmlHttpRequest){
	    		xmlHttpRequest.setRequestHeader("AJAX", "true");
		}, 
		success : function(r) {
			var rtn = JSON.parse(r);
			//alert(rtn.result);
			if (rtn.result*1 == 1) {
				alert("발송되었습니다.");
			} else if (rtn.result*1 == 0) {
				alert(rtn.msg);
				return;
			} else {
				alert(rtn.msg);
				return;
			}
		}, 
		complete : function() {
		}
	}); 
}
</script>

	<form name="frm">
	
	JCU 아이피 : <input type="text" name="myIp" value="192.168.244.81"/> <br/>
	
	<table>
		<tr>
			<th>STX</th>
			<th>CMD</th>
			<th>ETX</th>
			<th>0x0D</th>
			<th>0x0A</th>
			<th rowspan="2"><button type="button" onclick="javascript:sendthis();">전송하기</button></th>
		</tr>	
		<tr>
			<td>
				<select name="a">
					<option value="0x01">0x01</option>
				</select>
			</td>
			<td>
				<select name="b">
					<option value="0x10">0x10</option>
					<option value="0x20">0x20</option>
					<option value="0x30">0x30</option>
					<option value="0x40">0x40</option>
					<option value="0xA0">0xA0</option>
					<option value="0xB0">0xB0</option>
					<option value="0x50">0x50</option>
					<option value="0xC0">0xC0</option>
				</select>
			</td>
			<td>
				<select name="c">
					<option value="0xEE">0xEE</option>
					<option value="0x02">0x02</option>
				</select>
			</td>
			<td>
				<select name="d">
					<option value="0x0D">0x0D</option>
				</select>
			</td>
			<td>
				<select name="e">
					<option value="0x0A">0x0A</option>
					<option value="0x10">0x10</option>
					<option value="0x20">0x20</option>
					<option value="0x30">0x30</option>
					<option value="0x40">0x40</option>
					<!-- <option value="0xA0">0xA0</option> -->
					<option value="0xB0">0xB0</option>
					<option value="0x50">0x50</option>
					<option value="0xC0">0xC0</option>
				</select>
			</td>
		</tr>
		
	</table>
	
	</form>
	
</body>
</html>