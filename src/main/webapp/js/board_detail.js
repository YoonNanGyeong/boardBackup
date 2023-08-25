import { ajax } from '/js/ajax.js';

const $prevNextBtn = document.querySelectorAll('.prevNext-btn');	//이전 다음 버튼
const $condition = document.getElementById('prevNextCondition');	//이전 다음 버튼 value 값이 들어갈 input 태그
const $boardCd = document.getElementById('boardCd');	//게시물 코드
const $boardSq = document.getElementById('boardSq');	//게시물 번호
const $category = document.getElementById('category').textContent;	 //카테고리
console.log($category);

let cateObj = new Object;

for(const ele of $prevNextBtn){
	ele.addEventListener('click', e => {
		if(ele.value === 'prev'){ //버튼의 value 가 prev일 때(이전버튼 클릭)
			$condition.value = ele.value; //조회 조건 저장(이전, 다음)
		}else if(ele.value === 'next'){ //버튼의 value 가 next일 때(다음버튼 클릭)
			$condition.value = ele.value;
		}

		const url = "/api/detailBoard.do"; //이전, 다음글 번호로 조회하는 url 
		const payLoad = {//payload에 들어갈 데이터
			boardCd : $boardCd.value,	//카테고리 코드
			boardSq : $boardSq.value,	//글 번호
			prevNextCondition : $condition.value	//조회 조건
		};
		ajax
			.post(url, payLoad) // post method로 전송
			.then(res => res.json()) // 응답 데이터 json 타입
			.then(res => {	
				if(res.header.rtcd =='00'){// 결과 코드가 성공 일 경우 
					console.log(res.rtmsg); // 코드, 메세지

				  if(res.data != null){ // 응답 데이터가 있는 경우 
					  location.href = "/"+ res.data +"/detailBoard.do";
				  }else if(res.data == null){ // 응답 데이터가 없는 경우 
					$condition.value = null;
				  }   
				}else if(res.header.rtcd =='99'){ // 실패 
					console.log(res.rtmsg); // 코드, 메세지
					if($condition.value == 'next'){ // 조회 조건이 next면 안내 창 팝업
						alert("다음글이 없습니다. :(");
					}else{ // 조회 조건이 prev면 안내 창 팝업
						alert("이전글이 없습니다. :(");
					}
				}
			 }
			)
			.catch(console.error); 
			return;
	}, false);
}

// 다운로드 아이콘
const $downZip = document.querySelector('.fa-solid.fa-download');

// 아이콘 클릭 시 압축 파일 다운로드
$downZip.addEventListener('click', e => {
	const url = `/zipFileDownload.do?boardSq=${$boardSq.value}`; // 다운로드 url
	if(!confirm('첨부된 파일들이 압축파일로 저장됩니다.\n다운로드 하시겠습니까?')){
		return;
	}else{
		location.href = url;
	}
});