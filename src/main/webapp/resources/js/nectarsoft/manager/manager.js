var currentPage_g;
var dataPerPage=10;
var totalDataCount_g;
var extNo="";

$(document).ready(function() {
	
	$(".recogPage").css('color','rgba(0,0,0,.8)');
	
	$("#button1").off("click");
	$("#button1").on("click", function(e){
		/*var startDate = $("#date1").val();
		var endDate = $("#date2").val();*/
		var extId = $("#text").val();
		extNo = extId;
		
		$(".msg_card_body1").empty();
		$(".msg_card_body2").empty();

		getRecentResult(1);
	});
	
	
	
	var today = new Date();
	var year = today.getFullYear();
	var month = ('0' + (today.getMonth() + 1)).slice(-2);
	var day = ('0' + today.getDate()).slice(-2);

	var dateString = year + '-' + month  + '-' + day;
	
	$("#date1").val(year + '-' + month  + '-' + day);
	$("#date2").val(year + '-' + month  + '-' + day);
	
	
	getRecentResult(1);
	
	
	$('#listTable tbody').off( 'click', 'tr');
	$('#listTable tbody').on( 'click', 'tr', function () {
		$('.tableSelect').removeClass('tableSelect');
		this.classList.add("tableSelect");
	});
});









function getRecentResult(pageNo){
	currentPage_g = pageNo;
	
	var url = "/process/getRecentResultManager";
	var dat = {};
	dat.startDate = $("#date1").val();
	dat.endDate = $("#date2").val();
	//dat.extId = extId;
	dat.extId = "%"+extNo+"%";
	dat.startTime = $("#select").val().slice(0,2);
	dat.endTime = $("#select").val().slice(2,4);
	
	dat.pageCount = dataPerPage;
	dat.pageNo = pageNo;
	
	var json = JSON.stringify(dat);
	
	commonFunction.spinnerShow();
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var data = JSON.parse(xhttp.responseText);
			
			if(data.count == 0) {
				
				var tbodyHtml = "<tr><td colspan=3>목록이 존재하지 않습니다</td></tr>";
				
				$("#listTbody").empty();
				$("#listTbody").html(tbodyHtml);
				
				
				
				var html = "";
				html += "<li class='page-item disabled'><a class='page-link' href='#'>Prev</a></li>";
				html += "<li class='page-item disabled'><a class='page-link' href='#'>Next</a></li>";
				var pages = $("#pagination");
				pages.empty();
				pages.append(html);
				
				commonFunction.spinnerClose();
				$("#listCountDiv").text("");
				
				return (false);
			}
			var tbodyHtml = "";
			var totalCnt = data.count; // 전체 로우 수
			totalDataCount_g = totalCnt;
			
			var totalPage = Math.ceil(data.count / dataPerPage); // 전체 페이지 수
			var searchCnt = data.list.length; // 검색된 로우 수

			for (var i = 0; i < searchCnt; i++) {
				//tbodyHtml += "<tr onclick='loadDetailPage(\"" + data.list[i].voiceId + "\")'>";
				tbodyHtml += "<tr onclick='getDataTableRowData(\""+data.list[i].extVoiceId+"\",\""+data.list[i].extId+"\")'>";
				tbodyHtml += "<td>" + data.list[i].createDt + "</td>";
				tbodyHtml += "<td>" + data.list[i].extId + "</td>";
				tbodyHtml += "<td>" + data.list[i].callerNo + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#listTbody").empty();
			$("#listTbody").html(tbodyHtml);

			commonFunction.spinnerClose();
			
			paging();
			
			/*
			$('#dataTable').DataTable().destroy();
			$("#dataTable").DataTable({
				columns : [
					{ data : "createDt", orderable: false},
					{ data : "extId", orderable: false},
					{ data : "callerNo", orderable: false},
					{ data : "extVoiceId", orderable: false}
				],
				order: [[ 0, "asc" ]],
				data : data,
				paging : true,
			    lengthChange : false,
			    pageLength: 20,
			    info: false,
			    filter : false,
			    columnDefs: [
			    	{ width: '40%', targets: 0},
			    	{ width: '30%', targets: 1},
		            { width: '30%', targets: 2},
		            { width: '0%', targets: 3, visible : false}
		        ]
			});
			
			
			var table = $('#dataTable').DataTable();
			$('#dataTable tbody').off( 'click', 'tr');
			$('#dataTable tbody').on( 'click', 'tr', function () {
				var data = table.row( this ).data();
				getDataTableRowData(data);
			});
			*/
			
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
	
	
	
}


function paging(){
	var pageCount = 5;
  
	var totalPage = Math.ceil(totalDataCount_g/dataPerPage);
	var pageGroup = Math.ceil(currentPage_g/pageCount); 
  
	var last = pageGroup * pageCount;
	if(last > totalPage)
		last = totalPage;
	
	var first = 1+(5*(pageGroup-1));
	
	var next = last + 1;
	var prev = first - 1;
  
	if(totalPage < 1)
		first = last;
	
	
	var html = "";
	
	if(first > pageCount){
		html += "<li class='page-item'><a class='page-link' href='#' onclick='getRecentResult("+prev+")'>Prev</a></li>";
	}else{
		html += "<li class='page-item disabled'><a class='page-link' href='#'>Prev</a></li>";
	}
	for(var j=first; j<=last; j++){
		if(currentPage_g == j){
			html += "<li class='page-item active' aria-current='page'><a class='page-link' href='#' onclick='getRecentResult("+j+")'>"+j+"</a></li>";
		}else if(j>0){
			html += "<li class='page-item'><a class='page-link' onclick='getRecentResult("+j+")' href='#'>"+j+"</a></li>";
		}
	}
	//if(next>pageCount && next<totalPage){
	if(next>pageCount && next<=totalPage){
		html += "<li class='page-item'><a class='page-link' onclick='getRecentResult("+next+")' href='#'>Next</a></li>";
	}else{
		html += "<li class='page-item disabled'><a class='page-link' onclick='getRecentResult("+next+")' href='#'>Next</a></li>";
	}
	
	
	var pages = $("#pagination");
	pages.empty();
	pages.append(html);
	
	
	$("#listCountDiv").text("Total : "+totalDataCount_g+"  Page : "+currentPage_g+"/"+totalPage);
}




function getDataTableRowData(voiceId, extId){
	var dat = {};
	dat.extVoiceId = voiceId;
	dat.extId = extId;
	
	var url = "/process/getDataTableRowData";
	var json = JSON.stringify(dat);
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			var data;
			
			if(response==""){
				$(".msg_card_body2").html("내용이 존재하지 않습니다");
				return;
			}
			
			try{
				data = JSON.parse(xhttp.responseText);
			}catch(error){
				$(".msg_card_body1").empty();
				$(".msg_card_body2").empty();
				return;
			}
			
			$(".msg_card_body1").html(data.resultHtml);
			
			
			var html="";
			for(var i=0; i<$(".msg_card_body1").children().length; i++){
				//r+=$(".msg_card_body1").children()[i].innerText+" ";
				if($(".msg_card_body1").children()[i].outerHTML.indexOf("-webkit-left")>0){ //신고자
					html += "신고자 : "+$(".msg_card_body1").children()[i].innerText+"<br>";
				}else if($(".msg_card_body1").children()[i].outerHTML.indexOf("-webkit-right")>0){
					html += "접수자 : "+$(".msg_card_body1").children()[i].innerText+"<br>";
				}
			}
			
			$(".msg_card_body2").html(html);
			
			
			
		}
	};
	
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(json);
}

