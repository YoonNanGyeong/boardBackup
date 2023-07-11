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

import java.util.List;

import egovframework.normal.board.service.BoardDefaultVO;
import egovframework.normal.board.service.BoardVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import org.springframework.stereotype.Repository;


@Repository("boardDAO")
public class BoardDAO extends EgovAbstractDAO {

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public Long insertBoard(BoardVO vo) throws Exception {
		return (Long)insert("boardDAO.insertBoard", vo);
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateBoard(BoardVO vo) throws Exception {
		update("boardDAO.updateBoard", vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	public void deleteBoard(BoardVO vo) throws Exception {
		delete("boardDAO.deleteBoard", vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public BoardVO selectBoard(BoardVO vo) throws Exception {
		return (BoardVO) select("boardDAO.selectBoard", vo);
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectBoardList(BoardDefaultVO searchVO) throws Exception {
		return list("boardDAO.selectBoardList", searchVO);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectBoardListTotCnt(BoardDefaultVO searchVO) {
		return (Integer) select("boardDAO.selectBoardListTotCnt", searchVO);
	}
	
	
	/**
	 * 조회수 증가
	 * @param vo 
	 * @throws Exception
	 */
	public void increaseViewCnt(BoardVO vo) throws Exception{
		update("boardDAO.updateViewCnt",vo);
	}

}
