var language="kor";
var addr=location.href;
var temp=getNameFromPath(addr);
var st = $(window).scrollTop();
var isWide ;
// 파일명 추출
function getNameFromPath(strFilepath) {
	var objRE = new RegExp(/([^\/\\]+)$/);
	var strName = objRE.exec(strFilepath);
	if (strName == null) {
	   return null;
	}
	else {
		return strName[0].split(".")[0].split("?")[0];
	}
}

function getParameter(strParamName){
	 var arrResult = null;
	 if(strParamName){
		 arrResult = location.search.match(new RegExp("[&?]" + strParamName + "=(.*?)(&|$)"));
	 }
	 return arrResult && arrResult[1] ? arrResult[1] : null;
}

function mod (a,b){
	if(a%b < 0) {
		return b+a%b;
	} else {
		return a%b;
	}
}
function addZero(num) {
	if(num<10){
		return "0"+num;
	} else {
		return num;
	}
}


function fix() {
	var addr=location.href
	var temp=getNameFromPath(addr);

	switch (temp){
		case "recog":		allFix({mainSel:0,lnbSel:0}); break;
		case "mic":			allFix({mainSel:0,lnbSel:1}); break;
		case "upload":		allFix({mainSel:0,lnbSel:2}); break;
		case "view":		allFix({mainSel:0,lnbSel:0}); break;
//------------------------------------------------------------------------------------------
		case "dashboard":	allFix({mainSel:1,lnbSel:0}); break;
//------------------------------------------------------------------------------------------
		case "resourcestat":allFix({mainSel:2,lnbSel:0,tabSel:0}); break;
		case "channelstat":	allFix({mainSel:2,lnbSel:0,tabSel:1}); break;		
		case "recogstat":	allFix({mainSel:2,lnbSel:1,tabSel:0}); break;
		case "learnstat":	allFix({mainSel:2,lnbSel:2}); break;
		case "keywordstat":	allFix({mainSel:2,lnbSel:3}); break;
//------------------------------------------------------------------------------------------
		case "user":		allFix({mainSel:3,lnbSel:0}); break;
		case "auth":		allFix({mainSel:3,lnbSel:1}); break;
		case "server":		allFix({mainSel:3,lnbSel:2}); break;
		case "lang":		allFix({mainSel:3,lnbSel:3,tabSel:0}); break;
		case "learn":		allFix({mainSel:3,lnbSel:3,tabSel:1}); break;
		case "category": 	allFix({mainSel:3,lnbSel:4}); break;
//------------------------------------------------------------------------------------------
		case "profile":		allFix({mainSel:4,lnbSel:0}); break;
//------------------------------------------------------------------------------------------
		default:allFix({mainSel:-1,lnbSel:-1,tabSel:-1}); isMain=true;
	}
	
}
function makeSubTitle (subTitleMain,subTitleSub) {
	$(".subTitle .main").text(subTitleMain);
	$(".subTitle .sub").text(subTitleSub);
}

function makeContentsTitle (mainTxt) {
	$(".contentsTitle > span").text(mainTxt);

}
function makeSubContentsTitle (mainTxt) {
	$(".subContentsTitle > span").append(mainTxt);
	$(".subContentsTitle > span").contents().unwrap();
}
function makeBreadCrumb(depth01,depth02,depth03,depth04,mainSel){
	$(".breadCrumb").addClass("breadCrumb"+mainSel);
	$(".breadCrumb li").eq(1).remove();
	$(".breadCrumb li").eq(2).remove();
	$(".breadCrumb li").eq(3).remove();
	$(".breadCrumb li").eq(4).remove();
	$(".breadCrumb").append(depth01);
	$(".breadCrumb").append(depth02);
	$(".breadCrumb").append(depth03);
	$(".breadCrumb").append(depth04);
	//$(".breadCrumb li a").addClass("fa fa-angle-right")
	$(".breadCrumb li").removeClass("blind");
	$(".breadCrumb li").eq(2).find("ul").remove();
	$(".breadCrumb li").eq(3).find("ul").remove();
	$(".breadCrumb li").eq(4).find("ul").remove();
}

