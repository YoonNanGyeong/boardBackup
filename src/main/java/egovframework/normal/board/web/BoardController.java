package egovframework.normal.board.web;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private BoardService boardService; // 게시글 서비스
	
	@Resource(name = "codeService")
	private CodeService codeService; // 코드 서비스
	
	@Resource(name = "uploadFileService")
	private UploadFileService uploadFileService;  // 첨부파일 서비스

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** logger 객체 */
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	
	// 게시판 코드 디코드 
	@ModelAttribute("category")
	public Map<String,String> classifier()throws Exception{
//		CodeVO codeVO = new CodeVO();
//		codeVO.setCodePid("B01"); //조회 정보가 담긴 코드 객체(부모코드)
//		List<?>resultCodes = codeService.selectCodeList(codeVO); //부모코드가 B01 인 코드 목록 조회
	
		 // 코드 목록을 저장할 객체(key : 카테고리 코드 , value : 카테고리명)
		Map<String,String> category = new HashMap<>();
		category.put("B0101","공지사항"); // 카테고리 코드, 카테고리명 map에 넣기
		category.put("B0102","자유게시판"); 
		category.put("B0103","코딩게시판"); 
		
//		for(Object item : resultCodes) { 
//			CodeVO resultCd = (CodeVO) item;	// Object -> CodeVO 타입 캐스팅
//			category.put(resultCd.getCode(),resultCd.getDecode()); // 카테고리 코드, 카테고리명 map에 넣기
//		}	
		
		return category;
		
	}
	
	// 이미지 리사이징 
		public static BufferedImage makeThumbnail(BufferedImage src , int w, int h,String fileName) throws IOException {
	    	BufferedImage thumbImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  // BufferedImage 객체 생성
			Graphics2D graphics = thumbImage.createGraphics();  // 2d 이미지 생성
			
			// 이미지 렌더링 속성 설정(이미지 보간: 알려진 값 사이 중간 값 추정. 화소당 선형보간 세번 수행, 새롭게 생성된 화소는 가장 가까운 화소 4개에 가중치를 곱한 값을 합해서 얻음.)
	        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	        graphics.drawImage(src, 0,0, w, h, null);  // 설정한 가로, 세로 크기로 이미지 그리기
	    	
	        ResampleOp resampleOp = new ResampleOp (w, h); // imageScaling 라이브러리 객체 (조정할 이미지 크기로 생성)
	        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp); // 크기 조정 시 이미지 품질 선명하게 

	        // 단일 입력 및 출력, 두 이미지의 색상 모델이 일치하지 않으면 대상 색상 모델로 색상 변환이 수행 (대상 이미지 null -> 적절한 colorModel 생성)
	        // filter(소스이미지 , 대상 이미지)
	        BufferedImage rescaled = resampleOp.filter(thumbImage, null); 

	        
	       return rescaled; // 리사이징 된 이미지 반환
	    	
	    }
		
	// 첨부파일 압축
		public static void compressZip(List<File>files, File zipFile, byte[] buf) throws IOException{
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
		}
		

		
	// 글 목록
	@RequestMapping(value = "/boardList.do")
	public String selectBoardList(@ModelAttribute("searchVO") BoardVO searchVO, ModelMap model) throws Exception {
		log.info("----- 게시글 목록 화면 -----");
		
		/** EgovPropertyService */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit")); // 게시물 건 수 가져옴
		searchVO.setPageSize(propertiesService.getInt("pageSize")); // 페이지 건 수 가져옴
		

		/** 페이징 세팅 */
		PaginationInfo paginationInfo = new PaginationInfo(); // 페이징 객체 생성
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지 번호
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); //한 페이지 당 게시되는 게시물 건 수
		paginationInfo.setPageSize(searchVO.getPageSize()); //페이지 리스트에 게시되는 페이지 건 수

		/** 검색조건 세팅 */
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); // 검색조건 : 첫번째 행 번호
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex()); // 검색조건 : 마지막 행 번호
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage()); // 검색조건 : 검색한 게시물 건 수

		/** 검색조건 포함 글 목록 조회 */
		List<?> boardList = boardService.selectBoardList(searchVO);
		model.addAttribute("resultList", boardList); // 모델 속성 "resultList"에 글 목록 객체 추가
		
		/** 조회한 글 총 개수 */
		int totCnt = boardService.selectBoardListTotCnt(searchVO); 
		paginationInfo.setTotalRecordCount(totCnt); // 전체 게시물 건 수 
		
		model.addAttribute("paginationInfo", paginationInfo); // 모델 속성 "paginationInfo"에 페이징 객체 추가
		
		return "board/board_list"; // 글 목록 화면 반환
	}

	// 등록 화면
	@GetMapping(value = "/addBoardView.do")
	public String addBoardView( BoardVO boardVO,
			@ModelAttribute("searchVO") BoardDefaultVO searchVO, 
			Model model) throws Exception {
		log.info("----- 게시글 등록 화면 -----");
		model.addAttribute("boardVO", boardVO); // 모델 속성"boardVO"에 게시글 객체 추가
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

		// BindingResult에는 모델의 바인딩 작업 중 발생한 타입 변환 오류정보, 검증 작업에서 발생한 검증 오류 정보가 모두 저장 됨.

		if (bindingResult.hasErrors()) {
			log.info("bindingResult = " + bindingResult); // 발생한 에러 정보를 로그로 남긴다.
			model.addAttribute("boardVO", boardVO); // 모델 속성에 게시글 객체 저장
			return "board/board_register"; // 글 등록 화면 반환
		}
		

		// 게시글 저장
		 Long boardNo = boardService.insertBoard(boardVO); // 저장한 게시글의 글 번호
		 model.addAttribute("boardNo",boardNo); // 모델 속성 "boardNo"에 글 번호 저장


		// path 가져오기
		ServletContext context = request.getSession().getServletContext();
		
		String loot = context.getRealPath("/images/board/upload");	// 원본 저장경로
		String loot2 = context.getRealPath("/images/board/upload/thm");	// 썸네일 저장경로
		String loot3 = context.getRealPath("/images/board/upload/files");	// 압축파일 저장경로
		
		// 파일 객체 생성(원본파일, 썸네일이미지파일, 압축파일)
		File fileCheck = new File(loot);
		File fileCheck2 = new File(loot2);
		File fileCheck3 = new File(loot3);
		
		// 경로 존재하는지 체크 : 경로 존재 안하면 해당 폴더 생성
		if(!fileCheck.exists()) fileCheck.mkdirs();
		if(!fileCheck2.exists()) fileCheck2.mkdirs();
		if(!fileCheck3.exists()) fileCheck3.mkdirs();
		
		 // 저장된 파일이름 정보를 담는 배열 리스트 객체 생성 (key: 파일이름유형명 , value: 파일 이름)
		List<Map<String, String>> fileList = new ArrayList<>();
		List<File>files = new ArrayList<>(); // 파일들을 담을 배열 리스트 객체 생성
		
		UploadFileVO uploadFileVO = new UploadFileVO(); // 파일정보를 담을 첨부파일 객체
		String originFile = null; // 업로드 파일명
		String ext = null; // 업로드 파일명제외 한 확장자명 
		String changeFile = null; // 서버 저장용 파일명
		String thumFileNm = null; // 썸네일 이미지 파일명
		String fileSize = null; // 파일 크기
		String fileType = null; // 파일 유형
		String zipName = null; //압축파일명
		String zipFilePath = null; //저장할 압축파일 경로
		Long fileNo = 0L; // 파일 번호

		Map<String, String> map = new HashMap<>(); //파일명 정보를 담을 map 객체 (key: 파일유형명, value: 파일명)
		
		if (uploadFile != null && !uploadFile.get(0).isEmpty()) {

			try{
				for(int i = 0; i < uploadFile.size(); i++) {
					originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
					ext = originFile.substring(originFile.lastIndexOf("."));
					changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
					thumFileNm = "thum_" + changeFile;		// 썸네일 이미지 파일명
					fileNo = Long.valueOf(i);	//파일 순번 
					
					map.put("originFile", originFile);
					map.put("changeFile", changeFile);
					map.put("thumFileNm", thumFileNm);
					
					
					fileList.add(map);
									
					fileSize = String.valueOf(uploadFile.get(i).getSize());	//파일크기
					fileType = uploadFile.get(i).getContentType();	//파일타입
					
					// 파일 업로드 처리(서버 저장)
					
					// 원본 파일 객체 생성
					File fileObject = new File(loot + "\\" + fileList.get(i).get("changeFile"));
					
					files.add(fileObject); // 파일 객체 담기
					
					// 원본 파일 업로드
					uploadFile.get(i).transferTo(fileObject);
					
					BufferedImage resizedImage = null;
					int wantWeight = 1000;
					int wantHeight = 1000;
					

					// 썸네일 파일 객체 생성
					File thumFile = new File(loot2 + "\\" + fileList.get(i).get("thumFileNm"));

					if(fileType.contains("image")) { //파일 유형이 이미지인 경우
						
							BufferedImage src = ImageIO.read(fileObject);
							
							// 실제 이미지 크기
							int imageWidth = src.getWidth(null);
							int imageHeight = src.getHeight(null);

							// 썸네일이 원본 보다 크지 않게 만들기
							double ratio =Math.min( (double)wantWeight/ (double)wantHeight, 0.1);
							
							//비율대로 만들어지는 실제 이미지 크기 구하기
							int w = (int)(imageWidth * ratio);
							int h = (int)(imageHeight * ratio);
							
							resizedImage =makeThumbnail(src, w, h, fileList.get(i).get("changeFile"));					
							ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
							
						
							log.info("이미지 리사이징 완료!");
					}
					
					
					// 파일정보 db 저장
					uploadFileVO.setUploadNm(originFile);
					uploadFileVO.setStoreNm(changeFile);
					uploadFileVO.setFileSize(fileSize);
					uploadFileVO.setFileType(fileType);
					uploadFileVO.setBoardNo(boardNo);
					uploadFileVO.setFileNo(fileNo);
					uploadFileService.insertFile(uploadFileVO);
					
				}
				
				zipName = boardNo+ "_files";		// 압축파일명
				zipFilePath = loot3 + "\\" + zipName + ".zip";	// 압축파일 저장경로 + 파일명
				File zipFile = new File(zipFilePath);	//압축파일 객체 생성
				byte[] buf = new byte[4096];	// stream에 사용할 byte 지정
				
				// 첨부파일들 모두 압축하여 저장
				compressZip(files, zipFile, buf);
					
				log.info("다중 파일 업로드 성공!");
				
			}catch(IllegalStateException | IOException e){
				log.info("다중 파일 업로드 실패...");
				// 업로드 실패 시 파일 삭제
				for(int i = 0; i < uploadFile.size(); i++) {
					new File(loot + "\\" + fileList.get(i).get("changeFile")).delete();	
					new File(loot2 + "\\" + fileList.get(i).get("thumFileNm")).delete();	
					new File(zipFilePath).delete();
				}
				e.printStackTrace();
			}
			
		}

		status.setComplete(); // 세션 데이터 일괄 삭제
		return "redirect:{boardNo}/detailBoard.do";
	}
	
	
	
	// 게시글 조회
	public BoardVO selectBoard( BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO) throws Exception {
		return boardService.selectBoard(boardVO);
	}
	
	// 상세 조회 화면
	@GetMapping("{selectedId}/detailBoard.do")
	public String detailBoardView(@PathVariable("selectedId") Long boardSq, 
			@ModelAttribute("searchBoard") BoardVO searchBoard,
			@ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model)  throws Exception {
		log.info("----- 상세조회 화면 -----");
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardSq(boardSq);
		
		// 조회한 객체 
		BoardVO vo = selectBoard(boardVO, searchVO);
		model.addAttribute("boardVO", vo);
		
		if(vo == null){
			log.info("존재하지 않는 게시글입니다!");
			
			return "board/board_exist";
		}
		
		
		UploadFileVO uploadFileVO = new UploadFileVO(); // 조회 정보를 담을 파일 객체생성
		uploadFileVO.setBoardNo(boardSq); // 조회정보: 현재 게시글 번호를 객체에 세팅
		
		
		List<?> fileList = uploadFileService.selectFileList(uploadFileVO); // 파일 목록 조회
		model.addAttribute("fileList",fileList); // 모델 속성에 파일목록 객체 저장
		
		model.addAttribute("fileSize",fileList.size()); // 모델 속성에 파일목록의 크기 저장
		
		String updateDt = vo.getUpdateDt(); // 조회한 글 작성일자
		
		LocalDate now = LocalDate.now();	 // 현재 날짜
		updateDt = updateDt.replace(".", "-");
		String[] parts = updateDt.split(" "); 
		String result = parts[0];  // 최근 글 작성 날짜에서 시간 제거
		LocalDate resultDt = LocalDate.parse(result);	// 문자열 -> 날짜 타입
		
		// 열거형 클래스 ChronoUnit : 날짜 기간 단위의 표준 집합 (날짜-시간 조작을 위한 단위 기반 액세스 제공)
		long daysDifference = ChronoUnit.DAYS.between(resultDt, now);	// 날짜 일수 차이 계산(게시글 최종 수정일시, 현재 일시)
		
		Boolean dateResult = true; // 글 작성 이후 30일 경과 여부 (true: 경과하지 않음, false: 경과)  
		
		if(daysDifference > 30) {
			dateResult = false;
		}else {
			dateResult = true;
		}
		
		model.addAttribute("dateResult", dateResult);


		return "board/board_detail";
	}
	
	
	
	// 수정화면
	@RequestMapping("/updateBoardView.do")
	public String updateBoardView(@RequestParam("selectedId") Long boardSq, 
			@ModelAttribute("searchVO") BoardDefaultVO searchVO, Model model) throws Exception {
		BoardVO boardVO = new BoardVO(); // 수정할 게시글을 조회할 정보를 담고 있는 객체
		boardVO.setBoardSq(boardSq); // 글 조회 정보 세팅: 조회할 글 번호
		BoardVO selectedVO = selectBoard(boardVO, searchVO); // 조회 결과 글 객체
		model.addAttribute("boardVO",selectedVO); // 모델 속성에 조회 결과 글 객체를 저장
		
		if(selectedVO == null) { // 수정할 게시글이 존재하지 않는 경우
			log.info("삭제된 게시글 입니다!");
			return "board/board_exist"; // 삭제된 게시글 알람 화면 반환
		}
		
		log.info("----- 게시글 수정 화면 -----");
		
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
			log.info("bindingResult = " + bindingResult);
			model.addAttribute("boardVO", boardVO);
			return "board/board_register";
		}
		
		// 게시글 수정
		boardService.updateBoard(boardVO);
		Long boardNo = boardVO.getBoardSq();
		model.addAttribute("boardNo",boardNo);
		model.addAttribute("boardVO",boardVO);
		
		// path 가져오기
		ServletContext context = request.getSession().getServletContext();
		
		String loot = context.getRealPath("/images/board/upload");	// 저장경로
		String loot2 = context.getRealPath("/images/board/upload/thm");	// 썸네일 저장경로
		String loot3 = context.getRealPath("/images/board/upload/modify");	// 수정된 파일 압축파일 저장경로
		
		// 파일 객체 생성(원본파일, 이미지썸네일파일, 수정된 압축파일)
		File fileCheck = new File(loot);
		File fileCheck2 = new File(loot2);
		File fileCheck3 = new File(loot3);
		
		// 경로 존재하는지 체크: 경로 존재 안하면 해당 폴더 생성
		if(!fileCheck.exists()) fileCheck.mkdirs();
		if(!fileCheck2.exists()) fileCheck2.mkdirs();
		if(!fileCheck3.exists()) fileCheck3.mkdirs();
		
		List<Map<String, String>> fileList = new ArrayList<>();
		List<File>files = new ArrayList<>();
		
		UploadFileVO uploadFileVO = new UploadFileVO(); // 파일정보를 담을 첨부파일 객체

		uploadFileVO.setBoardNo(boardNo); // 첨부파일을 변경할 게시글 번호 세팅
		
		String originFile = null; // 업로드 파일명
		String ext = null; // 업로드 파일명제외 한 확장자명 
		String changeFile = null; // 서버 저장용 파일명
		String thumFileNm = null; // 썸네일 이미지 파일명
		String fileSize = null; // 파일 크기
		String fileType = null; // 파일 유형
		String zipName = null; //압축파일명
		String zipFilePath = null; //저장할 압축파일 경로
		Long fileNo = 0L; // 파일 번호
		
	if(uploadFile != null && !uploadFile.get(0).isEmpty()) {
		
		try{
			for(int i = 0; i < uploadFile.size(); i++) {
				
			originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
			ext = originFile.substring(originFile.lastIndexOf("."));
			changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
			thumFileNm =  "thum_" + changeFile;		// 저용량 파일명
			fileNo = Long.valueOf(i);	//파일 순번 
			
			Map<String, String> map = new HashMap<>();
			map.put("originFile", originFile);
			map.put("changeFile", changeFile);
			map.put("thumFileNm", thumFileNm);
			fileList.add(map);
			
			fileSize = String.valueOf(uploadFile.get(i).getSize());	//파일크기
			fileType = uploadFile.get(i).getContentType();	//파일타입
			
			// 파일 업로드 처리(서버 저장)
			
			 // 원본 파일 객체 생성
			File fileObject = new File(loot + "\\" + fileList.get(i).get("changeFile"));
			files.add(fileObject); // 파일 객체 담기
			
			 // 원본 파일 업로드
			uploadFile.get(i).transferTo(fileObject);
			
			BufferedImage resizedImage = null;
			int wantWeight = 1000;
			int wantHeight = 1000;
			
			
			 // 썸네일 파일 객체 생성
			File thumFile = new File(loot2 + "\\" + fileList.get(i).get("thumFileNm"));
			
			
			if(fileType.contains("image")) {
					
					BufferedImage src = ImageIO.read(fileObject);
										
					// 실제 이미지 크기
					int imageWidth = src.getWidth(null);
					int imageHeight = src.getHeight(null);

					// 썸네일이 원본 보다 크지 않게 만들기
					double ratio =Math.min( (double)wantWeight/ (double)wantHeight, 0.1);
					
					//비율대로 만들어지는 실제 이미지 크기 구하기
					int w = (int)(imageWidth * ratio);
					int h = (int)(imageHeight * ratio);
					
					resizedImage =makeThumbnail(src, w, h, fileList.get(i).get("changeFile"));					
					ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드

					log.info("이미지 리사이징 완료!");
			}
			
			// 저장할 정보를 담고 있는 첨부파일 세팅 
			uploadFileVO.setUploadNm(originFile);
			uploadFileVO.setStoreNm(changeFile);
			uploadFileVO.setFileSize(fileSize);
			uploadFileVO.setFileType(fileType);
			uploadFileVO.setFileNo(fileNo);
			
			uploadFileService.insertFile(uploadFileVO); //첨부파일 정보 DB저장
			}

			
			zipName = boardNo+ "_files_md";		// 압축파일명
		    zipFilePath = loot3 + "\\" + zipName + ".zip";	// 압축파일 저장경로 + 파일명
			File zipFile = new File(zipFilePath);	//압축파일 객체 생성
			byte[] buf = new byte[4096];	// stream에 사용할 byte 지정
			
			// 첨부파일들 모두 압축하여 저장
			compressZip(files, zipFile, buf);
			
		 log.info("다중 파일 업로드 성공!");
		 
		}catch(IllegalStateException | IOException e){
			log.info("다중 파일 업로드 실패...");
			// 업로드 실패 시 파일 삭제
			for(int i = 0; i < uploadFile.size(); i++) {
				new File(loot + "\\" + fileList.get(i).get("changeFile")).delete();	
				new File(loot2 + "\\" + fileList.get(i).get("thumFileNm")).delete();	
				new File(zipFilePath).delete();
			}
			e.printStackTrace();
		}
		
	}
		

		status.setComplete(); // 세션 데이터 일괄 삭제
		return "redirect:{boardNo}/detailBoard.do";
	}


	// 게시글 삭제 처리
	@PostMapping("/deleteBoard.do")
	public String deleteBoard(BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO, SessionStatus status) throws Exception {
		UploadFileVO uploadFile = new UploadFileVO(); // 삭제할 첨부파일 정보를 담고 있는 객체
		Long boardNo = boardVO.getBoardSq(); // 삭제할 게시글 번호
		uploadFile.setBoardNo(boardNo); // 첨부파일 객체에 글 번호 세팅
		
		// 게시글 삭제
		boardService.deleteBoard(boardVO); // use_yn = 'N'
		
		// 첨부파일 전체 삭제 
		uploadFileService.deleteAllFile(uploadFile); // use_yn = 'N'
		
		log.info("----- 게시글  삭제 완료 -----");
		status.setComplete(); // 세션 데이터 일괄 삭제
		return "redirect:/boardList.do";
	}

	
	
		
		
}
