// 입력 데이터 검증
 function fn_validation(){
	// 입력 필드 
	 const $user = document.getElementById("userNm");
	 const $title = document.getElementById("title");
	 const $content = document.getElementById("content");
	
	// 입력 필드 값
	 const $userValue = document.getElementById("userNm").value;
	 const $titleValue = document.getElementById("title").value;
	 const $contentValue = document.getElementById("content").value;
	 
	 // 에러 문구
	 const $errUser = document.querySelector(".error-user");
	 const $errTitle = document.querySelector(".error-title");
	 const $errContent = document.querySelector(".error-content");
	 
	 // 등록 버튼
	 const $addBtn = document.getElementById("writeBtn");
	 
	// 필드 입력값 길이
 	const lenOfUser = $userValue.length;
 	const lenOfTitle = $titleValue.length;
 	const lenOfContent = $contentValue.length;
 	
 	// 닉네임 검증
 	if(lenOfUser == 0){
 		$errUser.style.display = 'inline';
 		$errUser.style.color = 'red';
 		$errUser.textContent = '*닉네임을 입력해 주세요';
 		$user.focus();
 		$userValue = ' ';
 	}else if(lenOfUser > 6){
 		$errUser.style.display = 'inline';
 		$errUser.style.color = 'red';
 		$errUser.textContent = '*닉네임은 6글자 이하로 입력 가능합니다.';
 		$user.focus();
 	}else{
 		$errUser.style.display = 'hidden';
 		$title.focus();
 	}
 	
 	// 제목 검증
 	if(lenOfTitle == 0){
 		$errTitle.style.display = 'inline';
 		$errTitle.style.color = 'red';
 		$errTitle.textContent = '*제목을 입력해 주세요';
 		$title.focus();
 		$titleValue = ' ';
 	}else if(lenOfTitle > 45){
 		$errTitle.style.display = 'inline';
 		$errTitle.style.color = 'red';
 		$errTitle.textContent = '*제목은 45글자 이하로 입력 가능합니다.';
 		$title.focus();
 	}else{
 		$errTitle.style.display = 'hidden';
 		$title.focus();
 	}
 	
 	
 	// 내용 검증
 	if(lenOfContent == 0){
 		$errContent.style.display = 'inline';
 		$errContent.style.color = 'red';
 		$errContent.textContent = '*내용을 입력해 주세요';
 		$content.focus();
 		$contentValue = ' ';
 	}else{
 		$errContent.style.display = 'hidden';
 		$content.focus();
 	}
 }
 
 /* 글 등록 function */
    function fn_egov_save() {
        var boardType = '<c:out value = "${registerFlag}">';
        var errors = document.querySelectorAll(".errors");
        var btnVal = document.getElementById("writeBtn").innerText;
        let con = confirm(btnVal + "하시겠습니까?");
        
    	frm = document.detailForm;
    	if(con == false){
            return;
        }else if(con == true){
        	fn_validation();
        	frm.action = '<c:url value="${registerFlag == 'create' ? '/addBoard.do' : '/updateBoard.do'}"/>';
            frm.submit();
        }
    }
 	
 // 파일 다운로드 영역 삭제
 	const fileName = document.getElementById("fileNm");
	const fileDelete = document.getElementById("fileDelete");
	const fileDown = document.getElementById("fileDown");
	
	function fn_fileDelete(){
		fileName.value = null;
		fileDown.remove();
	}
	