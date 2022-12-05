var common = function(){
}


common.prototype.spinnerShow = function(){
	//화면의 높이와 너비를 구합니다.
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    //화면에 출력할 마스크를 설정해줍니다.
    var mask       ="<div id='mask' style='position:absolute; z-index:9999; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
      
    loadingImg +=" <img src='/resources/images/contents/spinner.gif' style='position: absolute; display: block; margin: 0px auto;z-index: 100;top: 30%;left: 50%;'/>";
 
    //화면에 레이어 추가
    $('body')
        .append(mask)
 
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
  
    //마스크 표시
    $('#mask').show();
  
    //로딩중 이미지 표시
    $('#loadingImg').append(loadingImg);
    $('#loadingImg').show();
}


common.prototype.spinnerClose = function(){
	$('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').empty(); 
}

