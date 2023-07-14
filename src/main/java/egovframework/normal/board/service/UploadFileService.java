
package egovframework.normal.board.service;

import java.util.List;


public interface UploadFileService {
	
	
	/**
	   * 업로드 파일 등록 - 단건
	   */
	  Long insertFile(UploadFileVO vo) throws Exception;
	  
	  /**
	   * 업로드파일조회
	   */
	  List<?> selectFileList(UploadFileVO vo) throws Exception;

	  /**
	   * 첨부파일조회
	   */
	  UploadFileVO selectFile(UploadFileVO vo);


	  /**
	   * 첨부파일 삭제 
	   */
	   int deleteFile(UploadFileVO vo) throws Exception;
	  
	   

}
