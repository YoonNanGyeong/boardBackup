package egovframework.example.sample.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class FileDownloadController {
	
	@RequestMapping(value = "fileDownload.do")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String filename = request.getParameter("fileNm");
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
        
        realFilename = "C:\\\\eGovFrameDev-3.10.0-64bit\\\\workspace\\\\.metadata\\\\.plugins\\\\org.eclipse.wst.server.core\\\\tmp0\\\\wtpwebapps\\\\normalBoard_backup\\\\images\\\\egovframework\\\\upload\\\\" + filename;
        System.out.println("realFileName= "+realFilename);
 
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
            byte[] bytes = new byte[512];
 
            while ((cnt = fis.read(bytes)) != -1) {
                os.write(bytes, 0, cnt);
            }
 
            fis.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        	
	}
}
