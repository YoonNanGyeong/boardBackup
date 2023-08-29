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
package egovframework.normal.board.service;

import java.util.List;


public interface BoardService {

	   

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 BoardVO
	 * @return 등록한 게시글 번호
	 */
	Long insertBoard(BoardVO vo) throws Exception;

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 BoardVO
	 */
	void updateBoard(BoardVO vo) throws Exception;

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 BoardVO
	 */
	void deleteBoard(BoardVO vo) throws Exception;

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 */
	BoardVO selectBoard(BoardVO vo) throws Exception;
	
	/**
	 *  조회한 글의 이전, 다음글 번호
	 * @param BoardVO - 조회한 글 VO
	 * @return 이전, 다음글 행번호 
	 */
	List<?> boardPrevNext(BoardVO vo) throws Exception;
	
	/**
	 * 이전, 다음글 번호로 글 조회
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @throws Exception
	 */
	List<?> selectPrevNext(BoardVO vo) throws Exception;

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 */
	List<?> selectBoardList(BoardDefaultVO searchVO) throws Exception;
	
	
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 */
	int selectBoardListTotCnt(BoardDefaultVO searchVO);
	
	
	/**
	 * 조회수 증가
	 * @param vo
	 * @throws Exception
	 */
	void updateViewCnt(BoardVO vo) throws Exception;

}
