import { ajax } from '/js/ajax.js';

// 파일 삭제 처리
const $delBtn =  document.querySelectorAll('.fa-solid.fa-trash-can');


for(const ele of $delBtn){
	ele.addEventListener('click', e => {
		const target = e.target;
		const $fileSq = target.parentNode.previousElementSibling;
		
		if(e.target.tagName != 'I') return;
		if(!confirm('삭제하시겠습니까?')) return;

		const url = `/api/${$fileSq.value}/deleteFile.do`;
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
    const $parent = document.querySelector('.download-area');
    const $child = e.target.closest('.files');
    $parent.removeChild($child);
}	