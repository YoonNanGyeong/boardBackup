const $prevNextBtn = document.getElementById('prevNextCondition');
// const $nextBtn = document.getElementById('nextNo');

$prevNextBtn.addEventListener('click', e => {
	const url = "/detailBoard.do";
	document.detailForm.action = url;
   	document.detailForm.submit();
}, false);
