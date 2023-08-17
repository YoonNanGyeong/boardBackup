import { ajax } from '/js/ajax.js';

const $prevNextBtn = document.querySelectorAll('.prevNext-btn');	//이전 다음 버튼
const $condition = document.getElementById('prevNextCondition');	//이전 다음 버튼 value 값이 들어갈 input 태그
const $boardCd = document.getElementById('boardCd');	//게시물 코드
const $boardSq = document.getElementById('boardSq');	//게시물 번호


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
			.post(url, payLoad)
			.then(res => res.json())
			.then(res => {
				if(res.header.rtcd =='00'){
					console.log(res.rtmsg);

				  if(res.data != null){
					  location.href = "/"+ res.data +"/detailBoard.do";
				  }else if(res.data == null){
					$condition.value = null;
				  }   
				}else if(res.header.rtcd =='99'){
					console.log(res.rtmsg);
					if($condition.value == 'next'){
						alert("다음글이 없습니다. :(");
					}else{
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
	const url = `/zipFileDownload.do?boardSq=${$boardSq.value}`;
	if(!confirm('첨부된 파일들이 압축파일로 저장됩니다.\n다운로드 하시겠습니까?')){
		return;
	}else{
		location.href = url;
	}
});