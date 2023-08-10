package egovframework.normal.board.service;

import java.time.LocalDateTime;

public class UploadFileVO {
		private Long fileSq; 			//	 --파일아이디
		private Long fileNo;			// --파일순번
		private Long boardNo;		//	--참조번호(게시글번호)
		private String storeNm;	//	 --서버보관파일명
		private String uploadNm;	//	 --업로드파일명(유저가 업로드한파일명)
		private String fileSize;		//	 --업로드파일크기(단위byte)
		private String fileType;		//  --파일유형(mimetype)
		private String useYn;	// 사용여부
		private LocalDateTime createDt;	//	 --등록일시

		
		
		public Long getFileNo() {
			return fileNo;
		}
		public void setFileNo(Long fileNo) {
			this.fileNo = fileNo;
		}
		public String getUseYn() {
			return useYn;
		}
		public void setUseYn(String useYn) {
			this.useYn = useYn;
		}
		public Long getFileSq() {
			return fileSq;
		}
		public void setFileSq(Long fileSq) {
			this.fileSq = fileSq;
		}
		public Long getBoardNo() {
			return boardNo;
		}
		public void setBoardNo(Long boardNo) {
			this.boardNo = boardNo;
		}
		public String getStoreNm() {
			return storeNm;
		}
		public void setStoreNm(String storeNm) {
			this.storeNm = storeNm;
		}
		public String getUploadNm() {
			return uploadNm;
		}
		public void setUploadNm(String uploadNm) {
			this.uploadNm = uploadNm;
		}
		public String getFileSize() {
			return fileSize;
		}
		public void setFileSize(String fileSize) {
			this.fileSize = fileSize;
		}
		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		public LocalDateTime getCreateDt() {
			return createDt;
		}
		public void setCreateDt(LocalDateTime createDt) {
			this.createDt = createDt;
		}
		
		
		
}
