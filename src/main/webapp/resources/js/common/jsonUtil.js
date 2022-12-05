var jsonUtil = function(){
	
}

jsonUtil.prototype.make = function(pid, saveval){
	saveval.pid = pid;
	var jsonData = JSON.stringify(saveval);
	return jsonData;
}

jsonUtil.prototype.defJson = function(){

	var def = {};
	
}
