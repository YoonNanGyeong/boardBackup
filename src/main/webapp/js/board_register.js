 import { ajax } from '/js/ajax.js';
 
 // form
 let frm = document.detailForm;
 
 // 등록 버튼
 const $addBtn = document.getElementById("writeBtn");
	 
// 입력 필드 
 const $user = document.getElementById("userNm");
 const $title = document.getElementById("title");
 const $content = document.getElementById("content");


 // 에러 문구
 const $errUser = document.querySelector(".hidden.error-user");
 const $errTitle = document.querySelector(".hidden.error-title");
 const $errContent = document.querySelector(".hidden.error-content");
 

// 필드 검증 결과값
 let resultOfUser = true;
 let resultOfTitle = true;
 let resultOfContent = true;
 
// 닉네임 검증
const fn_validationOfUser = e => {
	// 입력 필드 값
	let $userValue = e.target.value;
	// 필드 입력값 길이
 	let lenOfUser = $userValue.length;

							
	if(e.key != 'Enter'){
		return;
	}
	
	if(e.key == 'Enter'){	
	 	if(lenOfUser == 0){
	 		$errUser.classList.remove('hidden');
	 		$errUser.textContent = '*닉네임을 입력해주세요';
	 		$userValue = ' ';
	 		
	 		resultOfUser = false;
	 	}else if(lenOfUser > 6){
	 		$errUser.classList.remove('hidden');
	 		$errUser.textContent = '*닉네임은 6글자 이하로 입력 가능합니다.';
	 		$user.focus();
	 		
	 		resultOfUser = false;
	 	}else{
	 		$errUser.classList.add('hidden');
	 		
	 		resultOfUser = true;
	 	}
	 	return;
	}
	
	/*e.target.onblur = function(){
		if(lenOfUser > 0 && $errUser.classList.includes('hidden')){
			
		}
	}*/
		
	
	
};

// 제목 검증
const fn_validationOfTitle = e => {
	// 필드 입력값
	let $titleValue = document.getElementById("title").value;
	// 필드 입력값 길이
	let lenOfTitle = $titleValue.length;
	
							
	if(e.key != 'Enter'){
		return;
	}
	
	if(e.key == 'Enter'){	
	 	if(lenOfTitle == 0){
	 		$errTitle.classList.remove('hidden');
	 		$errTitle.textContent = '*제목을 입력해주세요';
	 		$title.focus();
	 		$titleValue = ' ';
	 		
	 		resultOfTitle = false;
	 	}else if(lenOfTitle > 45){
	 		$errTitle.classList.remove('hidden');
	 		$errTitle.textContent = '*제목은 45글자 이하로 입력 가능합니다.';
	 		$title.focus();
	 		
	 		resultOfTitle = false;
	 	}else{
	 		$errTitle.classList.add('hidden');
	 		
	 		resultOfTitle = true;
	 	}
	 	return;
	}
		

};

// 내용 검증
const fn_validationOfContent = e => {
	//필드입력값 
	let $contentValue = document.getElementById("content").value;
	//필드입력값 길이
	let lenOfContent = $contentValue.length;

						
	if(e.key != 'Enter'){
		return;
	}
	
	
	if(e.key == 'Enter'){	
	 	if(lenOfContent == 0){
	 		$errContent.classList.remove('hidden');
	 		$errContent.textContent = '*내용을 입력해주세요';
	 		$content.focus();
	 		$contentValue = ' ';
	 		
	 		resultOfContent = false;
	 	}else{
	 		$errContent.classList.add('hidden');
	 		$content.focus();
	 		
	 		resultOfContent = true;
	 	}
	 	return;
	}

	
}

// 키보드 입력 이벤트
	// 닉네임 
	$user.addEventListener('keydown', fn_validationOfUser, false);
	// 제목 
	$title.addEventListener('keydown', fn_validationOfTitle, false);
	// 내용
	$content.addEventListener('keydown', fn_validationOfContent, false);
	
 /* 글 등록 function */
 $addBtn.addEventListener('click', e => {
 	e.preventDefault();
 	
    const btnVal = document.getElementById("writeBtn").innerText;
    let con = confirm(btnVal + "하시겠습니까?");
    
    const flag = document.getElementById("boardSq").value;
    
	
	if(con == false){
        return;
    }else if(con == true){ 
    		
		if(resultOfUser == true && resultOfTitle == true && resultOfContent == true){
	    	if(flag == 0){
	    		frm.action = '/addBoard.do';
	    		
	   			frm.submit(); 
	    	}else if(flag > 0){
	    		frm.action = '/updateBoard.do';
	    		
	    		frm.submit(); 
	    	}
		}else {
			alert('입력되지 않은 항목이 있습니다. :(');
		}
		
    }
 });
 

 	
 // 파일 삭제 처리
const $delBtn =  document.querySelector('.fa-solid.fa-trash-can');
const fileSq = document.getElementById("fileSq");
$delBtn?.addEventListener('click', e => {
    if(e.target.tagName != 'I') return;
    if(!confirm('삭제하시겠습니까?')) return;
    
    const url = `/deleteFile.do/${fileSq.value}`;
    
    ajax
        .delete(url)
        .then(res => res.json())
        .then(res => {
            if(res.header.rtcd == '00'){
                //첨부파일 정보 화면에서 제거
                removeAttachFileFromView(e);
            }else{
                console.log(res.rtmsg);
            }
        })
        .catch(console.error);
	});
function removeAttachFileFromView(e){
    const $parent = document.querySelector('.download-area');
    const $child = e.target.closest('.files');
    $parent.removeChild($child);
}	



	