package egovframework.normal.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.normal.board.service.CodeVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("codeDAO")
public class CodeDAO extends EgovAbstractDAO{

	// 하위코드 반환
	public List<?> selectCodeList(CodeVO codeVO) throws Exception{
		return list("codeDAO.selectCodeList", codeVO);
	}

}
