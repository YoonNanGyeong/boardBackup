
package egovframework.normal.board.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class BoardVO extends BoardDefaultVO {

	private static final long serialVersionUID = 1L;

	private long boardSq;	// 게시글 순번 board_sq
	private String boardCd; //게시판 코드 board_cd
	private String title;		// 제목 title
	private String content; // 내용 content
	private long viewCnt;	// 조회수 view_cnt
	private String userNm;	// 작성자 user_nm
	private String useYn;	// 사용여부
	private String createDt;	//등록일시
	private String  updateDt;	//수정일시
	
	// 첨부파일
	private List<MultipartFile> uploadFile;
	
	
	// 이전, 다음글 번호
	private long nextNo;
	private long prevNo;

	
	// 이전 다음글 검색 조건
	private String prevNextCondition;
	
	
	

	public String getPrevNextCondition() {
		return prevNextCondition;
	}
	public void setPrevNextCondition(String prevNextCondition) {
		this.prevNextCondition = prevNextCondition;
	}
	public long getNextNo() {
		return nextNo;
	}
	public void setNextNo(long nextNo) {
		this.nextNo = nextNo;
	}
	public long getPrevNo() {
		return prevNo;
	}
	public void setPrevNo(long prevNo) {
		this.prevNo = prevNo;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

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

	public List<MultipartFile> getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(List<MultipartFile> uploadFile) {
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
