import { ajax } from '/js/ajax.js';

const $prevNextBtn = document.querySelectorAll('.prevNext-btn');	//이전 다음 버튼
const $condition = document.getElementById('prevNextCondition');	//이전 다음 버튼 value 값 
const $boardCd = document.getElementById('boardCd');	//게시물 코드
const $boardSq = document.getElementById('boardSq');	//게시물 번호


for(const ele of $prevNextBtn){
	ele.addEventListener('click', e => {
		if(ele.value === 'prev'){
			$condition.value = ele.value;
		}else if(ele.value === 'next'){
			$condition.value = ele.value;
		}

		const url = "/api/detailBoard.do";
		const payLoad = {
			boardCd : $boardCd.value,
			boardSq : $boardSq.value,
			prevNextCondition : $condition.value
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
