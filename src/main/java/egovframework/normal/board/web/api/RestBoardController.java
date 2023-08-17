package egovframework.normal.board.web.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.normal.board.service.BoardDefaultVO;
import egovframework.normal.board.service.BoardService;
import egovframework.normal.board.service.BoardVO;
import egovframework.normal.board.service.UploadFileService;
import egovframework.normal.board.service.UploadFileVO;


@Controller
@RequestMapping("/api")
public class RestBoardController {
	
	@Resource(name = "boardService")
	private BoardService boardService; // 게시글 서비스 
	
	@Resource(name = "uploadFileService")
	private UploadFileService uploadFileService;  // 첨부파일 서비스 
	
	// 파일 단건 삭제 처리
		@GetMapping("{fileSq}/deleteFile.do")
		@ResponseBody
		public RestResponse<Object> deleteFile(@PathVariable("fileSq")Long fileSq, Model model ) throws Exception{
			
			UploadFileVO uploadFile = new UploadFileVO();
			uploadFile.setFileSq(fileSq);
			int cnt = uploadFileService.deleteFile(uploadFile);	//첨부파일 정보 삭제처리
			
			model.addAttribute("fileSq",fileSq);
			
			RestResponse<Object> result = null;
			
			
		    if(cnt == 1){
		      result = RestResponse.createRestResponse("00", "성공", null);
		    }else{
		      result = RestResponse.createRestResponse("99", "실패", null);
		    }
		    
		    return result;
		}
		
		
		//이전, 다음글 상세조회 
		@PostMapping("/detailBoard.do")
		@ResponseBody
		public RestResponse<Object> detailPrevNext(
						@RequestBody BoardVO boardVO,
						@ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model
						)  throws Exception {
			
			// 이전, 다음행 번호 key, value값으로 가져오기
			List<Map<String, Object>> nextPrev = (List<Map<String, Object>>) boardService.boardPrevNext(boardVO);
			Map<String, Object> resultMap = nextPrev.get(0);  //해당 글 이전, 다음 행번호
			
			// object 타입 -> long 타입 
			Long longPrevNo = 
					(resultMap.get("prevNo") != null) ? Long.valueOf(resultMap.get("prevNo").toString()) : 0L;
			Long longNextNo = 
					(resultMap.get("nextNo") != null) ? Long.valueOf(resultMap.get("nextNo").toString()) : 0L;


			boardVO.setPrevNo(longPrevNo);	
			boardVO.setNextNo(longNextNo);

			// 행번호로 글 조회
			List<Map<String, Object>> nextPrevVO = (List<Map<String, Object>>) boardService.selectPrevNext(boardVO);
			Map<String, Object> resultVO = null; 
			if(nextPrevVO.size() > 0) {				
				resultVO = nextPrevVO.get(0);
			}else {
				resultVO = null;
			}
				
			
			RestResponse<Object> res = null;
			
			if(resultVO != null) {				
				Long boardSq = Long.valueOf(String.valueOf(resultVO.get("boardSq")));
				res = RestResponse.createRestResponse("00", "성공", boardSq);
			}else {
				res = RestResponse.createRestResponse("99", "실패", null);
			}

			return res ;
		} 
		
		// 삭제된 게시글 조회용
		@GetMapping("{boardSq}/selectBoard.do")
		@ResponseBody
		public RestResponse<Object> selectBoard(@PathVariable("boardSq")Long boardSq, Model model)throws Exception{
			RestResponse<Object> res = null;
			
			BoardVO vo = new BoardVO();
			vo.setBoardSq(boardSq);
			
			BoardVO selectedVO = boardService.selectBoard(vo);
			
			if(selectedVO == null) {
				res = RestResponse.createRestResponse("99", "실패", null);
			}else {
				String selectedYn = selectedVO.getUseYn();
				res = RestResponse.createRestResponse("00", "성공", selectedYn);
			}
			
			return res;
		}
		

}
