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
	   void deleteFile(UploadFileVO vo) throws Exception;
	   

}
