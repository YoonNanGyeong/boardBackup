import { ajax } from "/js/ajax.js";
const $boardSq = document.getElementById("boardSq");
	// 뒤로가기 눌렀을 경우 캐시된 페이지이면 실행
	window.onpageshow = function (e) {
		if (e.persisted|| (window.performance && window.performance.navigation.type == 2)) {
			const url = "/api/"+ $boardSq.value + "/selectBoard.do";
			ajax
				.get(url)
				.then(res => res.json())
				.then(res => {
					if(res.header.rtcd == 99){
						alert('삭제된 게시글 입니다.');
						location.replace("/boardList.do");
						// history.back(); //이전 페이지로 가기
					}else{
					console.log(res.rtmsg);
					}	
				})
				.catch(console.error);
		}
    }