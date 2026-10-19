$(function() { // jQuery 시작
    // 이미지 선택시 미리보기
    const defaultImg = $("#imgPreview").attr("src");
    
    $("#imgFile").on("change", function() {
        const file = this.files[0];
        if(!file) return;
        
        // 브라우저 메모리 안에서 임시 URL을 만들어 미리보기에 반영
        const previewUrl = URL.createObjectURL(file);
        
        // 💡 화면을 백지로 만들던 document.write 코드는 삭제했습니다.
        $("#imgPreview").attr("src", previewUrl);
		$("#removeImg").val('false');//제거 눌렀다가 다시 새 파일 선택하면 제거 요청 취소
    })
    
    // 이미지 제거
    $("#imgRemoveBtn").on("click", function() {
        $("#imgFile").val('');
        $("#imgPreview").attr("src", defaultImg);
		$("#removeImg").val('true'); //modify.jsp에만 있는 hidden,write.jsp엔 없다
    })
    
    // 💡 괄호 안에 'e'를 추가하여 e.preventDefault()가 작동하도록 수정했습니다.
    $("#writeForm").on("submit", function(e) {
        const title = $("#title").val().trim();
        const content = $("#content").val().trim();
        
        if(title === '') {
            alert("제목 입력 하세요!");
            e.preventDefault();
            $("#title").focus();
            return;
        }
        
        if(content === '') {
            alert("내용 입력 하세요!");
            e.preventDefault();
            $("#content").focus();
            return;
        }
    })
});/**
 * 
 */