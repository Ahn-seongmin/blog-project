<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp" %>
    	<body>
    		
    		<main class="view-wrap">
    <div class="view-header">
        <h2 class="view-title">${view.title}</h2>
        <p class="view-meta">작성자 ${view.writerNickname} | ${view.regDate} | 조회 ${view.viewCnt}</p>
    </div>
    <div class="view-image">
        <img src="../images/${view.imgPath}" alt="" class="view-thum">
    </div>
    <div class="view-content">
       ${view.content}
    </div>
    
    <div class="view-actions">
        <button type="button" id="likeBtn" class="btn btn-outline like-btn ${liked ? 'liked':''}">
            ❤ 좋아요 <span id="likeCount">${view.likeCnt}</span>
        </button>
        <div class="view-actions-right"> <!-- 오타도 함께 수정했습니다 (rigth -> right) -->
            <a href="${pageContext.request.contextPath}/blog/update?bno=${view.bno}" class="btn btn-outline">수정</a>
            <!-- 2. 띄어쓰기 및 '삭제' 텍스트 추가 완료 -->
            <a href="${pageContext.request.contextPath}/blog/delete?bno=${view.bno}" class="btn btn-outline" onclick="return confirm('삭제 할래요?')">삭제</a>
            <a href="${pageContext.request.contextPath}/blog/list" class="btn btn-primary">목록</a>
        </div>
    </div>
    
    <!-- 댓글 영역 -->
    <div class="reply-section">
        <h3 class="reply-title">댓글 <span id="repLyCount">${fn:length(replyList)}</span></h3>
        <ul class="reply-list" id="replyList">
        <c:forEach var="r" items="${replyList}">
            <li class="reply-item" data-rno="${r.rno}">
                <div class="reply-info">
                    <span class="reply-writer">${r.nickname}</span>
                    <span class="reply-date">${r.reg_date}</span>
                </div>
                <p class="reply-content">${r.content}</p>
                <div class="reply-btns">
                    <button type="button" class="reply-edit-btn">수정</button>
                    <button type="button" class="reply-delete-btn">삭제</button>
                </div>
            </li>
            </c:forEach>
            <li class="reply-item" data-rno="2">
                <div class="reply-info">
                    <span class="reply-writer">김길동</span>
                    <span class="reply-date">2026-09-17</span>
                </div>
                <p class="reply-content">슬픈날, 기쁜날에도 코딩연습. 일본어 연습</p>
                <div class="reply-btns">
                    <button type="button" class="reply-edit-btn">수정</button>
                    <button type="button" class="reply-delete-btn">삭제</button>
                </div>
            </li>
        </ul>
        <div class="reply-form">
            <input type="text" id="replyContent" placeholder="댓글을 입력하세요" class="reply-input">
            <!-- 3. btn-primary 오타 수정 완료 -->
            <button type="button" id="replySubmitBtn" class="btn btn-primary">등록</button>
        </div>
    </div>
</main>
<!-- Modal -->
<div class="modal fade" id="replyModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">댓글 수정</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <input type="hidden" id="modalRno">
        <textarea id="modalContent" class="form-control" row="4"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="modalSaveBtn">Save changes</button>
      </div>
    </div>
  </div>
