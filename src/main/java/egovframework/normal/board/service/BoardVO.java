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


import org.springframework.web.multipart.MultipartFile;


public class BoardVO extends BoardDefaultVO {

	private static final long serialVersionUID = 1L;

	private long boardSq;	// 게시글 순번 board_sq
	private String boardCd; //게시판 코드 board_cd
	private String title;		// 제목 title
	private String content; // 내용 content
	private long viewCnt;	// 조회수 view_cnt
	private String userNm;	// 작성자 user_nm
	private String createDt;	//등록일시
	private String  updateDt;	//수정일시
	
	// 첨부파일
	private String fileNm;
	private MultipartFile uploadFile;
	
	

	
	
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public String getUpdateDt() {
		return updateDt;
	}
	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	
	public long getBoardSq() {
		return boardSq;
	}
	public void setBoardSq(long boardSq) {
		this.boardSq = boardSq;
	}
	public String getBoardCd() {
		return boardCd;
	}
	public void setBoardCd(String boardCd) {
		this.boardCd = boardCd;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(long viewCnt) {
		this.viewCnt = viewCnt;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

}
