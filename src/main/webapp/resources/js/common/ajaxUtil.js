var ajaxUtil = function(){
	
}

//ajaxUtil.prototype.sendAsync = function(url, jsondata, success = this.success, fail = this.fail){
ajaxUtil.prototype.sendAsync = function(url, jsondata, success, fail){
	if (success == undefined) success = this.success;
	if (fail == undefined) fail = this.fail;
	
	$.ajax({
		type:"POST",
		dataType:"json",
		url:url,
		data:jsondata,
		contentType: "application/json",
		accept:"application/json",
		success:function(data){									
			success(data);					
		},error:function(e){
			fail(e);
		}
	});	
}

//ajaxUtil.prototype.sendSync = function(url, jsondata, success = this.success, fail = this.fail){
ajaxUtil.prototype.sendSync = function(url, jsondata, success, fail){
	if (success == undefined) success = this.success;
	if (fail == undefined) fail = this.fail;
	$.ajax({
		type:"POST",
		dataType:"json",
		url:url,
		async:false,
		data:jsondata,
		contentType: "application/json",
		accept:"application/json",
		success:function(data){									
			success(data);					
		},error:function(e){
			fail(e);
		}
	});	
}

ajaxUtil.prototype.sendSyncRet = function(url, jsondata){
	
	var ret;
	$.ajax({
		type:"POST",
		dataType:"json",
		url:url,
		async:false,
		data:jsondata,
		contentType: "application/json",
		accept:"application/json",
		success:function(data){									
			ret = data;
		},error:function(e){
			ret = e;
		}
	});	
	
	return ret;
}


ajaxUtil.prototype.fail = function(data){
	console.log("fail");	
}

ajaxUtil.prototype.success = function(data){
	console.log("success");	
}
