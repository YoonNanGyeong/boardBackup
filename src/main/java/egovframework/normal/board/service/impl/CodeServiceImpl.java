/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.normal.board.service.impl;

import egovframework.normal.board.service.CodeService;
import egovframework.normal.board.service.CodeVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service("codeService")
public class CodeServiceImpl extends EgovAbstractServiceImpl implements CodeService {


	@Resource(name = "codeDAO")
	private CodeDAO codeDAO;

	/**
	 * 하위코드 반환
	 * @param codePid 부모코드
	 * @return 하위코드
	 */
	@Override
	public List<?> selectCodeList(CodeVO codeVO) throws Exception {
		return codeDAO.selectCodeList(codeVO);
	}


	
	
	
	
	

}
