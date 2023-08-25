import { ajax } from '/js/ajax.js';

// 파일 삭제 처리
const $delBtn =  document.querySelectorAll('.fa-solid.fa-trash-can'); // 삭제 버튼


for(const ele of $delBtn){
	ele.addEventListener('click', e => {
		const target = e.target; // 클릭 이벤트가 발생한 버튼
		const $fileSq = target.parentNode.previousElementSibling; // 이벤트 발생 버튼의 바로 이전 요소
		
		if(e.target.tagName != 'I') return; // 클릭 이벤트 발생 요소가 i태그가 아니면 
		if(!confirm('삭제하시겠습니까?')) return; // 삭제 확인 창에서 취소를 선택했을 경우

		const url = `/api/${$fileSq.value}/deleteFile.do`; // 파일 단건 삭제 
		ajax
			.get(url)
			.then(res => res.json())
			.then(res => {
				if(res.header.rtcd == '00'){
					console.log("파일 삭제 성공!")
					//첨부파일 정보 화면에서 제거
					removeAttachFileFromView(e);
				}else{
					console.log(res.rtmsg);
				}
			})
			.catch(console.error);
	}, false);

}

//첨부파일 정보 화면에서 제거
function removeAttachFileFromView(e){
    const $parent = document.querySelector('.download-area'); // 첨부파일 영역 태그의 부모태그
    const $child = e.target.closest('.files');
    $parent.removeChild($child);
}	