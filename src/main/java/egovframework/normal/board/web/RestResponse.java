package egovframework.normal.board.web;

public class RestResponse<T> {
	private Header header;
	private T data;
	
	
	public RestResponse(Header header, T data) {
		super();
		this.header = header;
		this.data = data;
	}


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
		    return new RestResponse<>(new Header(rtcd,rtmsg),data);
		  }
	
	
}
