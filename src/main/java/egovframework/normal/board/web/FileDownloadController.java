package egovframework.normal.board.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.normal.board.service.UploadFileService;


@Controller
public class FileDownloadController {
	
	@Resource(name = "uploadFileService")
	private UploadFileService uploadFileService; // 첨부파일 서비스
	
	/** logger 객체 */
	private static final Logger log = LoggerFactory.getLogger(FileDownloadController.class);
	
	// 첨부파일 다운로드(단건)
	@RequestMapping(value = "fileDownload.do")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response)throws Exception {

		String storeFilename =   request.getParameter("storeNm"); // 서버저장 파일명 요청 파라미터로 가져오기
		String uploadFilename =   request.getParameter("uploadNm");  // 업로드 파일명 요청 파라미터로 가져오기
		
        String realFilename = ""; //다운로드 받을 파일경로
        
        try {			
        	String browser = request.getHeader("User-Agent");
        	// HTTP 요청을 보내는 디바이스와 브라우저 등 사용자 소프트웨어의 식별 정보를 담고 있는 request header
        	
        	// 브라우저에 따라 파일명 인코딩
        	if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
        		uploadFilename = URLEncoder.encode(uploadFilename, "UTF-8").replaceAll("\\\\", "%20");
        		// URLEncoder 클래스로 UTF-8 인코딩(2바이트), url에 포함된 백슬래시 인코딩 "%20" : 빈 문자
        	}else {
        		uploadFilename = new String(uploadFilename.getBytes("UTF-8"),"ISO-8859-1"); 
        		// ISO-8859-1 인코딩(1바이트)
        	}
		} catch (UnsupportedEncodingException  e) {
			// 지원되지 않는 인코딩 형식 예외가 발생했을 경우 에러 로그 남김
			log.trace("UnsupportedEncodingException 발생");
		}
        
        ServletContext context = request.getSession().getServletContext();
		String loot = context.getRealPath("/images/board/upload/"); // 세션에서 루트 경로 가져오기 
        
        realFilename = loot + storeFilename; //다운로드 받을 파일경로
 
        File file = new File(realFilename); //파일 객체 생성
        if (!file.exists()) {
        	log.info("파일이 존재하지 않습니다.");
            return;
        }
        
        // 파일명 지정
        
        response.setContentType("application/octer-stream"); // MIME 개별타입 중 application 에 속하는 타입, //8비트 단위 binary data (기본값)
        response.setHeader("Content-Transfer-Encoding", "binary"); // 전송 데이터의 body 인코딩 방법 binary : 이진수 데이터
        response.setHeader("Content-Disposition", "attachment; filename=\"" + uploadFilename + "\""); // 다운로드 여부를 설정, 다른이름으로 저장 
 
        try {
            OutputStream os = response.getOutputStream(); // httpServletResponse 객체를 통해 http 응답 바디에 데이터를 쓸 수 있는 스트림을 얻어옴
            FileInputStream fis = new FileInputStream(realFilename); // 파일에서 데이터를 읽기 위한 스트림 객체 생성
 
            int cnt = 0;
            byte[] bytes = new byte[(int)file.length()]; // 파일을 바이트 배열로 생성
 
            while ((cnt = fis.read(bytes)) != -1) { // 다운로드 파일이 존재하면
                os.write(bytes, 0, cnt); // bytes[0] 부터 cnt 개의 바이트를 출력 스트림으로 보냄
            }
 
            fis.close();
            os.close();
        } catch (Exception e) {
           log.trace("Exception 발생!");
        }
 
        	
	}
	
	// 압축파일 다운로드
	@RequestMapping(value = "zipFileDownload.do")
	public void zipFileDownload(HttpServletRequest request, HttpServletResponse response)throws Exception {

		String filename =   request.getParameter("boardSq") + "_files.zip"; // 압축파일명 

		
        String realFilename = ""; // 다운로드받을 파일 경로
        
        try {			
        	String browser = request.getHeader("User-Agent");
        	// HTTP 요청을 보내는 디바이스와 브라우저 등 사용자 소프트웨어의 식별 정보를 담고 있는 request header
        	
        	// 브라우저에 따라 파일명 인코딩
        	if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
        		filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\\\", "%20");
        		// URLEncoder 클래스로 UTF-8 인코딩(2바이트), url에 포함된 백슬래시 인코딩 "%20" : 빈 문자
        	}else {
        		filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
        		// ISO-8859-1 인코딩(1바이트)
        	}
		} catch (UnsupportedEncodingException  e) {
			// 지원되지 않는 인코딩 형식 예외가 발생했을 경우 에러 로그 남김
			log.info("UnsupportedEncodingException 발생");
		}
        
        ServletContext context = request.getSession().getServletContext();
		String loot = context.getRealPath("/images/board/upload/files/"); // 다운로드 받는 루트 경로 얻기 
        
        realFilename = loot + filename; // 다운로드 파일 경로
 
        File file = new File(realFilename); // 다운로드 받을 파일 객체 생성
        if (!file.exists()) { // 파일이 존재하지 않을 경우
        	log.info("압축파일이 존재하지 않습니다.");
            return;
        }
        
        // 파일명 지정
        response.setContentType("application/zip");  // MIME 개별타입 중 application 에 속하는 타입, // 종류: zip 아카이브
        response.setHeader("Content-Transfer-Encoding", "binary"); // 전송 데이터의 body 인코딩 방법 binary : 이진수 데이터
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\""); // 다운로드 여부를 설정, 다른이름으로 저장 
        
        FileInputStream fis = null; // 파일에서 데이터를 읽기 위한 스트림 객체 
        BufferedInputStream bis = null; // 바이트 단위로 파일을 읽어 올 때 사용하는 스트림
        ServletOutputStream so = null;	// 바이트 기반 출력 스트림 최상위 클래스 OuputStream 상속, 추상클래스라서 인스턴스를 생성할 수 없음 
        BufferedOutputStream bos = null;	// 바이트 단위로 파일을 출력 할 때 사용하는 스트림
        
        try {
            
            fis = new FileInputStream(realFilename);  // 파일에서 데이터를 읽기 위한 스트림 객체 생성
            bis = new BufferedInputStream(fis); // 바이트 단위 스트림 생성
            so = response.getOutputStream(); // servletResponse에서 인스턴스를 받음
            bos = new BufferedOutputStream(so); // 바이트 단위 출력 스트림 생성
 
            int cnt = 0;
            byte[] bytes = new byte[(int)file.length()]; // 파일을 바이트 배열로 생성
 
            while ((cnt = bis.read(bytes)) != -1) {// 다운로드 파일이 존재하면
                bos.write(bytes, 0, cnt); // bytes[0] 부터 cnt 개의 바이트를 출력 스트림으로 보냄
                bos.flush(); // 현재 저장되어 있는 내용 클라이언트로 전송 후 버퍼를 비움
            }
 
            // 스트림이 존재하지 않으면 연결한 스트림을 해제한다.
            if(bos != null) bos.close();
            if(bis != null) bis.close();
            if(so != null) so.close();
            if(fis != null) fis.close();
            
        } catch (Exception e) {
        	log.trace("Exception 발생!");
        }
 
        	
	}
}
