/*
 * Copyright 2011 MOPAS(Ministry of Public Administration and Security).
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
package egovframework.example.sample.service.impl;

import java.util.List;

import egovframework.example.sample.service.CodeVO;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.UploadFileVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * sample에 관한 데이터처리 매퍼 클래스
 *
 * @author  표준프레임워크센터
 * @since 2014.01.24
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *          수정일          수정자           수정내용
 *  ----------------    ------------    ---------------------------
 *   2014.01.24        표준프레임워크센터          최초 생성
 *
 * </pre>
 */
@Mapper("sampleMapper")
public interface SampleMapper {

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	void insertSample(SampleVO vo) throws Exception;

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
	void updateViewCnt(SampleVO vo)throws Exception;
	
	
	/**
	 * 하위 코드 반환
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	List<?> selectCodeList(CodeVO codeVO) throws Exception;
	
	
	/**
	 * 전체 코드 반환
	 * @param codeVO
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAllCodeList(CodeVO codeVO) throws Exception;
	
	/**
	   * 업로드 파일 등록 - 단건
	   * @param uploadFile
	   * @return 파일Id
	   */
	  public Long insertFile(UploadFileVO vo) throws Exception;
	  
	  /**
	   * 업로드파일조회
	   * @param code
	   * @param rid
	   * @return
	   */
	  public List<?> selectFileList(UploadFileVO vo) throws Exception;

	  /**
	   * 첨부파일조회
	   * @param 
	   * 
	   * @return
	   */
	 public UploadFileVO selectFile(UploadFileVO vo);


	  /**
	   * 첨부파일 삭제 by uplaodfileId
	   * @param 
	   * @return 
	   */
	  public void deleteFile(UploadFileVO vo) throws Exception;
	  
	  
	  /**
	   * 게시글 파일명, 타입 조회
	 * @param vo 
	 * @return
	 */
	public UploadFileVO findByfileNm(SampleVO vo);

}
