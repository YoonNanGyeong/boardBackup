package egovframework.normal.board.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import egovframework.normal.board.service.BoardService;
import egovframework.normal.board.service.BoardVO;

@Component
public class BoardValidator implements Validator{
	
	@Resource(name = "boardService")
	private BoardService boardService;

	@Override
	public boolean supports(Class<?> clazz) {
		return BoardVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BoardVO board = (BoardVO) target;
		
		//검증 로직
		if(ObjectUtils.isEmpty(board.getTitle())) {
			errors.rejectValue("title","제목을 입력해주세요",null);
		}
	}

}
