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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;


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

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator; 

	
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
	public String addBoard(@ModelAttribute("searchVO") BoardDefaultVO searchVO, BoardVO boardVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {

		// Server-Side Validation
		beanValidator.validate(boardVO, bindingResult);

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}

		
		// 파일 업로드 처리
		String loot = null;
		String fileName = null;
		String ogFileName = null;
		MultipartFile uploadFile = boardVO.getUploadFile();
		
		if(uploadFile != null && !uploadFile.isEmpty()) {
			ogFileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(ogFileName);
			UUID uuid = UUID.randomUUID();
			fileName = uuid + "." + ext;
			loot = "C:\\\\eGovFrameDev-3.10.0-64bit\\\\workspace\\\\.metadata\\\\.plugins\\\\org.eclipse.wst.server.core\\\\tmp0\\\\wtpwebapps\\\\normalBoard_backup\\\\images\\\\board\\\\upload\\\\";
			
			uploadFile.transferTo(new File(loot + fileName));
		}
		
		boardVO.setFileNm(fileName);
		System.out.println("등록한 글 첨부파일이름: "+boardVO.getFileNm());
		
		// 게시글 저장
		 Long boardNo = boardService.insertBoard(boardVO);
		 model.addAttribute("boardNo",boardNo);

		 System.out.println("boardNo: " + boardNo);
		 
		// 업로드 파일 객체
		UploadFileVO uploadFileVO = new UploadFileVO();
		
		String boardCode = boardVO.getBoardCd();
		if(boardCode.contains("B") == true) { // boardCd에 B 포함되어 있으면 F로 변경
			boardCode = boardCode.replace("B", "F");
		}
		

		uploadFileVO.setFileCd(boardCode);
		uploadFileVO.setBoardNo(boardNo);
		uploadFileVO.setStoreNm(fileName);
		uploadFileVO.setUploadNm(ogFileName);
		uploadFileVO.setFileSize(String.valueOf(uploadFile.getSize()));
		uploadFileVO.setFileType(uploadFile.getContentType());
		
		// 업로드 파일 테이블에 저장
		if(uploadFileVO.getStoreNm() != null || uploadFileVO.getUploadNm()!= null) {			
			uploadFileService.insertFile(uploadFileVO);
		}
		
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
		
		BoardVO vo = selectBoard(boardVO, searchVO);

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
	public String updateBoard(@ModelAttribute("searchVO") BoardDefaultVO searchVO, BoardVO boardVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {
		
		beanValidator.validate(boardVO, bindingResult);

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}
		
		// 파일업로드
		String fileName = null;
		String loot = null;
		MultipartFile uploadFile = boardVO.getUploadFile();
		if(!uploadFile.isEmpty()) {
			String ogFileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(ogFileName);
			UUID uuid = UUID.randomUUID();
			fileName = uuid + "." + ext;
			loot = "C:\\\\\\\\eGovFrameDev-3.10.0-64bit\\\\\\\\workspace\\\\\\\\.metadata\\\\\\\\.plugins\\\\\\\\org.eclipse.wst.server.core\\\\\\\\tmp0\\\\\\\\wtpwebapps\\\\\\\\normalBoard_backup\\\\\\\\images\\\\\\\\board\\\\\\\\upload\\\\\\\\";
			
			uploadFile.transferTo(new File(loot + fileName));
			
			boardVO.setFileNm(fileName);
		}
		

		boardService.updateBoard(boardVO);
		status.setComplete();
		return "forward:/boardList.do";
	}


	@PostMapping("/deleteBoard.do")
	public String deleteBoard(BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO, SessionStatus status) throws Exception {
		String boardFileNm = boardVO.getFileNm();
		UploadFileVO uploadFile = new UploadFileVO();
		uploadFile.setStoreNm(boardFileNm);
		
		// 물리 파일 삭제
		String loot = null;
		loot = "C:\\eGovFrameDev-3.10.0-64bit\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\normalBoard\\images\\board\\upload\\";
		
		String filePath = loot + boardFileNm;
		File file = new File(filePath);
		if(file.exists()) {			
			file.delete();
		}
		
		// 게시글 삭제
		boardService.deleteBoard(boardVO);
		
		// 파일 DB 정보 삭제
		uploadFileService.deleteFile(uploadFile);
		status.setComplete();
		return "forward:/boardList.do";
	}

	
	
		
		
}
