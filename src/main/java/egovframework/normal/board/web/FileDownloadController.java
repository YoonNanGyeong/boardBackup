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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.normal.board.service.UploadFileService;


@Controller
public class FileDownloadController {
	
	@Resource(name = "uploadFileService")
	private UploadFileService uploadFileService;
	
	// 첨부파일 다운로드(단건)
	@RequestMapping(value = "fileDownload.do")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response)throws Exception {

		String filename =   request.getParameter("storeNm");

		
        String realFilename = "";
        
        try {			
        	String browser = request.getHeader("User-Agent");
        	// 파일 인코딩
        	if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
        		filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\\\", "%20");
        		System.out.println("filename = " +filename);
        	}else {
        		filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
        	}
		} catch (UnsupportedEncodingException  e) {
			System.out.println("UnsupportedEncodingException 발생");
		}
        
        ServletContext context = request.getSession().getServletContext();
		String loot = context.getRealPath("/images/board/upload/");
        
        realFilename = loot + filename;
 
        File file = new File(realFilename);
        if (!file.exists()) {
            return;
        }
        
        // 파일명 지정
        response.setContentType("application/octer-stream");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
 
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(realFilename);
 
            int cnt = 0;
            byte[] bytes = new byte[(int)file.length()];
 
            while ((cnt = fis.read(bytes)) != -1) {
                os.write(bytes, 0, cnt);
            }
 
            fis.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        	
	}
	
	// 압축파일 다운로드
	@RequestMapping(value = "zipFileDownload.do")
	public void zipFileDownload(HttpServletRequest request, HttpServletResponse response)throws Exception {

		String filename =   request.getParameter("boardSq") + "_files.zip";

		
        String realFilename = "";
        
        try {			
        	String browser = request.getHeader("User-Agent");
        	// 파일 인코딩
        	if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
        		filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\\\", "%20");
        	}else {
        		filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
        	}
		} catch (UnsupportedEncodingException  e) {
			System.out.println("UnsupportedEncodingException 발생");
		}
        
        ServletContext context = request.getSession().getServletContext();
		String loot = context.getRealPath("/images/board/upload/files/");
        
        realFilename = loot + filename;
 
        File file = new File(realFilename);
        if (!file.exists()) {
        	System.out.println("압축파일이 존재하지 않습니다.");
            return;
        }
        
        // 파일명 지정
        response.setContentType("application/zip");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ServletOutputStream so = null;
        BufferedOutputStream bos = null;
        
        try {
            
            fis = new FileInputStream(realFilename);
            bis = new BufferedInputStream(fis);
            so = response.getOutputStream();
            bos = new BufferedOutputStream(so);
 
            int cnt = 0;
            byte[] bytes = new byte[(int)file.length()];
 
            while ((cnt = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, cnt);
                bos.flush();
            }
 
            if(bos != null) bos.close();
            if(bis != null) bis.close();
            if(so != null) so.close();
            if(fis != null) fis.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        	
	}
}
