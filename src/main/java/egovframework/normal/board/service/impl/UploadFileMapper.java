package egovframework.normal.board.service.impl;

import java.util.List;

import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("uploadFileMapper")
public interface UploadFileMapper {
	
	 /**
	   * 업로드 파일 등록 - 단건
	   */
	  public Long insertFile(UploadFileVO vo) throws Exception;
	  
	  /**
	   * 업로드파일 리스트조회
	   */
	  public List<?> selectFileList(UploadFileVO vo) throws Exception;
	  
	  /**
	   * 첨부파일조회
	   */
	 public UploadFileVO selectFile(UploadFileVO vo);
	 
	 /**
	   * 첨부파일 단건 삭제
	   */
	  public int deleteFile(UploadFileVO vo) throws Exception;
	  
	  /**
	   * 첨부파일 전체 삭제
	   */
	  public int deleteAllFile(UploadFileVO vo) throws Exception;
	  
	  /**
	   *  첨부파일 순번 조회
	   */
	  public List<?> selectFileNo(UploadFileVO vo)throws Exception;
	  
	  /**
	   *  첨부파일 순번 수정
	   */
	  public void updateFileNo(UploadFileVO vo)throws Exception;
}
