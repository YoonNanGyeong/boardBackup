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


}
