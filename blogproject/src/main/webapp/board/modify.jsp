<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp" %>

<main class="write-wrap">
    <h2 class="write-page-title">글 수정</h2>
    
    <form name="my" id="modifyForm" class="write-form" 
          method="post" 
          enctype="multipart/form-data"
          action="${pageContext.request.contextPath}/blog/update">
          <input type="hidden" name="bno" value="${board.bno}">
          <div class="form-row">
          	<label>작성자</label>
          	<p class="write-author">${board.writerNickname}</p>
          </div>
        
        <div class="form-row">
            <label for="title">제목</label>
            <input type="text" id="title" name="title" class="form-input" value="${board.title}">
        </div>
        
        <div class="form-row">
            <label>대표이미지 <span class="form-option">(선택)</span></label>
            <div class="image-upload-box">
    <c:choose>
        <c:when test="${not empty board.imgPath}">
            <!-- 업로드된 이미지가 있으면 그 이미지를 미리보기로 보임 -->
            <img id="imgPreview" src="${pageContext.request.contextPath}/upload/${board.imgPath}" alt="이미지 미리보기" class="img-preview">
        </c:when>
        <c:otherwise>
            <!-- 이미지가 없으면 기본 이미지 제공 -->
            <img id="imgPreview" src="${pageContext.request.contextPath}/images/1.jpeg" alt="이미지 미리보기" class="img-preview">
        </c:otherwise>
    </c:choose>
    <div class="image-upload-btns">
            </div>
        </div>
        <div class="form-row">
        	<label for="content">내용</label>
        	<textarea rows="12" id="content" name="content" placeholder="내용을 입력하시오" class="form-textarea">${board.content}</textarea>
        </div>
        <div class="write-btns">
        	<a href="${pageContext.request.contextPath}/blog/view?bno=${board.bno}" class="btn btn-outline">취소</a>
        	<button type="submit" class="btn btn-primary">수정완료</button>
        </div>
    </form> 
</main>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/write.js"></script>

<%@ include file="/include/footer.jsp" %>

    
    