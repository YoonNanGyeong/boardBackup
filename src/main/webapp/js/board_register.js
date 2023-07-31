import { ajax } from "/js/ajax.js";

// form
let frm = document.detailForm;

// 등록 버튼
const $addBtn = document.getElementById("writeBtn");
const $listBtn = document.getElementById("listBtn");

// 입력 필드 
const $user = document.getElementById("userNm");
const $title = document.getElementById("title");
const $content = document.getElementById("content");


// 에러 문구
const $errUser = document.querySelector(".hidden.error-user");
const $errTitle = document.querySelector(".hidden.error-title");
const $errContent = document.querySelector(".hidden.error-content");


// 필드 검증 결과값
let resultOfUser = false;
let resultOfTitle = false;
let resultOfContent = false;

// 특수문자 체크 정규식
const regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g;

// 닉네임 검증
const fn_validationOfUser = e => {

	// 입력 필드 값
	let $userValue = e.target.value;

	if ($userValue.trim() == '') {
		$errUser.classList.remove('hidden');
		$errUser.textContent = '*닉네임을 입력해주세요';
		$user.focus();
		$userValue = ' ';

		resultOfUser = false;
	} else if (regExp.test($userValue.trim())) {
		$errUser.classList.remove('hidden');
		$errUser.textContent = '*특수문자는 입력할 수 없습니다';
		$user.focus();
		$userValue = ' ';

		resultOfUser = false;
	} else {
		$errUser.classList.add('hidden');

		resultOfUser = true;
	}
	return;

};

// 제목 검증
const fn_validationOfTitle = e => {
	// 필드 입력값
	let $titleValue = e.target.value;


	if ($titleValue.trim() == '') {
		$errTitle.classList.remove('hidden');
		$errTitle.textContent = '*제목을 입력해주세요';
		$title.focus();
		$titleValue = ' ';

		resultOfTitle = false;
	} else {
		$errTitle.classList.add('hidden');

		resultOfTitle = true;
	}
	return;



};

// 내용 검증
const fn_validationOfContent = e => {
	//필드입력값 
	let $contentValue = e.target.value;


	if ($contentValue.trim() == '') {
		$errContent.classList.remove('hidden');
		$errContent.textContent = '*내용을 입력해주세요';
		$content.focus();
		$contentValue = ' ';

		resultOfContent = false;
	} else {
		$errContent.classList.add('hidden');

		resultOfContent = true;
	}
	return;



}

// 이벤트 리스너 등록
// 닉네임 
$user.addEventListener('input', fn_validationOfUser, false);
$user.addEventListener('focusout', fn_validationOfUser, false);
// 제목 
$title.addEventListener('input', fn_validationOfTitle, false);
$title.addEventListener('focusout', fn_validationOfTitle, false);
// 내용
$content.addEventListener('input', fn_validationOfContent, false);
$content.addEventListener('focusout', fn_validationOfContent, false);

if($user.value.trim() == ''){
	resultOfUser = false;
}else{
	resultOfUser = true;
}
if($title.value.trim() == ''){
	resultOfTitle = false;
}else{
	resultOfTitle = true;
}
if($content.value.trim() == ''){
	resultOfContent = false;
}else{
	resultOfContent = true;
}


/* 글 목록 화면 function */
function fn_selectList(){
	const btnVal = document.getElementById("writeBtn").innerText;
	let con = confirm(`${btnVal} 중이던 글이 저장되지 않습니다.\n목록으로 가시겠습니까?`);

	if(con == false){
		return;
	}else if(con == true){
		document.addForm.action = "/boardList.do";
		document.addForm.submit();
	}
}
$listBtn.addEventListener('click', fn_selectList, false);



/* 글 등록 function */
$addBtn.addEventListener('click', e => {
	e.preventDefault();
	
	let valiTxt = [];	// 필드명
	let valiParse = [];	// 에러문구 
	let result = [];	// 에러문구 전체


	const btnVal = document.getElementById("writeBtn").innerText;
	let con = confirm(btnVal + "하시겠습니까?");	// 등록, 수정 확인창

	const flag = document.getElementById("boardSq").value;	// 글 번호 0이면 새 글


	if (con == false) {
		return;
	} else if (con == true) {

		if (resultOfUser == true && resultOfTitle == true && resultOfContent == true) {
			if (flag == 0) {
				frm.action = '/addBoard.do';

				frm.submit();
			} else if (flag > 0) {
				frm.action = '/updateBoard.do';

				frm.submit();
			}
		} else {
		
			if ($title.value.trim() == '') {
				valiTxt.push("제목");
			}

			if ($content.value.trim() == '') {
				valiTxt.push("내용");
			}

			if ($user.value.trim() == '') {
				valiTxt.push("작성자명");
			}

			for (let i = 0; i < valiTxt.length; i++) {
				valiParse.push(`${valiTxt[i]}을 입력해주세요!\n`);
			}

			if (regExp.test($user.value.trim())) {
				valiParse.push("작성자명은 특수문자를 입력할 수 없습니다!\n");
			}

			for (let i = 0; i < valiParse.length; i++) {
				result.push(valiParse[i]);
			}
			alert(result);

			// console.log(valiTxt);
			// console.log(valiParse);
		}

	}
});


