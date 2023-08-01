<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>상세조회</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/detail.css?after'/>"/>
      <!-- fontawesome 아이콘 -->
    <script src="https://kit.fontawesome.com/dcfc9e7feb.js" ></script>
    <script type="text/javaScript" language="javascript" defer="defer">
    
    // 글 수정 화면
    function fn_modifyView(id) {
		document.detailForm.selectedId.value = id;
		document.detailForm.action = "<c:url value='/updateBoardView.do'/>";
		document.detailForm.submit();
	} 

    /* 글 목록 화면 function */
    function fn_selectList() {
    	document.detailForm.action = "<c:url value='/boardList.do'/>";
       	document.detailForm.submit();
    }
    
    /* 글 삭제 function */
    function fn_delete() {
    	if(!confirm("삭제 하시겠습니까?")){
    		document.detailForm.action = "<c:url value='/detailBoard.do'/>";
    	}else{    		
	       	document.detailForm.action = "<c:url value='/deleteBoard.do'/>";
	       	document.detailForm.submit();
    		alert("삭제 완료되었습니다.");
    	}
    }
    
        
    </script>
</head>

<body>
<form:form modelAttribute="searchVO" id="detailForm" name="detailForm" method="post">
        <input type="hidden" id="boardCd" name="boardCd" value="${boardVO.boardCd}" />
        <input type="hidden" id="boardSq" name="boardSq" value="${boardVO.boardSq}" />
        <input type="hidden" id="selectedId" name="selectedId" value="${boardVO.boardSq}" />

        
   		 <!-- 상단바 -->
        <div class="nv-l"></div>
        <nav class="nv">
        <a href="javascript:fn_selectList();" title="홈(게시글 목록)">
            <h4>TEST BOARD</h4>
        </a>
        </nav>
        <div class="nv-r"></div>

        <div class="border">
            <div class="hd-l"></div>
            <header class="hd">
            	<div class = "prev-next">
                    <input type="text" id="prevNextCondition" name="prevNextCondition" value="" style="display: none;"/>
            		<button class="prevNext-btn" id="prevNextBtn" name="prevNextBtn"  type="button" value="prev">이전</button>
            		<button class="prevNext-btn" id="prevNextBtn" name="prevNextBtn" type="button" value="next">다음</button>
            	</div>
                <!-- list title -->
                <div class="title">
                    <h4 style = "color: #0070D2;">
						<c:set var = "key" value = "${boardVO.boardCd}"/>
	            		<c:out value = "${category[key]}"/>
					</h4>
                    
                    <h3>
                      <c:out value="${boardVO.title}"/>&nbsp;
					</h3>
                </div>
                <!-- /title -->

                <!-- 작성자명, 시간, 조회수 -->
                <div class="user">
                    <h5>
                    <c:out value="${boardVO.userNm}"/>&nbsp;
					</h5>
                    <div id="ect">
                        <p><c:out value="${boardVO.updateDt}"/>&nbsp;</p>
                        <p>조회</p><p><c:out value="${boardVO.viewCnt}"/>&nbsp;</p>
                        
                    </div>
                </div>
            </header>
            <hr style="color: #D5D9DE;">
            <div class="hd-r"></div>
            
            
            <div class="mn-l"></div>
            <main class="mn">
                <div class="contents">
                    <div class="file-area">
                    	<c:forEach var="file" items="${fileList}" varStatus="status">
		                    <c:if test="${fn:contains(file.fileType,'image')}">	                    
		                      <img src="<c:url value='/images/board/upload/${file.storeNm}'/>" alt="image">
		                    </c:if>
                    	</c:forEach>
                    </div>
                    <div class="content-area">
                       <p><c:out value="${boardVO.content}"/>&nbsp;</p>
                    </div>
                </div>
				<c:if test="${fileSize gt 0}">				
	                <div class="download-title">
	                     <label for="uploadNm" style="margin-right: 0;">첨부파일 다운로드</label>
                         <!-- 다운로드 아이콘(압축파일 다운로드) -->
                        <i class="fa-solid fa-download"></i>
	                </div>
                    <p style="color: #7CA3C6; font-size: 12px;">다운로드 아이콘을 클릭하면 첨부된 파일 전체를 압축파일로 받을 수 있습니다.</p>
		                <div class="download-area">
							<c:forEach var="file" items="${fileList}" varStatus="status">
	              		  		<div class="files">
				                         <a href="/fileDownload.do?storeNm=${file.storeNm}&uploadNm=${file.uploadNm}">
                                            <!-- <input type="hidden" id="fileSq" name="fileSq" value="${file.fileSq}"> -->
				                        	<input type="hidden" id="storeNm" value="${file.storeNm}" name="storeNm" />
				                        	<input type="text" id="uploadNm" value="${file.uploadNm}" name="uploadNm" readonly="readonly" alt="첨부파일명 링크"/>
				                         </a>
	               		  		</div>
							</c:forEach>
		                </div>
				</c:if>
            </main>
            <div class="mn-r"></div>
        </div>
        <div class="ft-l"></div>
        <footer class="ft">
         <div class="btns">
             <div class="left-btn">
                 <!-- 목록 버튼 -->
                 <a href="javascript:fn_selectList();">
                 <button id="listBtn" type= "button">목록</button>
                 </a>
             </div>    

             <div class="right-btn">
                 <!-- 수정 버튼 -->
                 <a href="javascript:fn_modifyView('${boardVO.boardSq}');">
                 <button id="editBtn"  type= "button">수정 </button>
                 </a>
                 <!-- 삭제 버튼 -->
                 <a href="javascript:fn_delete();">
                 <button id="deleteBtn"   type= "button">삭제</button>
                </a>
             </div>             
         </div>   
        </footer>
        <div class="ft-r"></div>
        <!-- 검색조건 유지 -->
        <input type="hidden" id="boardCd2" name="boardCd2" value="${searchBoard.boardCd}"/>
        <input type="hidden" id="searchCondition" name="searchCondition" value="${searchVO.searchCondition}"/>
        <input type="hidden" id="searchKeyword" name="searchKeyword" value="${searchVO.searchKeyword}"/>
        <input type="hidden" id="pageIndex" name="pageIndex" value="${searchVO.pageIndex}"/> 
        
    </form:form>
</body>
<script type ="module" src="/js/detail_isExist.js"></script>
<script type="module" language="javaScript" src="/js/board_detail.js?after"></script>
</html>
