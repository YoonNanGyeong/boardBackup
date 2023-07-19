package egovframework.normal.board.web.api;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.normal.board.service.UploadFileService;
import egovframework.normal.board.service.UploadFileVO;

@Controller
@RequestMapping("/api")
public class RestBoardController {
	
	@Resource(name = "uploadFileService")
	private UploadFileService uploadFileService;
	
	// 파일 단건 삭제 처리
		@ResponseBody
		@GetMapping("{fileSq}/deleteFile.do")
		public RestResponse<Object> deleteFile(@PathVariable("fileSq")Long fileSq, Model model ) throws Exception{
			System.out.println("fileSq = " + fileSq);
			
			UploadFileVO uploadFile = new UploadFileVO();
			uploadFile.setFileSq(fileSq);
			int cnt = uploadFileService.deleteFile(uploadFile);	//첨부파일 정보 삭제처리
			System.out.println("삭제처리 성공 횟수="+cnt);
			
			model.addAttribute("fileSq",fileSq);
			
			RestResponse<Object> result = null;
			
		    if(cnt == 1){
		      result = RestResponse.createRestResponse("00", "성공", null);
		    }else{
		      result = RestResponse.createRestResponse("99", "fail", null);
		    }
		    return result;
		}
		

}
