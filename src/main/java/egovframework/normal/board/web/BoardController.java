package egovframework.normal.board.web;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import net.coobird.thumbnailator.Thumbnails;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

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
import org.springframework.web.bind.annotation.ResponseBody;
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
		public static BufferedImage resize(InputStream inputStream, int width, int height) throws IOException {
		    return Thumbnails.of(inputStream).size(width,height).asBufferedImage();
		}
		
		public static BufferedImage makeThumbnail(BufferedImage src , int w, int h,String fileName) throws IOException {
	    	BufferedImage thumbImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  
			Graphics2D graphics = thumbImage.createGraphics();  
	        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	        graphics.drawImage(src, 0,0, w, h, null);  
	    	
	        ResampleOp resampleOp2 = new ResampleOp (w, h);
	        resampleOp2.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);

	        BufferedImage rescaled2 = resampleOp2.filter(thumbImage, null);

	        
	       return rescaled2;
	    	
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
		
		UploadFileVO uploadFileVO = new UploadFileVO();
		
		if (uploadFile != null && !uploadFile.get(0).isEmpty()) {
			System.out.println("업로드 파일 있음!");
			System.out.println("uploadFile = "+uploadFile);
			// 업로드 파일 정보 저장(DB정보 저장)
			for(int i = 0; i < uploadFile.size(); i++) {
				String originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
				String ext = originFile.substring(originFile.lastIndexOf("."));
				String changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
				String thumFileNm = "thum_" + changeFile;		// 저용량 파일명
				
				Map<String, String> map = new HashMap<>();
				map.put("originFile", originFile);
				map.put("changeFile", changeFile);
				map.put("thumFileNm", thumFileNm);
				fileList.add(map);
				
				
				String fileSize = String.valueOf(uploadFile.get(i).getSize());	//파일크기
				String fileType = uploadFile.get(i).getContentType();	//파일타입
				
				
				uploadFileVO.setUploadNm(originFile);
				uploadFileVO.setStoreNm(changeFile);
				uploadFileVO.setFileSize(fileSize);
				uploadFileVO.setFileType(fileType);
				uploadFileVO.setBoardNo(boardNo);
				uploadFileService.insertFile(uploadFileVO);
				
			}
			
			// 파일 업로드 처리(디렉토리에 저장)
			try {
				for(int i = 0; i < uploadFile.size(); i++) {
					
					// 원본 파일 객체 생성
					File uplaodFile = new File(loot + "\\" + fileList.get(i).get("changeFile"));
					
					// 원본 파일 업로드
					uploadFile.get(i).transferTo(uplaodFile);
					
					
					InputStream inputStream = new FileInputStream(uplaodFile);
					String originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
					String fileType = uploadFile.get(i).getContentType();	//파일타입
					
					Image img = null;
					BufferedImage resizedImage = null;
					int wantWeight = 1000;
					int wantHeight = 1000;
					
					
					// 썸네일 파일 객체 생성
					File thumFile = new File(loot2 + "\\" + fileList.get(i).get("thumFileNm"));
					if(fileType.contains("image")) {
						if(originFile.contains("bmp")||originFile.contains("png")||originFile.contains("gif")) {
							BufferedImage src = ImageIO.read(uplaodFile);
							int imageWidth = src.getWidth(null);
							int imageHeight = src.getHeight(null);
							
							double ratio =Math.max((double)wantWeight/ (double)imageWidth, (double)wantHeight/ (double)imageHeight);
							ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
							
							int w = (int)(imageWidth * ratio);
							int h = (int)(imageHeight * ratio);
							
							resizedImage =makeThumbnail(src, w, h, fileList.get(i).get("changeFile"));					
							ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
							System.out.println("png 이미지 리사이징");
						}else{							
							img = new ImageIcon(uplaodFile.toString()).getImage();	//jpeg 포맷
							int imageWidth = img.getWidth(null);
							int imageHeight = img.getHeight(null);
							
							double ratio =Math.max((double)wantWeight/ (double)imageWidth, (double)wantHeight/ (double)imageHeight);
							ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
							
							int w = (int)(imageWidth * ratio);
							int h = (int)(imageHeight * ratio);
							
							resizedImage =resize(inputStream, w, h);			
							ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
							System.out.println("jpg 이미지 리사이징");
						}
						System.out.println("이미지 리사이징 완료!");
					}else {
				
						uploadFile.get(i).transferTo(thumFile);
					}
					
					System.out.println("다중 파일 업로드 성공!");
				}
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
		   
		// 이전글 다음글 행번호를 가지고 있는 객체
//		BoardVO nextPrev = boardService.boardPrevNext(vo);
//		nextPrev.setBoardCd(vo.getBoardCd());
//		model.addAttribute("nextPrev",nextPrev);
		
		// 행번호로 이전 다음글 조회
//		BoardVO nextPrevVO = boardService.selectPrevNext(nextPrev);
//		model.addAttribute("nextPrevVO",nextPrevVO);

		
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
		
	if(uploadFile != null && !uploadFile.get(0).isEmpty()) {
		for(int i = 0; i < uploadFile.size(); i++) {
			String originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
			String ext = originFile.substring(originFile.lastIndexOf("."));
			String changeFile = UUID.randomUUID().toString() + ext;		// 서버 저장용 파일명
			String thumFileNm =  "thum_" + changeFile;		// 저용량 파일명
			
			Map<String, String> map = new HashMap<>();
			map.put("originFile", originFile);
			map.put("changeFile", changeFile);
			map.put("thumFileNm", thumFileNm);
			fileList.add(map);
			
			String fileSize = String.valueOf(uploadFile.get(i).getSize());	//파일크기
			String fileType = uploadFile.get(i).getContentType();	//파일타입
			
			// 저장할 정보를 담고 있는 첨부파일 세팅 
			uploadFileVO.setUploadNm(originFile);
			uploadFileVO.setStoreNm(changeFile);
			uploadFileVO.setFileSize(fileSize);
			uploadFileVO.setFileType(fileType);
			
			uploadFileService.insertFile(uploadFileVO); //첨부파일 정보 추가
			
			
		}
		
		
		// 파일 업로드 처리(디렉토리에 저장)
		try {
			for(int i = 0; i < uploadFile.size(); i++) {
				// 원본 파일 객체 생성
				File uplaodFile = new File(loot + "\\" + fileList.get(i).get("changeFile"));
				
				// 원본 파일 업로드
				uploadFile.get(i).transferTo(uplaodFile);
				
				
				InputStream inputStream = new FileInputStream(uplaodFile);
				String originFile = uploadFile.get(i).getOriginalFilename();	// 업로드 파일명 
				String fileType = uploadFile.get(i).getContentType();	//파일타입
				
				Image img = null;
				BufferedImage resizedImage = null;
				int wantWeight = 1000;
				int wantHeight = 1000;
				
				
				// 썸네일 파일 객체 생성
				File thumFile = new File(loot2 + "\\" + fileList.get(i).get("thumFileNm"));
				
				if(fileType.contains("image")) {
					if(originFile.contains("bmp")||originFile.contains("png")||originFile.contains("gif")) {
						BufferedImage src = ImageIO.read(uplaodFile);
						int imageWidth = src.getWidth(null);
						int imageHeight = src.getHeight(null);
						
						double ratio =Math.max((double)wantWeight/ (double)imageWidth, (double)wantHeight/ (double)imageHeight);
						ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
						
						int w = (int)(imageWidth * ratio);
						int h = (int)(imageHeight * ratio);
						
						resizedImage =makeThumbnail(src, w, h, fileList.get(i).get("changeFile"));					
						ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
						System.out.println("png 이미지 리사이징");
					}else {							
						img = new ImageIcon(uplaodFile.toString()).getImage();	//jpeg 포맷
						int imageWidth = img.getWidth(null);
						int imageHeight = img.getHeight(null);
						
						double ratio =Math.max((double)wantWeight/ (double)imageWidth, (double)wantHeight/ (double)imageHeight);
						ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
						
						int w = (int)(imageWidth * ratio);
						int h = (int)(imageHeight * ratio);
						
						resizedImage =resize(inputStream, w, h);			
						ImageIO.write(resizedImage, "jpg", thumFile);	//리사이징 이미지 해당 경로로 업로드
						System.out.println("jpg 이미지 리사이징");
					}
					System.out.println("이미지 리사이징 완료!");   
				}else {
					uploadFile.get(i).transferTo(thumFile);
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


	// 게시글 삭제 처리
	@PostMapping("/deleteBoard.do")
	public String deleteBoard(BoardVO boardVO, @ModelAttribute("searchVO") BoardDefaultVO searchVO, SessionStatus status
			, HttpServletRequest request) throws Exception {
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
	
	// 파일 단건 삭제 처리
	@ResponseBody
	@GetMapping("/deleteFile.do/{fileSq}")
	public RestResponse<Object> deleteFile(@PathVariable("fileSq")Long fileSq, Model model ) throws Exception{
		UploadFileVO uploadFile = new UploadFileVO();
		uploadFile.setFileSq(fileSq);
		int cnt = uploadFileService.deleteFile(uploadFile);	//첨부파일 정보 삭제처리
		
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
