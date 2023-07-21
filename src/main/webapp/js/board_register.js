
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
		$content.focus();

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

// 목록버튼 클릭 시 알림창
$listBtn.addEventListener('click', e => {
	e.preventDefault();

	const btnVal = document.getElementById("writeBtn").innerText;
	let con = confirm(`${btnVal} 중이던 글이 저장되지 않습니다. 목록으로 가시겠습니까?`);

	if(con == false){
		return;
	}else if(con == true){
		location.replace("/boardList.do");
	}
}, false);


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
				valiParse.push(`${valiTxt[i]}을 입력해주세요!`);
			}

			if (regExp.test($user.value.trim())) {
				valiParse.push("작성자명은 특수문자를 입력할 수 없습니다!");
			}

			for (let i = 0; i < valiParse.length; i++) {
				result.push(valiParse[i]);
			}
			alert(result);

			console.log(valiTxt);
			console.log(valiParse);
		}

	}
});








// 첨부한 파일명 보이게
const $uploadFile = document.getElementById('uploadFile');
const $fileNameDiv = document.getElementById('fileName');

$uploadFile.addEventListener('change', function (event) {
	$fileNameDiv.innerHTML = '';
	for (let i = 0; i < $uploadFile.files.length; i++) {
		const fileNm = $uploadFile.files[i].name;	// 첨부한 파일명
		const $addFile = document.createElement('div');
		$addFile.classList.add("addFile");	// 파일명, 이미지 표시할 태그 생성하고 클래스 추가

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

		$fileNameDiv.appendChild($addFile);

	}

});


// 첨부파일 개수 제한


