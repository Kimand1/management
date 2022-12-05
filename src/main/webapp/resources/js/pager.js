pagingView = function(zone, options) {
	var startIndex = options.startIndex;
	var pagingHTML = "";
	var list_function = options.func;//리스트 호출에 사용할 함수
	var page = parseInt(Number(options.page)); // 현재 페이지
	
	var totalCount = parseInt(Number(options.total)); //실제 데이터 총 갯수
	 
	var pageBlock = parseInt(Number(options.view)); //화면에 보여질 전체 갯수
	 
	var navigatorNum = 10; // 그룹 번호 갯수

	var firstPageNum = 1;
	 
	var lastPageNum = Math.floor((totalCount-1)/pageBlock) + 1;

	var previewPageNum  = page == 1 ? 1 : page-1;

	var nextPageNum = page == lastPageNum ? lastPageNum : page+1;

	var indexNum = startIndex <= navigatorNum  ? 0 : parseInt((startIndex-1)/navigatorNum) * navigatorNum;
	if (totalCount > 1) {
		//페이지 처리.
		if(startIndex > 1) {
			pagingHTML += '<a href="#" onclick="'+list_function+'('+firstPageNum+','+pageBlock+')"><img src="/resources/images/board/btnFirst.gif" alt="" /></a>'
			pagingHTML += '<a href="#" onclick="'+list_function+'('+previewPageNum+','+pageBlock+')" class="arrow_l"><img src="/resources/images/board/btn_prev.gif" alt="이전 게시물 목록" class=""></a>';
		} else {
			pagingHTML += '<a href="#"><img src="/resources/images/board/btnFirst.gif" alt="" /></a>'; 
			pagingHTML += '<a href="#" class="arrow_l"><img src="/resources/images/board/btn_prev.gif" alt="이전 게시물 목록" class=""></a>';
		}
		pagingHTML += '<div class="number">';
		for (var i=1; i<=navigatorNum; i++) {

			var pageNum = i + indexNum;

			if (pageNum == startIndex){ 
				pagingHTML += '<a href="#" class="on">'+pageNum+'</a>';
			} else { 
				pagingHTML += '<a href="#" onclick="'+list_function+'('+pageNum+','+pageBlock+')'+'">'+pageNum+'</a>';
			}
			
			if(pageNum==lastPageNum){
				break;
			}
		}
		pagingHTML += '</div>';

		if (startIndex < lastPageNum) {
			pagingHTML += '<a href="#" onclick="'+list_function+'('+nextPageNum+','+pageBlock+')" class="arrow_r"><img src="/resources/images/board/btn_next.gif" alt="다음 게시물 목록" class=""></a>';
			pagingHTML += '<a href="#" onclick="'+list_function+'('+lastPageNum+','+pageBlock+')"><img src="/resources/images/board/btnLast.gif" alt="" /></a>'
		} else {
			pagingHTML += '<a href="#" class="arrow_r"><img src="/resources/images/board/btn_next.gif" alt="다음 게시물 목록" class=""></a>';
			pagingHTML += '<a href="#"><img src="/resources/images/board/btnLast.gif" alt="" /></a>'
		}
	}
	$(zone).html(pagingHTML);
};
