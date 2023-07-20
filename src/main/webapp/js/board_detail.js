const $prevNextBtn = document.querySelectorAll('.prevNext-btn');
const $condition = document.getElementById('prevNextCondition');

for(const ele of $prevNextBtn){
	ele.addEventListener('click', e => {
		if(ele.value === 'prev'){
			$condition.value = ele.value;
		}else if(ele.value === 'next'){
			$condition.value = ele.value;
		}

		const url = "/detailBoard.do";
		document.detailForm.action = url;
		document.detailForm.submit();
	}, false);
}
