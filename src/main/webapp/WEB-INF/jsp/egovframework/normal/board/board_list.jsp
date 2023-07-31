<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"       uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- Cache Control 설정 -->
	<meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="expires" content="0" />
    <title>게시판 목록</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/list.css?after'/>"/>
      <!-- fontawesome 아이콘 -->
    <script src="https://kit.fontawesome.com/dcfc9e7feb.js" ></script>
    <script type="text/javaScript" language="javascript" defer="defer">
        /* 상세조회 화면 function */
        function fn_select(id) {
			const url = id + "/detailBoard.do";
			document.listForm.action = url;
			document.listForm.method = 'get';
           	document.listForm.submit();
        }  

		/* 글 목록 화면 function */
		function fn_selectList() {
			document.listForm.pageIndex.value = 1;
			document.listForm.action = "<c:url value='/boardList.do'/>";
		   	document.listForm.submit();
		}
        
        /* 글 등록 화면 function */
        function fn_addView() {
			document.listForm.action = "<c:url value='/addBoardView.do'/>";
			document.listForm.method = 'get';
           	document.listForm.submit();
        } 
        
        /* pagination 페이지 링크 function */
         function fn_link_page(pageNo){
        	document.listForm.pageIndex.value = pageNo;
        	document.listForm.action = "<c:url value='/boardList.do'/>";
           	document.listForm.submit();
        } 
         
        
    </script>
</head>

<body>
<form:form modelAttribute="searchVO" id="listForm" name="listForm" method="post">
          <!-- 상단바 -->
        <div class="nv-l"></div>
        <nav class="nv" > 
       		<h4> TEST BOARD</h4>
        </nav>
        <div class="nv-r" onclick="javascript:fn_link_page(1); return false;"></div>
        
        <div class="hd-l"></div>
        <header class="hd">
          <!-- list title -->
          <div id="title">
            <div id="titleIndex">
                <!-- 글자 강조 박스 -->
            </div>
            <h3>
            	
				<c:set var = "key" value = "${searchVO.boardCd}"/>
         		<c:out value = "${category[key]}" default = "공지사항"/>
           	</h3>
          </div>
           <!-- /title -->
    		
	           <!-- 글 검색 -->
	           
	              <ul class="search">
	              <li>
	                 <label for="boardCd">카테고리</label>
        				<form:select path="boardCd"  id="boardCd">
        					<form:option value="B0101" label="공지사항" /> 
        					<form:option value="B0102" label="자유게시판" />
        					<form:option value="B0103" label="코딩게시판" />
        				</form:select>	
	                </li>
	                <li>
	                 <label for="searchCondition">검색 구분</label>
        				<form:select path="searchCondition"  id="searchCondition">
        					<form:option value="" label="선택" />
        					<form:option value="0" label="제목" /> 
        					<form:option value="1" label="내용" />
        					<form:option value="2" label="작성자" />
        				</form:select>	
	                </li>
	                <li>
	                	<label for="searchKeyword" style = "font-size: 0;">검색창</label>
	                     <form:input path="searchKeyword" placeholder="검색" maxlength='20' alt="검색어 입력창"/>
	                    <!-- 검색 아이콘 -->
	                     <a href="javascript:fn_selectList();" title="검색버튼">
	                     	<i class="fa-solid fa-magnifying-glass">
	                   			<p style = "font-size : 0;">검색 버튼</p>
                     		</i>
                   		</a>
	                </li>
	              </ul>
	           
           <!-- /글 검색 -->
    		
        </header>
        <div class="hd-r"></div>
        
        <div class="mn-l"></div>
        <!-- list -->
        <main class="mn">
            <table id="listTable">
                <caption style="visibility: hidden;">글 번호, 카테고리명, 제목, 조회수, 작성자, 작성일 표시하는 테이블</caption>
                <colgroup>
                    <col width="180">
                    <col width="180">
                    <col width="896">
                    <col width="180">
                    <col width="180">
                    <col width="180">
                </colgroup>
                <tr id="caption">
                    <th>글 번호</th>
                    <th>카테고리</th>
                    <th>제목</th>
                    <th>조회수</th>
                    <th>작성자</th>
                    <th>작성일</th>
                </tr>
                <c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td scope = "col"><c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
            				<td scope = "col" id= "category">
	            				<c:set var = "key" value = "${result.boardCd}"/>
	            				<c:out value = "${category[key]}"/>
            				</td>
            				<td scope = "col"><a href="javascript:fn_select('${result.boardSq}')" title="제목링크로 게시글 상세보기"><c:out value="${result.title}"/></a></td>
            				<td scope = "col"><c:out value="${result.viewCnt}"/>&nbsp;</td>
            				<td scope = "col"><c:out value="${result.userNm}"/>&nbsp;</td>
            				<td scope = "col"><c:out value="${result.updateDt}"/>&nbsp;</td>
            			</tr>
       			</c:forEach>
                
            </table>
        </main>
        <!-- /list -->
        <div class="mn-r"></div>
	
       	   <div class="ft-l"></div>
        	<footer class="ft">
		       	<div class="pagingArea">
		               <div id="paging">
		                   <ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fn_link_page" />
						</div>
						<div id="sysbtn">
							<a href="javascript:fn_addView();" id="writeBtn">
								글 작성
							</a>
						</div>
					</div>	
				</footer>
				<div class="ft-r"></div>
		
				<form:hidden path="pageIndex" />
				
    </form:form>
</body>
<script>
	const $keyWord = document.getElementById("searchKeyword");
	$keyWord.addEventListener('keydown', e => {
		if(e.key == 'Enter'){
			document.listForm.pageIndex.value = 1;
			document.listForm.action = "<c:url value='/boardList.do'/>";
           	document.listForm.submit();
		}
	});
</script>
</html>
