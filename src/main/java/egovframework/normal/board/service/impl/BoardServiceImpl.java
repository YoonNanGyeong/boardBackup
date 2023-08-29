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
	/** logger 객체 */
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	// mybatis
	@Resource(name = "boardMapper")
	private BoardMapper boardDAO;


	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 BoardVO
	 * @return 등록한 게시글 번호
	 */
	@Override
	public Long insertBoard(BoardVO vo) throws Exception {
		
		log.debug(vo.toString());

		Long id = boardDAO.insertBoard(vo);

		return id;
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 BoardVO
	 */
	@Override
	public void updateBoard(BoardVO vo) throws Exception {
		boardDAO.updateBoard(vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 BoardVO
	 */
	@Override
	public void deleteBoard(BoardVO vo) throws Exception {
		boardDAO.deleteBoard(vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
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
	 */
	@Override
	public List<?> selectBoardList(BoardDefaultVO searchVO) throws Exception {
		return boardDAO.selectBoardList(searchVO);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 */
	@Override
	public int selectBoardListTotCnt(BoardDefaultVO searchVO) {
		return boardDAO.selectBoardListTotCnt(searchVO);
	}
	

	/**
	 * 조회수 증가
	 * @param BoardVO - 조회수를 증가 시킬  VO
	 */
	@Override
	public void updateViewCnt(BoardVO vo) throws Exception {
		boardDAO.increaseViewCnt(vo);
	}

	
	
	/**
	 * 조회한 글의 이전, 다음글 번호
	 * @param BoardVO - 조회한 글 VO
	 * @return 이전, 다음글 행번호
	 */
	@Override
	public BoardVO boardPrevNext(BoardVO vo) throws Exception {
		return boardDAO.boardPrevNext(vo);
	}

	
	
	/**
	 * 이전, 다음글 번호로 글 조회
	 * @param BoardVO - 조회할 글정보를 담고있는 VO
	 * @return 이전, 다음글
	 */
	@Override
	public List<?> selectPrevNext(BoardVO vo) throws Exception {
		return boardDAO.selectPrevNext(vo);
	}




}
