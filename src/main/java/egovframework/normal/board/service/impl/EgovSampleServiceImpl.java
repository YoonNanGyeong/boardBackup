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
package egovframework.normal.board.service.impl;

import egovframework.normal.board.service.CodeVO;
import egovframework.normal.board.service.EgovSampleService;
import egovframework.normal.board.service.SampleDefaultVO;
import egovframework.normal.board.service.SampleVO;
import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @Class Name : EgovSampleServiceImpl.java
 * @Description : Sample Business Implement Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Service("sampleService")
public class EgovSampleServiceImpl extends EgovAbstractServiceImpl implements EgovSampleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	/** SampleDAO */
	// TODO ibatis 사용
	@Resource(name = "sampleDAO")
	private SampleDAO sampleDAO;
	
	@Resource(name = "codeDAO")
	private CodeDAO codeDAO;
	
	@Resource(name = "uploadFileDAO")
	private UploadFileDAO uploadFileDAO;
	
	@Value("${file.upload.path}")
	private String ROOT_DIR;	//첨부파일 루트


	/** ID Generation */
	@Resource(name = "egovIdGnrService")
	private EgovIdGnrService egovIdGnrService;

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	@Override
	public Long insertSample(SampleVO vo) throws Exception {
		LOGGER.debug(vo.toString());

		LOGGER.debug(vo.toString());

		Long id = sampleDAO.insertSample(vo);
		System.out.println("id: " + id);
	
		return id;
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void updateSample(SampleVO vo) throws Exception {
		sampleDAO.updateSample(vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void deleteSample(SampleVO vo) throws Exception {
		sampleDAO.deleteSample(vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	@Override
	public SampleVO selectSample(SampleVO vo) throws Exception {
		SampleVO resultVO = sampleDAO.selectSample(vo);
//		System.out.println("글 조회 객체 : " + resultVO);

		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		sampleDAO.increaseViewCnt(resultVO);
		return resultVO;
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<?> selectSampleList(SampleDefaultVO searchVO) throws Exception {
		return sampleDAO.selectSampleList(searchVO);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	@Override
	public int selectSampleListTotCnt(SampleDefaultVO searchVO) {
		return sampleDAO.selectSampleListTotCnt(searchVO);
	}
	
	
	/**
	 * 하위코드 반환
	 * @param codePid 부모코드
	 * @return 하위코드
	 */
	@Override
	public List<?> selectCodeList(CodeVO codeVO) throws Exception {
		return codeDAO.selectCodeList(codeVO);
	}

	
	/**
	   * 업로드 파일 등록 - 단건
	   * @param uploadFile
	   * @return 파일Id
	   */
	@Override
	public Long insertFile(UploadFileVO vo) throws Exception {
		return uploadFileDAO.insertFile(vo);
	}

	/**
	   * 업로드파일조회
	   * @param code
	   * @param rid
	   * @return
	   */
	@Override
	public List<?> selectFileList(UploadFileVO vo) throws Exception {
		return uploadFileDAO.selectFileList(vo);
	}

	/**
	   * 첨부파일조회
	   * @param uploadfileId
	   * 
	   * @return
	   */
	@Override
	public UploadFileVO selectFile(UploadFileVO vo) {
		return uploadFileDAO.selectFile(vo);
	}

	/**
	   * 첨부파일 삭제 by uplaodfileId
	   * @param uploadfileId 첨부파일아이디
	   * @return 삭제한 레코드수
	   */
	@Override
	public void deleteFile(UploadFileVO vo) throws Exception {
		uploadFileDAO.deleteFile(vo);
	}


	/**
	 * 조회수 증가
	 */
	@Override
	public void updateViewCnt(SampleVO vo) throws Exception {
		sampleDAO.increaseViewCnt(vo);
	}


	
	
	
	

}
