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
package egovframework.example.sample.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import egovframework.example.sample.service.CodeVO;
import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.UploadFileVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class EgovSampleController {
	

	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

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
		List<?>resultCodes = sampleService.selectCodeList(codeVO);
	
		Map<String,String> category = new HashMap<>();
	
		for(Object item : resultCodes) {
			CodeVO resultCd = (CodeVO) item;
			category.put(resultCd.getCode(),resultCd.getDecode());
		}	
		
		return category;
		
	}

	
	/**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/egovSampleList.do")
	public String selectSampleList(@ModelAttribute("searchVO") SampleVO searchVO, @RequestParam(value="selectedCd", required=false) String boardCd, ModelMap model) throws Exception {

		System.out.println("-----Get selectSampleList");
		
		/** EgovPropertyService.sample */
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

	
		List<?> sampleList = sampleService.selectSampleList(searchVO);
		model.addAttribute("resultList", sampleList);


		int totCnt = sampleService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
	
		return "sample/egovSampleList";
	}

	/**
	 * 글 등록 화면을 조회한다.
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping(value = "/addSample.do", method = RequestMethod.GET)
	public String addSampleView(@ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		model.addAttribute("sampleVO", new SampleVO());
		return "sample/egovSampleRegister";
	}

	/**
	 * 글을 등록한다.
	 * @param sampleVO - 등록할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/egovSampleList.do"
	 * @exception Exception
	 */
	@RequestMapping(value = "/addSample.do", method = RequestMethod.POST)
	public String addSample(@ModelAttribute("searchVO") SampleDefaultVO searchVO, SampleVO sampleVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {

		// Server-Side Validation
		beanValidator.validate(sampleVO, bindingResult);

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("sampleVO", sampleVO);
			return "sample/egovSampleRegister";
		}

		
		// 파일 업로드 처리
		String loot = null;
		String fileName = null;
		String ogFileName = null;
		MultipartFile uploadFile = sampleVO.getUploadFile();
		
		if(uploadFile != null && !uploadFile.isEmpty()) {
			ogFileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(ogFileName);
			UUID uuid = UUID.randomUUID();
			fileName = uuid + "." + ext;
			loot = "C:\\\\eGovFrameDev-3.10.0-64bit\\\\workspace\\\\.metadata\\\\.plugins\\\\org.eclipse.wst.server.core\\\\tmp0\\\\wtpwebapps\\\\normalBoard_backup\\\\images\\\\egovframework\\\\upload\\\\";
			
			uploadFile.transferTo(new File(loot + fileName));
		}
		
		sampleVO.setFileNm(fileName);
		System.out.println("등록한 글 첨부파일이름: "+sampleVO.getFileNm());
		
		// 게시글 저장
		 Long boardNo = sampleService.insertSample(sampleVO);
//		 model.addAttribute("boardSq",boardNo);
		 System.out.println("boardNo: " + boardNo);
		 
		// 업로드 파일 객체
		UploadFileVO uploadFileVO = new UploadFileVO();
		
		String boardCode = sampleVO.getBoardCd();
		if(boardCode.contains("B") == true) { // boardCd에 B 포함되어 있으면 F로 변경
			boardCode = boardCode.replace("B", "F");
//			System.out.println("파일테이블에 들어갈 코드="+boardCode);
		}
		
//		System.out.println("저장될 글 번호 : "+sampleVO.getBoardSq());

		uploadFileVO.setFileCd(boardCode);
		uploadFileVO.setBoardNo(boardNo);
		uploadFileVO.setStoreNm(fileName);
		uploadFileVO.setUploadNm(ogFileName);
		uploadFileVO.setFileSize(String.valueOf(uploadFile.getSize()));
		uploadFileVO.setFileType(uploadFile.getContentType());
		
		// 업로드 파일 테이블에 저장
		if(uploadFileVO.getStoreNm() != null || uploadFileVO.getUploadNm()!= null) {			
			sampleService.insertFile(uploadFileVO);
		}
		
		status.setComplete();
		return "redirect:/detailSample.do?selectedId=" + boardNo;
	}
	



	/**
	 * 글을 조회한다.
	 * @param sampleVO - 조회할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - 조회한 정보
	 * @exception Exception
	 */
	public SampleVO selectSample( SampleVO sampleVO, @ModelAttribute("searchVO") SampleDefaultVO searchVO) throws Exception {
		return sampleService.selectSample(sampleVO);
	}
	
	/**
	 * 글 상세화면을 조회한다.
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping("/detailSample.do")
	public String detailSampleView(@RequestParam("selectedId") Long boardSq, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)  throws Exception {
		SampleVO sampleVO = new SampleVO();
		sampleVO.setBoardSq(boardSq);
		
		SampleVO vo = selectSample(sampleVO, searchVO);
		System.out.println("상세페이지 객체: " + vo);

		// 변수명은 CoC 에 따라 sampleVO
		model.addAttribute("sampleVO", vo);
		
		return "sample/egovSampleDetail";
	}
	
	/**
	 * 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "egovSampleRegister"
	 * @exception Exception
	 */
	@RequestMapping("/updateSampleView.do")
	public String updateSampleView(@RequestParam("selectedId") Long boardSq, @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model) throws Exception {
		SampleVO sampleVO = new SampleVO();
		sampleVO.setBoardSq(boardSq);
		
		// 변수명은 CoC 에 따라 sampleVO
		model.addAttribute(selectSample(sampleVO, searchVO));
		
		return "sample/egovSampleRegister";
	}

	/**
	 * 글을 수정한다.
	 * @param sampleVO - 수정할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/egovSampleList.do"
	 * @exception Exception
	 */
	@RequestMapping("/updateSample.do")
	public String updateSample(@ModelAttribute("searchVO") SampleDefaultVO searchVO, SampleVO sampleVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {
		
		beanValidator.validate(sampleVO, bindingResult);

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("sampleVO", sampleVO);
			return "sample/egovSampleRegister";
		}
		
		// 파일업로드
		String fileName = null;
		String loot = null;
		MultipartFile uploadFile = sampleVO.getUploadFile();
		if(!uploadFile.isEmpty()) {
			String ogFileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(ogFileName);
			UUID uuid = UUID.randomUUID();
			fileName = uuid + "." + ext;
			loot = "C:\\\\\\\\eGovFrameDev-3.10.0-64bit\\\\\\\\workspace\\\\\\\\.metadata\\\\\\\\.plugins\\\\\\\\org.eclipse.wst.server.core\\\\\\\\tmp0\\\\\\\\wtpwebapps\\\\\\\\normalBoard_backup\\\\\\\\images\\\\\\\\egovframework\\\\\\\\upload\\\\\\\\";
			
			uploadFile.transferTo(new File(loot + fileName));
			
			sampleVO.setFileNm(fileName);
		}
		

		sampleService.updateSample(sampleVO);
		status.setComplete();
		return "forward:/egovSampleList.do";
	}

	/**
	 * 글을 삭제한다.
	 * @param sampleVO - 삭제할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/egovSampleList.do"
	 * @exception Exception
	 */
	@RequestMapping("/deleteSample.do")
	public String deleteSample(SampleVO sampleVO, @ModelAttribute("searchVO") SampleDefaultVO searchVO, SessionStatus status) throws Exception {
		String boardFileNm = sampleVO.getFileNm();
		UploadFileVO uploadFile = new UploadFileVO();
		uploadFile.setStoreNm(boardFileNm);
		
		// 물리 파일 삭제
		String loot = null;
		loot = "C:\\eGovFrameDev-3.10.0-64bit\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\normalBoard\\images\\egovframework\\upload\\";
		
		String filePath = loot + boardFileNm;
		File file = new File(filePath);
		if(file.exists()) {			
			file.delete();
		}
		
		// 게시글 삭제
		sampleService.deleteSample(sampleVO);
		
		// 파일 DB 정보 삭제
		sampleService.deleteFile(uploadFile);
		status.setComplete();
		return "forward:/egovSampleList.do";
	}

	
	
		
		
}
