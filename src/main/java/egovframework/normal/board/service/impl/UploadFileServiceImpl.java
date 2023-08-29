package egovframework.normal.board.service.impl;

import egovframework.normal.board.service.UploadFileService;
import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("uploadFileService")
public class UploadFileServiceImpl extends EgovAbstractServiceImpl implements UploadFileService {
	/** logger 객체 */
	private static final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	
	// mybatis
	@Resource(name = "uploadFileMapper")
	private UploadFileMapper uploadFileDAO;
	
	/**
	   * 업로드 파일 등록 
	   * @param vo - 등록할 정보가 담긴 UploadFileVO
	   * @return 등록한 파일 번호
	   */
	@Override
	public Long insertFile(UploadFileVO vo) throws Exception {
		log.debug(vo.toString());
		
		Long fileSq = uploadFileDAO.insertFile(vo);
		return fileSq;
	}

	/**
	   * 파일목록조회
	   * @param vo - 조회할 정보가 담긴 UploadFileVO
	   * @return 조회한 파일 목록
	   */
	@Override
	public List<?> selectFileList(UploadFileVO vo) throws Exception {
		return uploadFileDAO.selectFileList(vo);
	}

	/**
	   * 첨부파일 단건조회
	   * @param vo - 조회할 정보가 담긴 UploadFileVO
	   * @return 조회한 파일
	   */
	@Override
	public UploadFileVO selectFile(UploadFileVO vo) {
		return uploadFileDAO.selectFile(vo);
	}

	/**
	   * 첨부파일 단건 삭제 
	   * @param vo - 삭제할 정보가 담긴 UploadFileVO
	   * @return 삭제 건수
	   */
	@Override
	public int deleteFile(UploadFileVO vo) throws Exception {
		return uploadFileDAO.deleteFile(vo);
	}

	
	/**
	   * 첨부파일 전체 삭제 
	   * @param vo - 삭제할 정보가 담긴 UploadFileVO
	   * @return 삭제 건수
	   */
	@Override
	public int deleteAllFile(UploadFileVO vo) throws Exception {
		return uploadFileDAO.deleteAllFile(vo);
	}

	/**
	    * 첨부파일 순번 조회
	    * @param vo - 조회할 정보가 담긴 UploadFileVO
	    * @return 파일 순번, 번호 목록
	   */
	@Override
	public List<?> selectFileNo(UploadFileVO vo) throws Exception {
		return uploadFileDAO.selectFileNo(vo);
	}

	/**
	   *  첨부파일 순번 수정
	   *  @param vo - 수정할 정보가 담긴 UploadFileVO
	   */
	@Override
	public void updateFileNo(UploadFileVO vo) throws Exception {
		uploadFileDAO.updateFileNo(vo);
	}


}
