<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
      <!-- 게시판 코드가 있으면 registerFlag변수에 modify, 없으면 create 저장 -->
    <c:set var="registerFlag" value="${empty boardVO.boardCd ? 'create' : 'modify'}"/>
    <title>게시글
    	 <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
         <c:if test="${registerFlag == 'modify'}"><spring:message code="button.modify" /></c:if>
    </title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/write.css?after'/>"/>
    
    <script type="text/javaScript" language="javascript" defer="defer">
     
        /* 글 목록 화면 function */
        function fn_egov_selectList() {
           	document.detailForm.action = "<c:url value='/boardList.do'/>";
           	document.detailForm.submit();
        }
        
        /* 글 삭제 function */
        function fn_egov_delete() {
        	if(!confirm("삭제하시겠습니까?")){
        		document.detailForm.action = "<c:url value='/detailBoard.do'/>";
        	}else{    		
        		alert("삭제완료되었습니다.");
    	       	document.detailForm.action = "<c:url value='/deleteBoard.do'/>";
    	       	document.detailForm.submit();
        	}
        }
        
        /* 글 등록 function */
        function fn_egov_save() {
	        var errors = document.querySelectorAll(".errors");
	        var btnVal = document.getElementById("writeBtn").innerText;
	        let con = confirm(btnVal + "하시겠습니까?");
	        
        	frm = document.detailForm;
        	if(con == false){
                return;
            }else if(con == true){
            	frm.action = "<c:url value="${registerFlag == 'create' ? '/addBoard.do' : '/updateBoard.do'}"/>";
                frm.submit();
            }
        }
       
    </script>
</head>
<body >

<form:form method ="post" commandName="boardVO" id="detailForm" name="detailForm"  enctype="multipart/form-data" >
 <input type="hidden" name="boardSq" value="${boardVO.boardSq}" />
    <!-- 상단바 -->
        <div class="nv-l"></div>
        <nav class="nv">
	        <a href="javascript:fn_egov_selectList();" title="홈(게시글 목록)">
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
					<c:if test="${registerFlag == 'create'}">게시글 등록</c:if>
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
	                <form:errors path="boardCd" class="errors"/>
	             <label id="userNmLabel" for="userNm">작성자</label>
             	<form:input path="userNm"  id="userNm" placeholder="닉네임 입력" maxlength="6"/>
<%--              	 <form:errors path="userNm"  class="errors"/> --%>
            </div>
            
            <div class="contents">
                <div class="title-area">
                    <label for="title" id="titleLabel">제목</label>
                    <form:input path="title"  placeholder="제목을 입력하세요." maxlength="45"/>
<%--                      <form:errors path="title"  class="errors" style="margin-left: 10px;"/> --%>
                </div>
                <div class="content-area">
                    <label for="content">내용</label>
                    <form:textarea path="content" name="content" id="content" cols="150" rows="20" placeholder="내용을 입력하세요."/>
<%--                     <form:errors path="content"  class="errors" style="margin-left: 10px;"/> --%>
                </div>
            </div>
            
                        
	            <div class="file-area">
	                <label for="uploadFile">첨부파일</label>
	                        
	                	<form:input type="file" path="uploadFile" title="첨부파일 추가"/>
	           
<%-- 	                <c:if test = "${boardVO.fileNm ne null }">  --%>
<!-- 		                <div style= "margin-top: 10px;" id="fileDown"> -->
<!-- 			                <label for="fileNm">다운로드</label>              -->
<%-- 			                        <a href="fileDownload.do?fileNo=${boardVO.fileNo}" title="첨부파일 다운로드"> --%>
<%-- 			                        	<input type="text" id="fileNo" value="${boardVO.fileNo}" name="fileNo readonly="readonly"  title="첨부파일 다운로드"/> --%>
<!-- 			                        </a> -->
<!-- 				                   		<button id="fileDelete" type="button" onclick="fn_fileDelete()">파일삭제</button> -->
<!-- 		                </div> -->
<%--                  	</c:if> --%>
	            </div>
            
            
        </main>
        <div class="mn-r"></div>
        <!-- /글 작성 영역 -->
        
        <div class="ft-l"></div>
        <footer class="ft">
         <div class="btns">
             <a href="javascript:fn_egov_save();" >
         	<button id="writeBtn" type="button">
							<!--  등록 -->
	                     <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
							<!--  수정 -->
	                     <c:if test="${registerFlag == 'modify'}"><spring:message code="button.modify" /></c:if>
         	</button>
             </a>      	
         	
         	     	
	         	 <a  href="javascript:document.detailForm.reset();">
		         	<button id="delPrevBtn" type="button">         	
			   			 <!-- 취소 -->
			   			 <spring:message code="button.reset" />
		         	</button>
	         	</a>
	         	
	         	<a  href="javascript:fn_egov_selectList();">
		         	<button id="listBtn" type="button">
		         			<!-- 목록 -->
			   			  <spring:message code="button.list" />
		         	</button>
	         	</a>
         	
	             <!-- 삭제 -->
	             <c:if test="${registerFlag == 'modify'}">
	              <a href="javascript:fn_egov_delete();">
		         	<button id="delPrevBtn" type="button">         	
			              <spring:message code="button.delete" />           
		         	</button>
		         	</a>      
	   			 </c:if>
	   			 
         </div>   
        </footer>
        <div class="ft-r"></div>
    
    <!-- 검색조건 유지 -->
    <input type="hidden" name="searchCondition" value="<c:out value='${searchVO.searchCondition}'/>"/>
    <input type="hidden" name="searchKeyword" value="<c:out value='${searchVO.searchKeyword}'/>"/>
    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
</form:form>
</body>
<script  type="text/javaScript" language="javascript">
	var fileName = document.getElementById("fileNm");
	var fileDelete = document.getElementById("fileDelete");
	var fileDown = document.getElementById("fileDown");
	
	function fn_fileDelete(){
		fileName.value = null;
		fileDown.remove();
	}
</script>
</html>