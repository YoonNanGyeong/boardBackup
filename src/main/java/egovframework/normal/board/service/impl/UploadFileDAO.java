package egovframework.normal.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("uploadFileDAO")
public class UploadFileDAO extends EgovAbstractDAO{
	
	 /**
	   * 업로드 파일 등록 - 단건
	   */
	  public Long insertFile(UploadFileVO vo) throws Exception{
		  Long fileSq = (Long) insert("uploadFileDAO.insertFile", vo);
		  select("uploadFileDAO.selectSequence",vo); //null
		  return fileSq;
	  }
	  
	  /**
	   * 파일 순번 시퀀스 초기화
	   */
	  public void updateSequence(UploadFileVO vo)throws Exception{
		  UploadFileVO result = (UploadFileVO)select("uploadFileDAO.selectSequence",vo);
		  Long fileNo = result.getFileNo();
		  vo.setFileNo(fileNo);
		  
		  update("uploadFileDAO.updateIncrement",vo);
		  select("uploadFileDAO.updateSequence",vo);
		  update("uploadFileDAO.backIncrement",vo);
	  }
	  
	  /**
	   * 업로드파일 리스트조회
	   */
	  public List<?> selectFileList(UploadFileVO vo) throws Exception{
		  return list("uploadFileDAO.selectFileList", vo);
	  }

	  /**
	   * 첨부파일조회
	   */
	 public UploadFileVO selectFile(UploadFileVO vo) {
		 return (UploadFileVO) select("uploadFileDAO.selectFile",vo);
	 }


	  /**
	   * 첨부파일 단건 삭제
	   */
	  public int deleteFile(UploadFileVO vo) throws Exception{
		 return delete("uploadFileDAO.deleteFile",vo);
	  }
	  
	  
	  /**
	   * 첨부파일 전체 삭제
	   */
	  public int deleteAllFile(UploadFileVO vo) throws Exception{
		 return delete("uploadFileDAO.deleteAllFile",vo);
	  }
	  
  
}
