package egovframework.normal.board.service.impl;

import java.util.List;

import egovframework.normal.board.service.BoardDefaultVO;
import egovframework.normal.board.service.BoardVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("boardMapper")
public interface BoardMapper {

	/**
	 * 글을 등록한다.
	 */
	public Long insertBoard(BoardVO vo);

	/**
	 * 글을 수정한다.
	 */
	public void updateBoard(BoardVO vo) ;

	/**
	 * 글을 삭제한다.
	 */
	public void deleteBoard(BoardVO vo);

	/**
	 * 글을 조회한다.
	 */
	public BoardVO selectBoard(BoardVO vo);
	
	
	/**
	 * 조회한 글의 이전, 다음글번호
	 */
	public BoardVO boardPrevNext(BoardVO vo);
	
	/**
	 * 글 목록을 조회한다.
	 */
	public List<?> selectBoardList(BoardDefaultVO searchVO);

	/**
	 * 글 총 갯수를 조회한다.
	 */
	public int selectBoardListTotCnt(BoardDefaultVO searchVO);
	
	
	/**
	 * 조회수 증가
	 */
	public void increaseViewCnt(BoardVO vo);
}
