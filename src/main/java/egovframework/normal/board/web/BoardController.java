package egovframework.normal.board.web;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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

import com.mortennobel.imagescaling.ResampleOp;
import com.mortennobel.imagescaling.AdvancedResizeOp;


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
	
	// 이미지 리사이징 
		public static BufferedImage makeThumbnail(BufferedImage src , int w, int h,String fileName) throws IOException {
	    	BufferedImage thumbImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  
			Graphics2D graphics = thumbImage.createGraphics();  
	        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	        graphics.drawImage(src, 0,0, w, h, null);  
	    	
	        ResampleOp resampleOp = new ResampleOp (w, h);
	        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);

	        BufferedImage rescaled = resampleOp.filter(thumbImage, null);

	        
	       return rescaled;
	    	
	    }
		
		
		
	// 글 목록
	@RequestMapping(value = "/boardList.do")
	public String selectBoardList(@ModelAttribute("searchVO") BoardVO searchVO, @RequestParam(value="selectedCd", required=false) String boardCd, ModelMap model) throws Exception {

		System.out.println("-----Get selectSampleList");
		
		/** EgovPropertyService */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** 페이징 세팅 */
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

	// 등록 화면
	@GetMapping(value = "/addBoardView.do")
	public String addBoardView(@ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model) throws Exception {
		model.addAttribute("boardVO", new BoardVO());
		return "board/board_register";
	}

	// 등록
	@PostMapping(value = "/addBoard.do")
	public String addBoard(
			@RequestParam(value = "uploadFile" ,required = false)List<MultipartFile>uploadFile, 
			@ModelAttribute("searchVO") BoardDefaultVO searchVO, BoardVO boardVO, 
			BindingResult bindingResult, Model model, SessionStatus status,
			HttpServletRequest request)
			throws Exception {


		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}
		

		// 게시글 저장
		 Long boardNo = boardService.insertBoard(boardVO);
		 model.addAttribute("boardNo",boardNo);


		// path 가져오기
		ServletContext context = request.getSession().getServletContext();
		
		String loot = context.getRealPath("/images/board/upload");	// 원본 저장경로
		String loot2 = context.getRealPath("/images/board/upload/thm");	// 썸네일 저장경로
		
		File fileCheck = new File(loot);
		File fileCheck2 = new File(loot2);
		
		if(!fileCheck.exists()) fileCheck.mkdirs();
		if(!fileCheck2.exists()) fileCheck2.mkdirs();
		
		List<Map<String, String>> fileList = new ArrayList<>();
		List<File>files = new ArrayList<>();
		
		UploadFileVO uploadFileVO = new UploadFileVO();
		String originFile = null;
		String ext = null;
		String changeFile = null;
		String thumFileNm = null;
		String fileSize = null;
		String fileType = null;

		Map<String, String> map = new HashMap<>();
		
		if (uploadFile != null && !uploadFile.get(0).isEmpty()) {

			try{
				for(int i = 0; i < uploadFile.size(); i++) {
					originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
					ext = originFile.substring(originFile.lastIndexOf("."));
					changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
					thumFileNm = "thum_" + changeFile;		// 저용량 파일명
					
					map.put("originFile", originFile);
					map.put("changeFile", changeFile);
					map.put("thumFileNm", thumFileNm);
					
					
					fileList.add(map);
									
					fileSize = String.valueOf(uploadFile.get(i).getSize());	//파일크기
					fileType = uploadFile.get(i).getContentType();	//파일타입
					
					// 파일 업로드 처리(서버 저장)
					
					// 원본 파일 객체 생성
					File uplaodFile = new File(loot + "\\" + fileList.get(i).get("changeFile"));
					
					files.add(uplaodFile); // 파일 객체 담기
					
					// 원본 파일 업로드
					uploadFile.get(i).transferTo(uplaodFile);
					
					BufferedImage resizedImage = null;
					int wantWeight = 1000;
					int wantHeight = 1000;
					

					// 썸네일 파일 객체 생성
					File thumFile = new File(loot2 + "\\" + fileList.get(i).get("thumFileNm"));

					if(fileType.contains("image")) {
						
							BufferedImage src = ImageIO.read(uplaodFile);
							
							// 실제 이미지 크기
							int imageWidth = src.getWidth(null);
							int imageHeight = src.getHeight(null);

							// 썸네일이 원본 보다 크지 않게 만들기
							double ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
							
							//비율대로 만들어지는 실제 이미지 크기 구하기
							int w = (int)(imageWidth * ratio);
							int h = (int)(imageHeight * ratio);
							
							resizedImage =makeThumbnail(src, w, h, fileList.get(i).get("changeFile"));					
							ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
							
						
						System.out.println("이미지 리사이징 완료!");
					}
					// 파일정보 db 저장
					uploadFileVO.setUploadNm(originFile);
					uploadFileVO.setStoreNm(changeFile);
					uploadFileVO.setFileSize(fileSize);
					uploadFileVO.setFileType(fileType);
					uploadFileVO.setBoardNo(boardNo);
					uploadFileService.insertFile(uploadFileVO);
					
				}
				
				System.out.println("fileType = "+fileType);
				
				// 첨부파일 타입이 이미지 아닌 경우 thm 경로에 압축하여 저장
				String zipName = boardNo+ "_files";		// 압축파일명
				String zipFilePath = loot2 + "\\" + zipName + ".zip";	// 압축파일 저장경로 + 파일명
				File zipFile = new File(zipFilePath);	//압축파일 객체 생성
				byte[] buf = new byte[4096];	// stream에 사용할 byte 지정
				
					
					// zip 파일 형식으로 파일을 쓰기 위한 출력 스트림 필터 구현하여 글번호_files.zip 파일 생성
					try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))){
						// 파일 객체 리스트로 loop
						for(File file : files) {
							try(FileInputStream in = new FileInputStream(file)){
								// 압축되어지는 파일 파일명 지정
								ZipEntry ze = new ZipEntry(file.getName());
								out.putNextEntry(ze); // 새 zip 파일 항목 쓰기를 시작하고 항목 데이터의 시작에 스트림 배치
								int len;
		
								// FileInputStream을 통해 파일 데이터 읽어들여 ZipOutputStream으로 생성된 zip 파일에 write
								while((len = in.read(buf)) > 0) {
									out.write(buf, 0, len);
								}
								// 현재 zip 항목 닫고 다음 항목을 쓸 수 있도록 스트림 배치
								out.closeEntry();
							}				
						}
					}
				
				System.out.println("다중 파일 업로드 성공!");
				
			}catch(IllegalStateException | IOException e){
				System.out.println("다중 파일 업로드 실패...");
				// 업로드 실패 시 파일 삭제
				for(int i = 0; i < uploadFile.size(); i++) {
					new File(loot + "\\" + fileList.get(i).get("changeFile")).delete();	
					new File(loot2 + "\\" + fileList.get(i).get("thumFileNm")).delete();	
				}
				e.printStackTrace();
			}
			
		}
		
		

		status.setComplete();
		return "redirect:{boardNo}/detailBoard.do";
	}
	
	
	
	// 게시글 조회
	public BoardVO selectBoard( BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO) throws Exception {
		return boardService.selectBoard(boardVO);
	}
	
	// 상세 조회 화면
	@GetMapping("{selectedId}/detailBoard.do")
	public String detailBoardView(@PathVariable("selectedId") Long boardSq, @ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model)  throws Exception {
		System.out.println("상세조회 화면 !");
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardSq(boardSq);
		
		// 조회한 객체 
		BoardVO vo = selectBoard(boardVO, searchVO);
		model.addAttribute("boardVO", vo);
		
		
		UploadFileVO uploadFileVO = new UploadFileVO();
		uploadFileVO.setBoardNo(boardSq);
		
		List<?> fileList = uploadFileService.selectFileList(uploadFileVO);
		model.addAttribute("fileList",fileList);
		
		model.addAttribute("fileSize",fileList.size());


		return "board/board_detail";
	}
	
	
	
	// 수정화면
	@RequestMapping("/updateBoardView.do")
	public String updateBoardView(@RequestParam("selectedId") Long boardSq, @ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model) throws Exception {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardSq(boardSq);
		model.addAttribute(selectBoard(boardVO, searchVO));
		
		UploadFileVO uploadFileVO = new UploadFileVO();
		uploadFileVO.setBoardNo(boardSq);
		
		List<?> fileList = uploadFileService.selectFileList(uploadFileVO);
		model.addAttribute("fileList", fileList);
		model.addAttribute("fileSize",fileList.size());
		
		return "board/board_register";
	}

	// 수정
	@PostMapping("/updateBoard.do")
	public String updateBoard(@ModelAttribute("searchVO") BoardDefaultVO searchVO, 
			BoardVO boardVO, BindingResult bindingResult, Model model, SessionStatus status,
			@RequestParam(value = "uploadFile", required = false)List<MultipartFile>uploadFile,
			HttpServletRequest request)
			throws Exception {
		

		if (bindingResult.hasErrors()) {
			System.out.println("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}
		
		// 게시글 수정
		boardService.updateBoard(boardVO);
		Long boardNo = boardVO.getBoardSq();
		model.addAttribute("boardNo",boardNo);
		
		// path 가져오기
		ServletContext context = request.getSession().getServletContext();
		
		String loot = context.getRealPath("/images/board/upload");	// 저장경로
		String loot2 = context.getRealPath("/images/board/upload/thm");	// 썸네일 저장경로
		
		File fileCheck = new File(loot);
		File fileCheck2 = new File(loot2);
		
		if(!fileCheck.exists()) fileCheck.mkdirs();
		if(!fileCheck2.exists()) fileCheck2.mkdirs();
		
		List<Map<String, String>> fileList = new ArrayList<>();
		
		UploadFileVO uploadFileVO = new UploadFileVO();
		uploadFileVO.setBoardNo(boardNo);
		
		String originFile = null;
		String ext = null;
		String changeFile = null;
		String thumFileNm = null;
		String fileSize = null;
		String fileType = null;
		
	if(uploadFile != null && !uploadFile.get(0).isEmpty()) {
		
		try{
			for(int i = 0; i < uploadFile.size(); i++) {
				
			originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
			ext = originFile.substring(originFile.lastIndexOf("."));
			changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
			thumFileNm =  "thum_" + changeFile;		// 저용량 파일명
			
			Map<String, String> map = new HashMap<>();
			map.put("originFile", originFile);
			map.put("changeFile", changeFile);
			map.put("thumFileNm", thumFileNm);
			fileList.add(map);
			
			fileSize = String.valueOf(uploadFile.get(i).getSize());	//파일크기
			fileType = uploadFile.get(i).getContentType();	//파일타입
			
			// 파일 업로드 처리(서버 저장)
			
			 // 원본 파일 객체 생성
			File uplaodFile = new File(loot + "\\" + fileList.get(i).get("changeFile"));
			
			 // 원본 파일 업로드
			uploadFile.get(i).transferTo(uplaodFile);
			
			originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
			fileType = uploadFile.get(i).getContentType();	//파일타입
			
			BufferedImage resizedImage = null;
			int wantWeight = 1000;
			int wantHeight = 1000;
			
			
			 // 썸네일 파일 객체 생성
			File thumFile = new File(loot2 + "\\" + fileList.get(i).get("thumFileNm"));
			
			
			if(fileType.contains("image")) {
					
					BufferedImage src = ImageIO.read(uplaodFile);
										
					// 실제 이미지 크기
					int imageWidth = src.getWidth(null);
					int imageHeight = src.getHeight(null);

					// 썸네일이 원본 보다 크지 않게 만들기
					double ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
					
					//비율대로 만들어지는 실제 이미지 크기 구하기
					int w = (int)(imageWidth * ratio);
					int h = (int)(imageHeight * ratio);
					
					resizedImage =makeThumbnail(src, w, h, fileList.get(i).get("changeFile"));					
					ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
					System.out.println("png 이미지 리사이징");

					System.out.println("이미지 리사이징 완료!");   
			}else {
				// 이미지 파일이 아닌 경우 리사이징 하지 않고 저장
				uploadFile.get(i).transferTo(thumFile);
			}
			
			// 저장할 정보를 담고 있는 첨부파일 세팅 
			uploadFileVO.setUploadNm(originFile);
			uploadFileVO.setStoreNm(changeFile);
			uploadFileVO.setFileSize(fileSize);
			uploadFileVO.setFileType(fileType);
			
			uploadFileService.insertFile(uploadFileVO); //첨부파일 정보 DB저장
			
			}
			
		 System.out.println("다중 파일 업로드 성공!");
		 
		}catch(IllegalStateException | IOException e){
			
			System.out.println("다중 파일 업로드 실패...");
			
			// 업로드 실패 시 파일 삭제
			for(int i = 0; i < uploadFile.size(); i++) {
				new File(loot + "\\" + fileList.get(i).get("changeFile")).delete();	
				new File(loot2 + "\\" + fileList.get(i).get("thumFileNm")).delete();	
			}
			e.printStackTrace();
		}
		
	}
		

		status.setComplete();
		return "redirect:{boardNo}/detailBoard.do";
	}


	// 게시글 삭제 처리
	@PostMapping("/deleteBoard.do")
	public String deleteBoard(BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO, SessionStatus status) throws Exception {
		UploadFileVO uploadFile = new UploadFileVO();
		Long boardNo = boardVO.getBoardSq();
		uploadFile.setBoardNo(boardNo);
		
		// 게시글 삭제
		boardService.deleteBoard(boardVO);
		
		// 첨부파일 전체 삭제 
		uploadFileService.deleteAllFile(uploadFile);
		
		status.setComplete();
		return "redirect:/boardList.do";
	}

	
	
		
		
}
