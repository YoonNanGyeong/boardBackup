
package egovframework.normal.board.service;

import java.util.List;


public interface UploadFileService {
	
	
	/**
	   * 업로드 파일 등록 - 단건
	   * @param uploadFile
	   * @return 파일Id
	   */
	  Long insertFile(UploadFileVO vo) throws Exception;
	  
	  /**
	   * 업로드파일조회
	   * @param code
	   * @param rid
	   * @return
	   */
	  List<?> selectFileList(UploadFileVO vo) throws Exception;

	  /**
	   * 첨부파일조회
	   * @param uploadfileId
	   * 
	   * @return
	   */
	  UploadFileVO selectFile(UploadFileVO vo);


	  /**
	   * 첨부파일 삭제 by uplaodfileId
	   * @param uploadfileId 첨부파일아이디
	   * @return 삭제한 레코드수
	   */
	   int deleteFile(UploadFileVO vo) throws Exception;
	   
	   /**
	    * 첨부파일 수정
	    * @param vo
	    * @throws Exception
	   */
	   void updateFile(UploadFileVO vo) throws Exception;
	   

}
