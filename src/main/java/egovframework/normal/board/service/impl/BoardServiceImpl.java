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

import egovframework.normal.board.service.BoardService;
import egovframework.normal.board.service.BoardDefaultVO;
import egovframework.normal.board.service.BoardVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("boardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardServiceImpl.class);

	@Resource(name = "boardDAO")
	private BoardDAO boardDAO;


	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 BoardVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	@Override
	public Long insertBoard(BoardVO vo) throws Exception {
		LOGGER.debug(vo.toString());

		LOGGER.debug(vo.toString());

		Long id = boardDAO.insertBoard(vo);

		return id;
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 BoardVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void updateBoard(BoardVO vo) throws Exception {
		boardDAO.updateBoard(vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 BoardVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void deleteBoard(BoardVO vo) throws Exception {
		boardDAO.deleteBoard(vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	@Override
	public BoardVO selectBoard(BoardVO vo) throws Exception {
		BoardVO resultVO = boardDAO.selectBoard(vo);

		if (resultVO != null) {			
			boardDAO.increaseViewCnt(resultVO);
		}	
		return resultVO;
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<?> selectBoardList(BoardDefaultVO searchVO) throws Exception {
		return boardDAO.selectBoardList(searchVO);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	@Override
	public int selectBoardListTotCnt(BoardDefaultVO searchVO) {
		return boardDAO.selectBoardListTotCnt(searchVO);
	}
	

	/**
	 * 조회수 증가
	 */
	@Override
	public void updateViewCnt(BoardVO vo) throws Exception {
		boardDAO.increaseViewCnt(vo);
	}

	
	
	/**
	 * 조회한 글 이전 다음글 번호
	 */
	@Override
	public List<?> boardPrevNext(BoardVO vo) throws Exception {
		return boardDAO.boardPrevNext(vo);
	}

	
	
	/**
	 * 이전, 다음글 번호로 글 조회
	 */
	@Override
	public List<?> selectPrevNext(BoardVO vo) throws Exception {
		return boardDAO.selectPrevNext(vo);
	}




}