function subVisual(mainSel) {
	$("#subVisual").removeClass();
	$("#subVisual").addClass("subVisual0"+(mainSel+1));
}
function lnbLoad(mainSel,lnbSel,tabSel) {
	$("#lnb .mainLnb").append($("#gnb .gnbList > li").clone());
	$("#lnb .mainLnb li .sub").remove();
	$("#lnb .mainLnb li .slogan").remove();
	$("#lnb .mainLnb li h2").contents().unwrap();

	$("#lnb .subLnb").append($("#gnb .gnbList > li").eq(mainSel).find(".sub > li").clone());
	$("#lnb .subLnb li .depth03").remove();
	$("#lnb .subLnb li h3").contents().unwrap();

	$("#lnb .depth03Lnb").append($("#gnb .gnbList > li").eq(mainSel).find(".sub > li").eq(lnbSel).find(".depth03 > li").clone());
	$("#lnb .depth03Lnb li h4").contents().unwrap();
	$("#lnb .depth04").remove();

	var mainTxt = $("#gnb .gnbList > li").eq(mainSel).find("h2 > a").text();
	var subTxt = $("#gnb .gnbList > li").eq(mainSel).find(".sub > li").eq(lnbSel).find("h3 > a").text();
	var depth03Txt = $("#gnb .gnbList > li").eq(mainSel).find(".sub > li").eq(lnbSel).find(".depth03 > li").eq(tabSel).find("h4 > a").text();

	$("#lnb .mainLnbWrap .mainBtn").text(mainTxt);
	$("#lnb .subLnbWrap .mainBtn").text(subTxt);
	$("#lnb .depth03LnbWrap .mainBtn").text(depth03Txt);


	$("#lnb .mainBtn").on("click",function(e){
		$(this).next().slideToggle(100,"easeOutQuint");
		return false;
	})
}
function snbLoad(mainSel,lnbSel,tabSel,depth04Sel) {
	
	$("#snb").append($("#header #gnb .gnbList > li").eq(mainSel).find(".sub > li").eq(lnbSel).find(".depth03 > li").eq(tabSel).find(".depth04 > li").clone().removeClass("blind"));
	//$("#snb li").contents().unwrap();
	if($("#snb > li").length <= 1){
		$("#snb").css({display:"none"});
	}
	if($("#snb > li").length == 2){
		$("#snb > li").css({width:"50%"});
	}
	if($("#snb > li").length == 3){
		$("#snb > li").css({width:"33.33%"});
	}
	if($("#snb > li").length == 4){
		$("#snb > li").css({width:"25%"});
	}
	if($("#snb > li").length == 5){
		$("#snb > li").css({width:"20%"});
	}

	
}
function lnbFix(lnbSel) {
	$("#lnb > ul > li").on("mouseenter",function(e){
		$("#lnb > ul > li").removeClass("on");
		$(this).addClass("on");
	})
	$("#lnb > ul > li").on("mouseleave",function(e){
		$("#lnb > ul > li").removeClass("on");
		$("#lnb > ul > li").eq(lnbSel).addClass("on")
	})
	$("#lnb > ul > li").trigger("mouseleave");
}
function snbFix(depth04Sel) {
	$("#snb > li").on("mouseenter",function(e){
		$("#snb > li").removeClass("on");
		$(this).addClass("on");
	})
	$("#snb > li").on("mouseleave",function(e){
		$("#snb > li").removeClass("on");
		$("#snb > li").eq(depth04Sel).addClass("on")
	})
	$("#snb > li").trigger("mouseleave");
}


function allFix(obj){
	var addr=location.href
	var temp=getNameFromPath(addr);
	var defaults={};
	var option=$.extend(defaults,obj);
	var mainSel = option.mainSel;
	var lnbSel = option.lnbSel;
	var tabSel = option.tabSel;
	var depth04Sel = option.depth04Sel;
	var depth01 = $(".gnbList > li").eq(mainSel).clone();
	
	var depth02 = depth01.find(".sub > li").eq(lnbSel).clone();
	var depth03 = depth02.find(".depth03 > li").eq(tabSel).clone();
	var depth04 = depth03.find(".depth04 > li").eq(depth04Sel).clone();
	
	depth01.find("img").remove();
	depth01.find("ul").remove();
	depth02.find("ul").remove();
	depth03.find("ul").remove();
	depth04.find("ul").remove();


	

	var bread01 = depth01;
	var bread02 = depth02;
	var bread03 = depth03;
	var bread04 = depth04;
	if(temp == 'user' || temp == 'user#') {
		temp = "user_manage";
	}
	if(temp == 'auth' || temp == 'auth#') {
		temp = "user_permissions";
	}
	if(temp == 'server' || temp == 'server#') {
		temp = "server_manage";
	}
	if(temp == 'category' || temp == 'category#') {
		temp = 'category_manage';
	}
	if(temp == 'learnstat' || temp == 'learnstat#') {
		teml = 'learn_day';
	}
	$("#container").addClass(temp);
	if (mainSel == 2){
		$("#container").addClass("statistics");
	}
	var subTitleMain = $("#gnb > .gnbList > li").eq(mainSel).children("a").text();
	var subTitleSub = $("#gnb > .gnbList > li").eq(mainSel).find("h2 > a").attr("data-sub-copy");
	var contentsTitle;
	contentsTitle=$("#gnb > .gnbList > li").eq(mainSel).find(".sub > li").eq(lnbSel).find(".depth03 > li").eq(tabSel).find("h4 > a").text();
	lnbLoad(mainSel,lnbSel,tabSel);
	snbLoad(mainSel,lnbSel,tabSel,depth04Sel);
	lnbFix(lnbSel);
	snbFix(depth04Sel);
	subVisual(mainSel);
	makeSubTitle(subTitleMain,subTitleSub);
	makeContentsTitle(contentsTitle);
	makeBreadCrumb(bread01,bread02,bread03,bread04,mainSel);

	$(".gnbList > li").eq(mainSel).addClass("on");
}




