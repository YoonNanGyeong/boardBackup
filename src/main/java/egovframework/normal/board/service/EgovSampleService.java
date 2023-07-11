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
package egovframework.normal.board.service;

import java.util.List;

/**
 * @Class Name : EgovSampleService.java
 * @Description : EgovSampleService Class
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
public interface EgovSampleService {
	
	
	
	/**
	 * 하위코드 반환
	 * @param codePid 부모코드
	 * @return 하위코드
	 */
	List<?> selectCodeList(CodeVO codeVO) throws Exception;
	
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
	   void deleteFile(UploadFileVO vo) throws Exception;
	   

	   
	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	Long insertSample(SampleVO vo) throws Exception;

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void updateSample(SampleVO vo) throws Exception;

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	void deleteSample(SampleVO vo) throws Exception;

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	SampleVO selectSample(SampleVO vo) throws Exception;

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectSampleList(SampleDefaultVO searchVO) throws Exception;
	
	
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	int selectSampleListTotCnt(SampleDefaultVO searchVO);
	
	
	/**
	 * 조회수 증가
	 * @param vo
	 * @throws Exception
	 */
	void updateViewCnt(SampleVO vo) throws Exception;

}
