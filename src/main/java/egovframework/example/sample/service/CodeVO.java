package egovframework.example.sample.service;


import java.time.LocalDateTime;

public class CodeVO {
	private String code; //code id
	private String decode; //코드명
	private String codePid; //부모 코드
	private LocalDateTime createDt;	//등록일시
	private LocalDateTime  updateDt;	//수정일시
	
	
	
	public LocalDateTime getCreateDt() {
		return createDt;
	}
	public void setCreateDt(LocalDateTime createDt) {
		this.createDt = createDt;
	}
	public LocalDateTime getUpdateDt() {
		return updateDt;
	}
	public void setUpdateDt(LocalDateTime updateDt) {
		this.updateDt = updateDt;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDecode() {
		return decode;
	}
	public void setDecode(String decode) {
		this.decode = decode;
	}
	public String getCodePid() {
		return codePid;
	}
	public void setCodePid(String codePid) {
		this.codePid = codePid;
	}
	
	
	
}