function popup(){
	$(".btnPopup").on("click",function(){
		$("#popup").css({display:"block"});
		$("body").append("<div class='cover'></div>");
		return false;
	});
	
	$("#popup .btnClose").on("click",function(){
		$("#popup").css({display:"none"});
		$(".cover").remove();
		return false;
	});
	
	$(".btn_popup2").on("click",function(){
		//$("#popup").css({display:"none"});
		$("#popup2").css({display:"block"});
		var mId = $('#hdnModelId').val();
		getNotIndata(mId);
		return false;
	});
	
	$("#popup2 .btnClose").on("click",function(){
		$("#popup2").css({display:"none"});
		//$(".cover").remove();
		return false;
	});
	
	$(".btn_popup3").on("click",function(){
		$("body").append("<div class='cover'></div>");
		$("#popup3").css({display:"block"});
		return false;
	});
	
	$("#popup3 .btnClose").on("click",function(){
		$("#popup3").css({display:"none"});
		$(".cover").remove();
		return false;
	});
}

function open_winpop(pName, w, h) {
	//'all_upload.html','PopupWin','width=1000,height=800'
	var url = String.format('/popup?viewName={0}', pName);
	var options = String.format('width={0},height={1}', w, h);
	
	$('.btnClose').trigger('click');
	window.open(url,'PopupWin',options);
}

function inputBox(){
	var input = $("input[type='text']");
	input.on("mouseenter",function(){
		$(this).parent(".inputBox").addClass("on");		
	});
	input.on("mouseleave",function(){
		$(this).parent(".inputBox").removeClass("on");		
	});
}
function selectBox() {	
	$(".selectBox > a").on("click",function(){
		if ($(".selectBox").hasClass("on")){
			$(".selectBox").removeClass("on");
			$(".selectBox ul").slideUp(100);
		} else {
			$(this).parent(".selectBox").addClass("on");
			$(this).siblings("ul").slideDown(100);
		}	
		
		return false;
	})

	
	$(document).click(function(e){ //문서 body를 클릭했을때
 		if(e.target.className =="selectBox > a"){return false} //내가 클릭한 요소(target)를 기준으로 상위요소에 .share-pop이 없으면 (갯수가 0이라면)
		$(".selectBox").removeClass("on");
 		$(".selectBox ul").stop().slideUp(100);
	});

/*
	$(".selectBox ul li").on("click",function(e){
		var txt = $(this).find("a").text();
		$(this).parent().parent().parent("ul").siblings("a").find(".txt").text(txt);
		$(this).parent().parent().parent("ul").css({display:"none"})
		$(this).parent().parent().parent().parent(".selectBox").removeClass("on");
		return false;
	});
	*/
}

function tab(){
	$(".tab_icon > ul > li").on("click",function(){
		var sel = $(this).index();
		$(".tab_icon > ul > li").removeClass("on");
		$(this).addClass("on");
		$(".tabContents > ul > li").css({display:"none"});
		$(".tabContents > ul > li").eq(sel).css({display:"block"})
		if (sel=="1"){
			$(".tab_icon .btn").addClass("on");
		} else {
			$(".tab_icon .btn").removeClass("on");
		}
		return false;
	})
	$(".tab.nonlink > ul > li").on("click",function(){
		var sel = $(this).index();
		$(".tab > ul > li").removeClass("on");
		$(this).addClass("on");
		$(".tabContents > ul > li").css({display:"none"});
		$(".tabContents > ul > li").eq(sel).css({display:"block"});
		return false;
	})
}

