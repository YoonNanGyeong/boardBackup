/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.normal.board.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import egovframework.normal.board.service.CodeVO;
import egovframework.normal.board.service.UploadFileService;
import egovframework.normal.board.service.BoardService;
import egovframework.normal.board.service.BoardDefaultVO;
import egovframework.normal.board.service.BoardVO;
import egovframework.normal.board.service.CodeService;
import egovframework.normal.board.service.UploadFileVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/")
public class BoardController {
	

	@Resource(name = "boardService")
	private BoardService boardService;
	
	@Resource(name = "codeService")
	private CodeService codeService;
	
	@Resource(name = "uploadFileService")
	private UploadFileService uploadFileService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;


	
	// 게시판 코드 디코드 
	@ModelAttribute("category")
	public Map<String,String> classifier()throws Exception{
	CodeVO codeVO = new CodeVO();
		codeVO.setCodePid("B01");
		List<?>resultCodes = codeService.selectCodeList(codeVO);
	
		Map<String,String> category = new HashMap<>();
	
		for(Object item : resultCodes) {
			CodeVO resultCd = (CodeVO) item;
			category.put(resultCd.getCode(),resultCd.getDecode());
		}	
		
		return category;
		
	}


	@RequestMapping(value = "/boardList.do")
	public String selectBoardList(@ModelAttribute("searchVO") BoardVO searchVO, @RequestParam(value="selectedCd", required=false) String boardCd, ModelMap model) throws Exception {

		System.out.println("-----Get selectSampleList");
		
		/** EgovPropertyService.board */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

	
		List<?> boardList = boardService.selectBoardList(searchVO);
		model.addAttribute("resultList", boardList);
		

		int totCnt = boardService.selectBoardListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
	
		return "board/board_list";
	}

	
	@GetMapping(value = "/addBoardView.do")
	public String addBoardView(@ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model) throws Exception {
		model.addAttribute("boardVO", new BoardVO());
		return "board/board_register";
	}


	@PostMapping(value = "/addBoard.do")
	public String addBoard(
			@RequestParam("uploadFile")List<MultipartFile>uploadFile, 
			@ModelAttribute("searchVO") BoardDefaultVO searchVO, BoardVO boardVO, 
			BindingResult bindingResult, Model model, SessionStatus status,
			HttpServletRequest request)
			throws Exception {


		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}
//		String fileName = null;
//		String ogFileName = null;
//		MultipartFile uploadFile = boardVO.getUploadFile();
		
		
//		if(uploadFile != null && !uploadFile.isEmpty()) {
//			ogFileName = uploadFile.getOriginalFilename();	//작성자 첨부파일 이름
//			String ext = FilenameUtils.getExtension(ogFileName);
//			UUID uuid = UUID.randomUUID();
//			fileName = uuid + "." + ext;	// 서버 보관용 파일 이름
//			
//			uploadFile.transferTo(new File(loot + fileName));
//			System.out.println("loot = " + loot);
//			
//		}
		
		// requestParam 확인
		// System.out.println("multiFileList = " + uploadFile);

		// path 가져오기
		ServletContext context = request.getSession().getServletContext();
		
		String loot = context.getRealPath("/images/board/upload");
		
		File fileCheck = new File(loot);
		
		if(!fileCheck.exists()) fileCheck.mkdirs();
		
		List<Map<String, String>> fileList = new ArrayList<>();
		
		
		for(int i = 0; i < uploadFile.size(); i++) {
			String originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
			String ext = originFile.substring(originFile.lastIndexOf("."));
			String changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
			
			Map<String, String> map = new HashMap<>();
			map.put("originFile", originFile);
			map.put("changeFile", changeFile);
			
			fileList.add(map);
		}
		System.out.println("fileList = " + fileList);	// 업로드 파일 확인
		
		// 파일 업로드 처리
		try {
			for(int i = 0; i < uploadFile.size(); i++) {
				File uplaodFile = new File(loot + "\\" + fileList.get(i).get("changeFile"));
				uploadFile.get(i).transferTo(uplaodFile);
			}
			System.out.println("다중 파일 업로드 성공!");
		}catch(IllegalStateException | IOException e){
			System.out.println("다중 파일 업로드 실패...");
			// 업로드 실패 시 파일 삭제
			for(int i = 0; i < uploadFile.size(); i++) {
				new File(loot + "\\" + fileList.get(i).get("changeFile")).delete();
			}
			e.printStackTrace();
		}
		
		
		
