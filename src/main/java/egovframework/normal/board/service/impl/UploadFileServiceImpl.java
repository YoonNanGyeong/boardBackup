package egovframework.normal.board.service.impl;

import egovframework.normal.board.service.UploadFileService;
import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service("uploadFileService")
public class UploadFileServiceImpl extends EgovAbstractServiceImpl implements UploadFileService {

	@Resource(name = "uploadFileDAO")
	private UploadFileDAO uploadFileDAO;
	
	/**
	   * 업로드 파일 등록 
	   */
	@Override
	public Long insertFile(UploadFileVO vo) throws Exception {
		Long fileSq = uploadFileDAO.insertFile(vo);
//		uploadFileDAO.updateSequence(vo);
		return fileSq;
	}

	/**
	   * 업로드파일조회
	   */
	@Override
	public List<?> selectFileList(UploadFileVO vo) throws Exception {
		return uploadFileDAO.selectFileList(vo);
	}

	/**
	   * 첨부파일조회
	   */
	@Override
	public UploadFileVO selectFile(UploadFileVO vo) {
		return uploadFileDAO.selectFile(vo);
	}

	/**
	   * 첨부파일 단건 삭제 
	   * 
	   */
	@Override
	public int deleteFile(UploadFileVO vo) throws Exception {
		return uploadFileDAO.deleteFile(vo);
	}

	
	/**
	   * 첨부파일 전체 삭제 
	   * 
	   */
	@Override
	public int deleteAllFile(UploadFileVO vo) throws Exception {
		return uploadFileDAO.deleteAllFile(vo);
	}
		



}