const uploadFile = document.getElementById('uploadFile');
const $fileNameDiv = document.getElementById('fileName');
const maxSize = 300 * 1024 * 1024;

// 첨부파일 이벤트(첨부파일명 보임, 용량 300mb 넘으면 알림 팝업)
uploadFile.addEventListener('change', function (event) {
const $uploadFile = event.target;

	$fileNameDiv.innerHTML = '';
	for (let i = 0; i < $uploadFile.files.length; i++) {
		const fileNm = $uploadFile.files[i].name;	// 첨부한 파일명
		const $fileSize = $uploadFile.files[i].size;	// 첨부한 파일 사이즈
		
		
		// 첨부파일 용량 체크: 300mb 초과
		if($uploadFile.files && $fileSize > maxSize){
			alert("파일 용량이 300MB 초과 했습니다.");
			$uploadFile.value = null;
			return;
		}else if(checkFileName(fileNm) == false){
			// 파일 확장자 체크
			alert("허용하지 않는 확장자입니다.");
			$uploadFile.value = null;
			return;
		}else{
			const $addFile = document.createElement('div');	// 첨부파일정보 태그
			$addFile.classList.add("addFile");	// 파일명, 이미지 표시할 태그 생성하고 클래스 추가

			const fileNo = document.createElement('input');
			fileNo.setAttribute("type","hidden");
			fileNo.setAttribute("id","fileNo");
			fileNo.setAttribute("name","fileNo");
			fileNo.setAttribute("value",i);

			$addFile.appendChild(fileNo);	//file 순번을 저장할 태그 추가


			const fileNameEle = document.createElement('p');	// 파일명을 표시할 태그
			fileNameEle.textContent = fileNm;
			$addFile.appendChild(fileNameEle);	//fileName div에 자식 태그로 addFile 추가
			

			//첨부파일명에 해당 확장자명 포함이면 썸네일 제공
			if (/\.(jpe?g|png|gif)$/i.test(fileNm)) {
				const fileImgEle = document.createElement('img');	// 이미지를 표시할 태그
				console.log('contains image!');
				const reader = new FileReader();
	
				reader.onload = function (e) {
					fileImgEle.setAttribute("src", e.target.result);
					fileImgEle.setAttribute("width", "40px");
					fileImgEle.setAttribute("height", "40px");
				}
				reader.readAsDataURL(event.target.files[i]);
				$addFile.appendChild(fileImgEle); 
			} else {
				console.log('not contains image!');
			}

			const $fileDelBtn = document.createElement('i');	// 첨부파일 삭제 버튼
			
			$fileDelBtn.classList.add("fa-solid", "fa-trash-can");
			$fileDelBtn.setAttribute("data-index",`${uploadFile.files[i].lastModified}`);	
			$addFile.appendChild($fileDelBtn);
	
			$fileNameDiv.appendChild($addFile);

		}

		

		const delBtnAll = document.querySelectorAll('.addFile .fa-solid.fa-trash-can');	// 첨부파일 등록 시 나타나는 삭제 버튼
		
		for(const ele of delBtnAll){	
			ele.addEventListener('click', e => {
				
				const dataTransfer = new DataTransfer();
				let files = $uploadFile.files;	//첨부파일 리스트
				const target = e.target;	// click 이벤트 타겟(삭제 버튼)
				const $removeTarget = target.closest('.addFile');	// 삭제할 파일 태그
				const lastModified = e.target.dataset.index; // 삭제할 파일의 lastModified 값
				
				
					Array.from(files) //파일 리스트를 배열로 변환
							 .filter(file => file.lastModified != lastModified) // 선택한 파일 이외의 파일만 가져오도록 필터링
							 .forEach(file => {
								dataTransfer.items.add(file);
							 });	//남은 배열 dataTransfer로 처리(Array -> FileList)
							 
							 files = dataTransfer.files;	// 제거 처리된 FileList 리턴
							 $uploadFile.files = files;		// 현재 파일목록에 반영
							 $removeTarget.remove(); // 해당 첨부파일 정보 화면에서 제거
				
						
			});

		}



	}

});

// 파일 확장자 체크 (허용하는 확장자 외에 불가능)
// 결과 true 정상, false 업로드 불가
function checkFileName(fileName){
	// 허용하는 확장자
	const extReg =  /(hwp|doc|docx|ppt|pptx|xls|xlsx|txt|csv|jpg|jpeg|gif|png|bmp|pdf)$/i;
	const ext = fileName.substr( (fileName.lastIndexOf('.') +1) );
	return extReg.test(ext);
}




// 파일 삭제 처리
const fileDelBtns =  document.querySelectorAll('.fa-solid.fa-trash-can');


for(const ele of fileDelBtns){
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