</div>
    		
    		
    		
    		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    		
    		
    		<script>
    		//자바스크립트
    		/*	document.getElementById("likeBtn").addEventListener("click",function() {
    				//likeBtn id 이름이 붙은 태그를 클릭했을때
    				let likeBtn = this; //좋아요 버튼
    				likeBtn.classList.toggle("liked"); //liked 클래스가 있으면 제거하고, 없으면 추가된다
    				let count = parseInt(document.getElementById("likeCount").textContent);
    				//likeCount라는 내용을 가져와서 parseInt에 저장하라는 뜻
    				if(likeBtn.classList.contains("liked")){
    					document.getElementById("likeCount").textContent = count + 1;
    				}else{
    					document.getElementById("likeCount").textContent = count - 1;
    				}
    				
    			})*/
    			//jquery
    			$("#likeBtn").on("click",function(){
    				$(this).toggleClass("liked");
    				let count = parseInt($("#likeCount").text());
    				if($(this).hasClass("liked")){
    					$("#likeCount").text(count + 1);
    				}else{
    					$("#likeCount").text(count - 1);
    				}
    			})
    			
    			$("#likeBtn").on("click",function(){
    				$.ajax({
    					url:"${pageContext.request.contextPath}/blog/like",
    					type:"post",
    					data:{bno:bno},
    					dataType:'json',
    					success:function(result){
    						if(!result.success){
    							alert(result.message);
    							return;
    						}
    						$("#likeCount").text(result.likeCount);
    						$("#likeBtn").toggleClass("liked", result.liked);
    					},error:function(){
    						alert("통신에러");
    					}
    				})
    			})
    			
    			//댓글 등록
    			$("#replySubmitBtn").on("click", function(){
    				const content = $("#replyContent").val().trim();
    				if(content == ''){
    					alert("댓글 내용을 입력하세요");
    					return;
    				}
    				
    				$.ajax({
    					url:"${pageContext.request.contextPath}/reply/write",
    					type : 'post',
    					data:{bno:${view.bno}, content:content},
    					dataType:'json',
    					success:function(result){
    						if(!result.success){
    							alert(result.message || '댓글등록 실패');
    							return
    						}
    						//`혼따옴표 가 아니라 백틱이다
    	        			const newItem = `
    	        				 <li class="reply-item" data-rno='\${result.rno}'>
    	                        <div class="reply-info">
    	                            <span class="reply-writer">\${result.writeNickname}</span>
    	                            <span class="reply-date">방금전</span>
    	                        </div>
    	                        <p class="reply-content">\${result.content}</p>
    	                        <div class="reply-btns">
    	                            <button type="button" class="reply-edit-btn">수정</button>
    	                            <button type="button" class="reply-delete-btn">삭제</button>
    	                        </div>
    	                    </li>
    	        			`;
    	        			$("#replyList").append(newItem);
    	        			$("#reply-content").val(``);
    					}, error:function(){
    						alert("댓글 등록 중 오류가 발생");
    					}
    				})
    				
    				
        			
        			/*실제 연동 시 비동기식으로 데이터 처리 할 거다
        			$.ajax({
        				
        			})
        			*/
    			}); 
    			
    		// 댓글 삭제
    		$("#replyList").on("click", '.reply-delete-btn', function(){
    			if(confirm('댓글을 삭제?')){
    				$(this).closest('.reply-item').remove();
    				//실제 연동시 rno값을 data-rno에서 꺼내서 비동기식으로 처리
    				const $item = $(this).closest('.reply-item');//클릭한 li 요소
    				const rno = $item.data('rno');//li 태그의 data-rno 속성 값
    				
    				$.ajax({
    					url:"${pageContext.request.contextPath}/reply/delete",
    					type:'post',
    					data:{rno : rno},
    					dataType:'json',//서버에서 보내는 데이터는 반드시 json 형식으로 보내줘
    					success:function(result){
    						if(result.success){
    							$item.remove(); //서버에서 실제로 지워졌으면 화면에서 제거
    						}else{
    							alert(result.message);
    						}
    					},error:function(){
    						alert("통신오류!");
    					}
    				})
    			}
    			
    		})	
    		//댓글 수정
    		const replyModal = new bootstrap.Modal(document.getElementById('replyModal'));

			document.getElementById('replyModal').addEventListener('show.bs.modal', function(){
    		$('#modalContent').focus();
			});

			$("#replyList").on("click", '.reply-edit-btn', function(){
    		const $item=$(this).closest('.reply-item');
    		$('#modalRno').val($item.data('rno'));
    		$('#modalContent').val($item.find('.reply-content').text());
   			 replyModal.show();
			});
			
			$('#modalSaveBtn').on('click', function(){
				const rno = $('#modalRno').val();
				const newContent = $('#modalContent').val().trim();
				if(newContent == ''){
					alert('댓글 내용을 입력하세요');
					return;
				}
				$.ajax({
					url:'${pageContext.request.contextPath}/reply/modify',
					type:'post',
					data:{rno:rno,content:newContent},
					dataType:'json',
					success:function(result){
						if(result.success){
							$('#replyList .reply-item[data-rno="'+rno+'"]')
							.find(".reply-content").text(result.content);
							replyModal.hide();
						}else{
							alert(result.message);
						}
					},error:function(){
						alert("통신실패");
					}
				})
			})
    		</script>
    	<!--footer.jsp -->
    		
    		<%@ include file="/include/footer.jsp" %>