		// 게시글 저장
		 Long boardNo = boardService.insertBoard(boardVO);
		 model.addAttribute("boardNo",boardNo);

		 
		// 업로드 파일 객체
//		UploadFileVO uploadFileVO = new UploadFileVO();
//		
//		uploadFileVO.setBoardNo(boardNo);
//		uploadFileVO.setStoreNm(fileName);
//		uploadFileVO.setUploadNm(ogFileName);
//		uploadFileVO.setFileSize(String.valueOf(uploadFile.getSize()));
//		uploadFileVO.setFileType(uploadFile.getContentType());
//		
//		Long fileNo = null;
//		// 업로드 파일 테이블에 저장
//		if(uploadFileVO.getStoreNm() != null || uploadFileVO.getUploadNm()!= null) {			
//			fileNo = uploadFileService.insertFile(uploadFileVO);
//			boardVO.setFileNo(fileNo);
//			boardService.updateBoard(boardVO);
//		}
		
		
		status.setComplete();
		return "redirect:{boardNo}/detailBoard.do";
	}
	


	public BoardVO selectBoard( BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO) throws Exception {
		return boardService.selectBoard(boardVO);
	}
	

	@GetMapping("{selectedId}/detailBoard.do")
	public String detailBoardView(@PathVariable("selectedId") Long boardSq, @ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model)  throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardSq(boardSq);
		
		// 조회한 객체 
		BoardVO vo = selectBoard(boardVO, searchVO);
		   
		// 이전글 다음글 행번호를 가지고 있는 객체
//		BoardVO nextPrev = boardService.boardPrevNext(vo);
//		nextPrev.setBoardCd(vo.getBoardCd());
//		model.addAttribute("nextPrev",nextPrev);
		
		// 행번호로 이전 다음글 조회
//		BoardVO nextPrevVO = boardService.selectPrevNext(nextPrev);
		

		model.addAttribute("boardVO", vo);
		
		return "board/board_detail";
	}
	

	@RequestMapping("updateBoardView.do")
	public String updateBoardView(@RequestParam("selectedId") Long boardSq, @ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardSq(boardSq);
		
		model.addAttribute(selectBoard(boardVO, searchVO));
		
		return "board/board_register";
	}


	@PostMapping("/updateBoard.do")
	public String updateBoard(@ModelAttribute("searchVO") BoardDefaultVO searchVO, 
			BoardVO boardVO, BindingResult bindingResult, Model model, SessionStatus status,
			HttpServletRequest request)
			throws Exception {
		

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}
		
//		// 파일업로드
//		String fileName = null;
//		String loot = null;
//		MultipartFile uploadFile = boardVO.getUploadFile();
//		
//		if(!uploadFile.isEmpty()) {
//			String ogFileName = uploadFile.getOriginalFilename();
//			String ext = FilenameUtils.getExtension(ogFileName);
//			UUID uuid = UUID.randomUUID();
//			fileName = uuid + "." + ext;
//			ServletContext context = request.getSession().getServletContext();
//			loot = context.getRealPath("/images/board/upload");
//			
//			uploadFile.transferTo(new File(loot + fileName));
//			
//			// 업로드 파일 객체
//			UploadFileVO uploadFileVO = new UploadFileVO();
//			uploadFileVO.setBoardNo(boardVO.getBoardSq());
//			uploadFileService.selectFileList(uploadFileVO);
//			
//			
//			Long fileNo = uploadFileService.insertFile(uploadFileVO);
//			
//			boardVO.setFileNo(fileNo);
//		}
		

		boardService.updateBoard(boardVO);
		status.setComplete();
		return "forward:/boardList.do";
	}


	@PostMapping("/deleteBoard.do")
	public String deleteBoard(BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO, SessionStatus status
			, HttpServletRequest request) throws Exception {
		Long boardFileNo = boardVO.getFileNo();
		UploadFileVO uploadFile = new UploadFileVO();
		uploadFile.setFileSq(boardFileNo);
		
//		MultipartFile ufile = boardVO.getUploadFile();
//		String ogFileName = ufile.getOriginalFilename();
//		
//		
//		// 물리 파일 삭제
//		String loot = null;
//		ServletContext context = request.getSession().getServletContext();
//		loot = context.getRealPath("/images/board/upload");
//		
//		String filePath = loot + ogFileName;
//		File file = new File(filePath);
//		if(file.exists()) {			
//			file.delete();
//		}
		
		// 게시글 삭제
		boardService.deleteBoard(boardVO);
		
		// 파일 DB 정보 삭제
//		uploadFileService.deleteFile(uploadFile);
		status.setComplete();
		return "forward:/boardList.do";
	}

	
	
		
		
}
