<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- fontawesome 아이콘 -->
    <script src="https://kit.fontawesome.com/dcfc9e7feb.js" ></script>
    
    <c:set var="registerFlag" value="${empty boardVO.boardCd ? 'create' : 'modify'}"/>
    <title>게시글
    	 <c:if test="${registerFlag == 'create'}">작성</c:if>
         <c:if test="${registerFlag == 'modify'}">수정</c:if>
    </title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/register.css?after'/>"/>
    <script type="text/javascript" language="javaScript" defer = "defer">    
		/* 글 목록 화면 function */
	    function fn_selectList() {
	       	document.detailForm.action = "<c:url value='/boardList.do'/>";
	       	document.detailForm.submit();
	    }
	    
	    /* 글 삭제 function */
	    function fn_delete() {
	    	if(!confirm("삭제하시겠습니까?")){
	    		document.detailForm.action = "<c:url value='/detailBoard.do'/>";
	    	}else{    		
		       	document.detailForm.action = "<c:url value='/deleteBoard.do'/>";
		       	document.detailForm.submit();
	    		alert("삭제완료되었습니다.");
	    	}
	    }
	    
    </script>
</head>
<body >

<form:form method ="post" commandName="boardVO" id="detailForm" name="detailForm"  enctype="multipart/form-data" >
 <input type="hidden" id="boardSq" name="boardSq" value="${boardVO.boardSq}" />
    <!-- 상단바 -->
        <div class="nv-l"></div>
        <nav class="nv">
	        <a href="javascript:fn_selectList();" title="홈(게시글 목록)">
	            <h4>TEST BOARD</h4>
	        </a>
        </nav>
        <div class="nv-r"></div>

        <div class="hd-l"></div>
        <header class="hd">
            <!-- list title -->
            <div id="boardTitle">
                <!-- 글자 강조 박스 -->
                <div id="titleIndex">
                </div>
                <!-- /글자 강조 박스 -->
                <h3>
					<c:if test="${registerFlag == 'create'}">게시글 작성</c:if>
                    <c:if test="${registerFlag == 'modify'}">게시글 수정</c:if>
				</h3>
            </div>
            <!-- /title -->
        </header>
        <div class="hd-r"></div>
        
         <!-- 글 작성 영역 -->
        <div class="mn-l"></div>
        <main class="mn">
            <div class="category-area">
                <label for="boardCd">카테고리</label>            
	                 <form:select path="boardCd" id="boardCd">
	                    <form:option value="B0101" label="공지사항" />
	                    <form:option value="B0102" label="자유게시판"/>
	                    <form:option value="B0103" label="코딩게시판"/>
	                </form:select>
	                <span class = "error error-category"></span>
	             <label id="userNmLabel" for="userNm">작성자</label>
             	<form:input path="userNm"  id="userNm" placeholder="닉네임 입력" maxlength="6"/>
             	 <span class = "error-user hidden"></span>
            </div>
            
            <div class="contents">
                <div class="title-area">
                    <label for="title" id="titleLabel">제목</label>
                    <form:input path="title"  placeholder="제목을 입력하세요." maxlength="45"/>
					<span class = "error-title hidden"></span>
                </div>
                <div class="content-area">
                    <label for="content">내용</label>
                    <form:textarea path="content" name="content" id="content" cols="150" rows="20" placeholder="내용을 입력하세요."/>
					<span class = "error-content hidden"></span>
                </div>
            </div>
            
                        
	            <div class="file-area">
	                <label for="uploadFile">첨부파일</label>
						<div class="file-list">
							<form:input type="file" path="uploadFile" title="첨부파일 추가" multiple="multiple" />
					  
							  <!-- 첨부한 파일명 표시 영역  -->   
							   <div id="fileName">
							   		
							   </div>
						</div>
	           			
	           			<!-- 기존에 첨부된 파일이 있으면 표시  -->   
						<c:if test="${fileSize gt 0}">			
				                <div class="download-area">
		                		  <label for="fileSq" style="margin-bottom: 20px;">
		                		  	첨부파일 다운로드
		                		  	<i class="fa-solid fa-download" style="color: #0070D2;"></i>
	                		  	  </label>
									<c:forEach var="file" items="${fileList}" varStatus="status">
										<input id="fileSq" name="fileSq" value = "${file.fileSq}" style="display:none;" />
			              		  		<div class="files">
						                         <a href="/fileDownload.do?storeNm=${file.storeNm}">
						                        	<input type="text" id="storeNm" value="${file.storeNm}" name="storeNm" readonly="readonly" alt="첨부파일명 링크" style="display: none;"/>
				                        			<input type="text" id="uploadNm" value="${file.uploadNm}" name="uploadNm" readonly="readonly" alt="첨부파일명 링크"/>
							                         <c:if test="${fn:contains(file.fileType,'image')}">	
							                          <img src="<c:url value='/images/board/upload/thm/thum_${file.storeNm}'/>" alt="image" style="width: 40px; height: 40px;">
							                         </c:if>
						                         </a>
			               		  		 <i class="fa-solid fa-trash-can"></i>
			               		  		</div>
									</c:forEach>
				                </div>
						</c:if>
	            </div>
            
            
        </main>
        <div class="mn-r"></div>
        <!-- /글 작성 영역 -->
        
        <div class="ft-l"></div>
        <footer class="ft">
         <div class="btns"> 
			<div class="left-btn">
				<!-- 목록 -->
			   <a  href="javascript:fn_selectList();">
				   <button id="listBtn" type="button">
						   <spring:message code="button.list" />
				   </button>
			   </a>
			</div>
			<div class="right-btn">
				<button id="writeBtn" type="button">
				   <!--  등록 -->
				   <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
				   <!--  수정 -->
				   <c:if test="${registerFlag == 'modify'}"><spring:message code="button.modify" /></c:if>
				</button>
				
						 
			   <!-- 취소 -->
			   <a  href="javascript:document.detailForm.reset();">
				   <button id="delPrevBtn" type="button">         	
						   <spring:message code="button.reset" />
				   </button>
			   </a>
			   
		   
			   <!-- 삭제 -->
			   <c:if test="${registerFlag == 'modify'}">
				   <a href="javascript:fn_delete();">
					   <button id="delPrevBtn" type="button">         	
							   <spring:message code="button.delete" />           
					   </button>
				   </a>      
			   </c:if>
			</div>
	   			 
         </div>   
        </footer>
        <div class="ft-r"></div>
    
    <!-- 검색조건 유지 -->
    <input type="hidden" name="searchCondition" value="<c:out value='${searchVO.searchCondition}'/>"/>
    <input type="hidden" name="searchKeyword" value="<c:out value='${searchVO.searchKeyword}'/>"/>
    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
</form:form>
</body>

<script language="javaScript" src="/js/board_register.js?after"></script>
<script type = "module" language="javaScript" src="/js/deleteFile.js?after"></script>

</html>