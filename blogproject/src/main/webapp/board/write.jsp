<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/include/header.jsp" %>

<main class="write-wrap">
    <h2 class="write-page-title">글쓰기</h2>
    
    <form name="my" id="writeForm" class="write-form" 
          method="post" 
          enctype="multipart/form-data"
          action="${pageContext.request.contextPath}/blog/write">
          
        <%-- 💡 [수정] 따옴표 오타를 해결하여 로그인한 유저의 실제 고유 ID가 서버로 안전하게 넘어가도록 정비했습니다. --%>
        <input type="hidden" name="writer" value="${sessionScope.loginMember.id}">
        
        <div class="form-row">
          	<label>작성자</label>
          	<p class="write-author"> <%-- 오타 수정: outhor -> author --%>
          	    <%-- 💡 [수정] name="writer" 중복을 방지하기 위해 화면 표시용 nickname input의 name 속성은 과감히 제거했습니다. (어차피 readonly라 서버로 보낼 필요가 없습니다) --%>
          	    <input type="text" id="writer" class="form-input" value="${sessionScope.loginMember.nickname}" readonly>
          	</p>
        </div>
        
        <div class="form-row">
            <label for="title">제목</label>
            <input type="text" id="title" name="title" placeholder="제목을 입력하세요" class="form-input">
        </div>
        
        <div class="form-row">
            <label>대표이미지 <span class="form-option">(선택)</span></label>
            <div class="image-upload-box">
                <img id="imgPreview" src="${pageContext.request.contextPath}/images/1.jpeg" alt="이미지 미리보기" class="img-preview">
                <div class="image-upload-btns">
                    <label for="imgFile" class="btn btn-outline file-label">이미지 선택</label>
                    <input type="file" id="imgFile" name="imgFile" accept="image/*" class="file-input">
                    <button type="button" id="imgRemoveBtn" class="btn btn-outline">이미지 제거</button>
                </div>
            </div>
        </div>
        
        <div class="form-row">
        	<label for="content">내용</label>
        	<textarea rows="12" id="content" name="content" placeholder="내용을 입력하시오" class="form-textarea"></textarea>
        </div>
        
        <div class="write-btns">
            <%-- 💡 [수정] 취소 버튼을 클릭했을 때 에러가 나지 않고 정상 목록 서블릿을 타도록 주소를 /blog/list로 정정했습니다. --%>
        	<a href="${pageContext.request.contextPath}/blog/list" class="btn btn-outline">취소</a>
        	<button type="submit" class="btn btn-primary">등록</button>
        </div>
    </form> 
</main>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/write.js"></script>

<%@ include file="/include/footer.jsp" %>
    
    