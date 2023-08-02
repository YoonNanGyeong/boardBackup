<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- fontawesome 아이콘 -->
    <script src="https://kit.fontawesome.com/dcfc9e7feb.js" ></script>
    
    <c:set var="registerFlag" value="${empty boardVO.title ? 'create' : 'modify'}"/>
    <title>게시글
    	 <c:if test="${registerFlag == 'create'}">작성</c:if>
         <c:if test="${registerFlag == 'modify'}">수정</c:if>
    </title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/register.css?after'/>"/>
	<script>
		
	/* 글 삭제 function */
	function fn_delete() {
		if(!confirm("삭제하시겠습니까?")){
			document.addForm.action = "/detailBoard.do";
		}else{    		
				document.addForm.action = "/deleteBoard.do";
				document.addForm.submit();
			alert("삭제완료되었습니다.");
		}
	}
	</script>
</head>
<body>
<form:form method ="post" commandName="boardVO" id="addForm" name="addForm"  enctype="multipart/form-data" >
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
             	<form:input path="userNm"  id="userNm" placeholder="닉네임 입력" maxlength="15"/>
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
	                <label for="uploadFile">첨부파일 업로드</label> 
					<p style="color: #0070D2; font-size: 14px;">최대 300MB까지 업로드 가능 합니다.</p>
					<p style="color: #7CA3C6; font-size: 12px;">업로드 가능 파일 확장자: hwp,doc,docx,ppt,pptx,xls,xlsx,txt,csv,jpg,jpeg,gif,png,bmp,pdf</p>
						<div class="file-list">
							<form:input type="file" path="uploadFile" title="첨부파일 추가" multiple="multiple" />
					  
							  <!-- 첨부한 파일명 표시 영역  -->   
							   <div id="fileName">
							   		
							   </div>
						</div>

	           			<!-- 기존에 첨부된 파일이 있으면 표시  -->   
						<!-- 수정일 기준 30일 경과 되면 다운로드 안됨 -->
						<c:if test="${fileSize gt 0}">			
				                <div class="download-area">
		                		  <label for="fileSq" style="margin-bottom: 20px;">
		                		  	첨부파일 다운로드
		                		  	<i class="fa-solid fa-download" style="color: #0070D2;"></i>
	                		  	  </label>
								  
									<c:forEach var="file" items="${fileList}" varStatus="status">
										<input id="fileSq" name="fileSq" value = "${file.fileSq}" style="display:none;" />
			              		  		<div class="files">
						                         <a href="/fileDownload.do?storeNm=${file.storeNm}&uploadNm=${file.uploadNm}">
						                        	<input type="hidden" id="storeNm" value="${file.storeNm}" name="storeNm" />
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
				<button id="listBtn" type="button">
						<spring:message code="button.list" />
				</button>
			
			  
			</div>
			<div class="right-btn">
				<button id="writeBtn" type="button">
				   <!--  등록 -->
				   <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
				   <!--  수정 -->
				   <c:if test="${registerFlag == 'modify'}"><spring:message code="button.modify" /></c:if>
				</button>
				
						 
			   <!-- 취소 -->
			   <a  href="javascript:document.addForm.reset();">
				   <button id="delPrevBtn" type="button">         	
						   <spring:message code="button.reset" />
				   </button>
			   </a>
			   
		   
			   <!-- 삭제 -->
			   <c:if test="${registerFlag == 'modify'}">
					<button id="delBtn" type="button" onclick="fn_delete();">         	
							<spring:message code="button.delete" />           
					</button>
			   </c:if>
			</div>
	   			 
         </div>   
        </footer>
        <div class="ft-r"></div>

	<!-- 검색조건 유지 -->
	<input type="hidden" id="boardCd2" name="boardCd2" value="${boardVO.boardCd}"/>
	<input type="hidden" id="searchCondition" name="searchCondition" value="${searchVO.searchCondition}"/>
	<input type="hidden" id="searchKeyword" name="searchKeyword" value="${searchVO.searchKeyword}"/>
	<input type="hidden" id="pageIndex" name="pageIndex" value="${searchVO.pageIndex}"/> 
</form:form>
<script type ="module" src="/js/register_isExist.js"></script>
<script type="text/javascript">
	function fn_selectList(){
		const btnVal = document.getElementById("writeBtn").innerText;
		let con = confirm(btnVal+"중이던 글이 저장되지 않습니다.\n목록으로 가시겠습니까?");

		if(con == false){
			return;
		}else if(con == true){
			document.addForm.action = "/boardList.do";
			document.addForm.submit();
		}
	}

</script>
<script type = "module" language="javaScript" src="/js/board_register.js?after"></script>
</body>

</html>