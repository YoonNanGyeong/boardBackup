package egovframework.normal.board.web.api;

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
			
			UploadFileVO uploadFile = new UploadFileVO(); //첨부파일 정보를 담을 객체 생성
			uploadFile.setFileSq(fileSq); // url 경로로 얻은 파일번호로 세팅
			int cnt = uploadFileService.deleteFile(uploadFile);	//첨부파일 정보 삭제처리, 삭제 건 수 반환
			
			model.addAttribute("fileSq",fileSq); // 삭제된 파일 번호 모델 속성에 저장
			
			RestResponse<Object> result = null;
			
			
		    if(cnt == 1){ // 삭제 건수가 1이면
		      result = RestResponse.createRestResponse("00", "성공", null); // 성공 코드, 메세지
		    }else{
		      result = RestResponse.createRestResponse("99", "실패", null); // 실패 코드, 메세지
		    }
		    
		    return result; // 결과 코드, 메세지, 데이터를 담고 있는 RestResponse 객체 반환
		}
		
		
		//이전, 다음글 상세조회 
		@PostMapping("/detailBoard.do")
		@ResponseBody
		public RestResponse<Object> detailPrevNext(
						@RequestBody BoardVO boardVO,
						@ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model
						)  throws Exception {	
			BoardVO prevNextVO = boardService.boardPrevNext(boardVO); // 이전, 다음글 번호 조회
			prevNextVO.setPrevNextCondition(boardVO.getPrevNextCondition()); // 검색조건 유지(이전, 다음버튼 여부)
			
		    BoardVO resultVO = boardService.selectBoard(prevNextVO); // 이전, 다음글 조회
		    
			RestResponse<Object> res = null;
			
			if(resultVO != null) {				
				Long boardSq = resultVO.getBoardSq(); // 조회한 글의 글 번호
				res = RestResponse.createRestResponse("00", "성공", boardSq); // 성공 했을 경우 코드, 메세지, 글번호 데이터 
			}else {
				res = RestResponse.createRestResponse("99", "실패", null); // 실패 했을 경우 코드, 메세지 
			}

			return res ;
		} 
		
		// 삭제된 게시글 조회용
		@GetMapping("{boardSq}/selectBoard.do")
		@ResponseBody
		public RestResponse<Object> selectBoard(@PathVariable("boardSq")Long boardSq, Model model)throws Exception{
			RestResponse<Object> res = null;
			
			BoardVO vo = new BoardVO(); // 조회할 정보를 담고 있는 객체
			vo.setBoardSq(boardSq); // 조회할 글 번호 세팅
			
			BoardVO selectedVO = boardService.selectBoard(vo); // 글 조회
			
			if(selectedVO == null) { // 글 존재 하지 않으면
				res = RestResponse.createRestResponse("99", "실패", null); // 실패 코드, 메세지
			}else { // 글 존재하면
				String selectedYn = selectedVO.getUseYn(); // 조회한 글의 삭제 여부 값 
				res = RestResponse.createRestResponse("00", "성공", selectedYn); // 성공 코드, 메세지, 글 삭제여부 데이터
			}
			
			return res;
		}
		

}
