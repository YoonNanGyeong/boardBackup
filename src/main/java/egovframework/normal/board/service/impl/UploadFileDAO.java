package egovframework.normal.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("uploadFileDAO")
public class UploadFileDAO extends EgovAbstractDAO{
	
	 /**
	   * 업로드 파일 등록 - 단건
	   * @param 
	   * @return 
	   */
	  public Long insertFile(UploadFileVO vo) throws Exception{
		  return (Long) insert("uploadFileDAO.insertFile", vo);
	  }
	  
	  /**
	   * 업로드파일 리스트조회
	   * @param 
	   * @param 
	   * @return
	   */
	  public List<?> selectFileList(UploadFileVO vo) throws Exception{
		  return list("uploadFileDAO.selectFileList", vo);
	  }

	  /**
	   * 첨부파일조회
	   * @param
	   * 
	   * @return
	   */
	 public UploadFileVO selectFile(UploadFileVO vo) {
		 return (UploadFileVO) select("uploadFileDAO.selectFile",vo);
	 }


	  /**
	   * 첨부파일 삭제
	   * @param 
	   * @return 
	   */
	  public int deleteFile(UploadFileVO vo) throws Exception{
		 return delete("uploadFileDAO.deleteFile",vo);
	  }
	  
	  /**
	   * 첨부파일 수정
	   * @param vo
	   * @throws Exception
	  */
	public void updateFile(UploadFileVO vo) throws Exception{
		  update("uploadFileDAO.updateFile",vo);
	  }
	  
	  
	  
	  
	 
  
}