function upload(){
	var fileTarget1 = $('.filebox1 .upload-hidden');
	fileTarget1.on('change', function(){ // 값이 변경되면
		if(window.FileReader){ // modern browser
			var filename = $(this)[0].files[0].name;
		} else { // old IE
			var filename = $(this).val().split('/').pop().split('\\').pop(); // 파일명만 추출
		} // 추출한 파일명 삽입
		$(this).siblings('.upload-name').val(filename);
	});
	var fileTarget2 = $('.filebox2 .upload-hidden');
	fileTarget2.on('change', function(){ // 값이 변경되면
		if(window.FileReader){ // modern browser
			var filename = $(this)[0].files[0].name;
		} else { // old IE
			var filename = $(this).val().split('/').pop().split('\\').pop(); // 파일명만 추출
		} // 추출한 파일명 삽입
		$(this).siblings('.upload-name').val(filename);
	});
}

//ajax 객체 생성
function getAjaxHttp(){
	var xmlhttp;
	if(window.XMLHttpRequest){
		xmlhttp = new XMLHttpRequest();
	} else {
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}

function requestAjaxData(ajax, method, url, callback) {
	ajax.onreadystatechange = function() {
		if(ajax.readyState==4 && ajax.status==200) eval(callback)(ajax);
	};
	ajax.open(method, url, true);
	ajax.send();
}

function callAjax(method, url, callback) {
	var ajax = getAjaxHttp();
	if(ajax==null) {
		alert("ajax 변수 세팅안됨"); 
		return;
	}
	requestAjaxData(ajax, method, url, callback);
}
function gnb(){
	$("#gnb .gnbList > li:nth-child(3)").on("mouseenter",function(){
		$(this).addClass("hover");
		$(this).find(".sub").stop().slideDown(100);
	});
	$("#gnb .gnbList > li:nth-child(3)").on("mouseleave",function(){
		$(this).removeClass("hover");
		$(this).find(".sub").stop().slideUp(100);
	});
	$("#gnb .gnbList > li:nth-child(4)").on("mouseenter",function(){
		$(this).addClass("hover");
		$(this).find(".sub").stop().slideDown(100);
	});
	$("#gnb .gnbList > li:nth-child(4)").on("mouseleave",function(){
		$(this).removeClass("hover");
		$(this).find(".sub").stop().slideUp(100);
	});
	$("#gnb .sub > li").on("mouseenter",function(){
		$(this).addClass("hover");
	});
	$("#gnb .sub > li").on("mouseleave",function(){
		$(this).removeClass("hover");
	});
}

function fn_logout() {
	var url = '/logoutAction';
	location.href = url;
}

function mainVisual(){
	var h = $(window).height();
	$("#mainVisual").css({height:h})
	var mainVisual = $("#mainVisual .slider").slick({
		autoplay:true,
		autoplaySpeed:6000,
		infinite:true,
		fade:true,
		arrows:true,
		dots:true,
		prevArrow:"#mainVisual .btnPrev",
		nextArrow:"#mainVisual .btnNext",
	});
	$("#mainVisual .slick-active").addClass("active-item");
	mainVisual.on('afterChange', function(event, slick, currentSlide){
		$("#mainVisual .slick-slide").removeClass("active-item");
		$("#mainVisual .slick-slide").eq(currentSlide).addClass("active-item")
		console.log(currentSlide);
	});
}

function btnUser(){
	var state = "close";
	$("#header .user .img > a").on("click",function(){
		
		if (state=="close"){
			$(this).next(".menuBox").animate({height:90},500,"easeOutQuint")
			state = "open";
		} else {
			$(this).next(".menuBox").animate({height:0},500,"easeOutQuint")
			state = "close";
		}
		
			return false;
	});
	$(document).click(function(e){ //문서 body를 클릭했을때
		if(e.target.className =="#header .user .img > a"){return false} //내가 클릭한 요소(target)를 기준으로 상위요소에 .share-pop이 없으면 (갯수가 0이라면)
		$("#header .user .img .menuBox").animate({height:0},500,"easeOutQuint");
	});
}

function innit(){
	$(document).ready(function(){
		fix();
		popup();
		inputBox();
		selectBox();
		tab();
		upload();
		gnb();
		mainVisual();
		btnUser();
	});
}
innit();