package egovframework.normal.board.service.impl;

import java.util.List;

import egovframework.normal.board.service.BoardDefaultVO;
import egovframework.normal.board.service.BoardVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("boardMapper")
public interface BoardMapper {

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 BoardVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public Long insertBoard(BoardVO vo) throws Exception;

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 BoardVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateBoard(BoardVO vo) throws Exception;

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 BoardVO
	 * @return void형
	 * @exception Exception
	 */
	public void deleteBoard(BoardVO vo) throws Exception;

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public BoardVO selectBoard(BoardVO vo) throws Exception;
	
	
	/**
	 * 조회한 글의 이전, 다음글 행번호
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return	이전, 다음글 행번호
	 * @throws Exception
	 */
	public List<?> boardPrevNext(BoardVO vo) throws Exception;
	
	/**
	 * 이전, 다음글 번호로 글 조회
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @throws Exception
	 */
	public List<?> selectPrevNext(BoardVO vo) throws Exception;

	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<?> selectBoardList(BoardDefaultVO searchVO) throws Exception;

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectBoardListTotCnt(BoardDefaultVO searchVO);
	
	
	/**
	 * 조회수 증가
	 * @param vo 
	 * @throws Exception
	 */
	public void increaseViewCnt(BoardVO vo) throws Exception;
}