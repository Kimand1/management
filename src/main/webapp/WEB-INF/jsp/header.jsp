<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
		<style>
		h2{position: absolute;right: 30px;top: 95px;bottom: 0;height: 89px;margin: auto 0;font-size: 15px;color: #b1b1b1;padding-bottom: 10px;}
		.nbutton{color: #fff;background-color: #007bff;border-color: #007bff;box-shadow: none;border-top-left-radius: 0;border-bottom-left-radius: 0;margin: 0;font-family: inherit;box-sizing: border-box;margin-right: 20px;margin-bottom: 5px;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-moz-user-select: none;-ms-user-select: none;border: 1px solid transparent;padding: 0.375rem 0.75rem;font-size: 1rem;line-height: 1.5;border-radius: 0.25rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;}
		.nbutton2{color: #fff;background-color: #b9b9b9;border-color: #007bff;box-shadow: none;border-top-left-radius: 0;border-bottom-left-radius: 0;margin: 0;font-family: inherit;box-sizing: border-box;margin-right: 20px;margin-bottom: 5px;display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;-moz-user-select: none;-ms-user-select: none;border: 1px solid transparent;padding: 0.375rem 0.75rem;font-size: 1rem;line-height: 1.5;border-radius: 0.25rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;}
		.logoutBtn{position:absolute;right:9px;top:3px}
		</style>
		<header>
			<h1>방송시스템 모니터링 솔루션</h1><div class="logoutBtn"><button type="button" class="nbutton2" onclick="javascript:logout();">로그아웃</button></div>
			<h2><button type="button" onclick="javascript:updateNow();" class="nbutton">즉시 업데이트</button>로그 업데이트 시각 : <span id="utime"></span></h2>
			<nav>
				<ul>
					<%pageContext.setAttribute("pgidx", request.getParameter("pgidx")); %>
					<c:choose>
						<c:when test="${pgidx*1 eq 1 }">
							<li class="current"><a href="/">현황판</a></li>
							<li><a href="/logList.do">상세로그</a></li>
						</c:when>
						<c:when test="${pgidx*1 eq 2 }">
							<li><a href="/">리소스조회</a></li>
							<li class="current"><a href="/logList.do">상세로그</a></li>
						</c:when>
						<c:otherwise>
							
						</c:otherwise>
					</c:choose>					
				</ul>
			</nav>
			<div style="width:100%;text-align: center;">
				<div style="width: 270px;margin-left: calc((50% - 120px));">
					<img src="/resources/images/logo.png" style="margin-top: 20px;background-color: white;"/>
				</div>
			</div>
		</header>
		<script>
			$(document).ready(function(){
				logtime();
				setInterval(function() {
					logtime();
				}, 300000);
			});
			function logtime(){
				var url = '/api/lastLogTime';
				$.get(url, '', function(newitems){	
					document.getElementById("utime").innerHTML = newitems;
				});	
			}			
			function updateNow(){
				LoadingWithMask();
				var url = '/reUpdate.do';
				
				$.get(url, '', function(newitems){	
					location.reload();
				});	
			}
			function LoadingWithMask() {
			    var maskHeight = $(document).height();
			    var maskWidth = window.document.body.clientWidth;
			    var imgHeight = (maskHeight - 250) / 2;
			    var mask = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
			    var loadingImg = '';
			    loadingImg += "<div id='loadingImg' style='position:absolute; z-index:9000; width:100%; top:" + imgHeight + "px; '>";
			    loadingImg += " <img src='/resources/images/loading.gif' style='position: relative; display: block; margin: 0px auto;'/>";
			    loadingImg += "</div>";
			    $('body').append(mask).append(loadingImg);
			    $('#mask').css({
			        'width': maskWidth,
			        'height': maskHeight,
			        'opacity': '0.3'
			    });
			    $('#mask').show();
			    $('#loadingImg').show();
			}
			function logout(){
				var url = "logout.do";
				$.post(url, '', function(newitems){	
						alert("로그아웃 되었습니다.");
						location.reload();
					}
				);
			}
		</script>