package egovframework.normal.board.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestResponse<T> {
	private Header header;
	private T data; // 제너릭타입 여러 데이터 타입을 가질 수 있음
	
	/** logger 객체 */
	private static final Logger log = LoggerFactory.getLogger(RestResponse.class);
	
	public Header getHeader() {
		return header;
	}
	
	
	public void setHeader(Header header) {
		this.header = header;
	}
	
	
	public T getData() {
		return data;
	}
	
	
	public void setData(T data) {
		this.data = data;
	}
	
	public RestResponse(Header header, T data) {
		  super();
		  this.header = header;
		  this.data = data;
	  }

	// header 코드, 메세지를 생성하는 이너클래스
	public static class Header{
		private String rtcd;
		private String rtmsg;
		
		
		public String getRtcd() {
			return rtcd;
		}
		public void setRtcd(String rtcd) {
			this.rtcd = rtcd;
		}
		public String getRtmsg() {
			return rtmsg;
		}
		public void setRtmsg(String rtmsg) {
			this.rtmsg = rtmsg;
		}
		
		public Header(String rtcd, String rtmsg) {
			super();
			this.rtcd = rtcd;
			this.rtmsg = rtmsg;
		}
		
		
	}
	

	  public static <T> RestResponse<T> createRestResponse(String rtcd,String rtmsg, T data){
		  log.info("rtcd = {}, ", rtcd, "rtmsg = {}", rtmsg); // 결과 코드, 결과 메세지 로그
		    return new RestResponse<>(new Header(rtcd,rtmsg),data); // 제너릭 타입 객체 반환(결과 코드, 메세지, 데이터)
		  }
	
	  
}
