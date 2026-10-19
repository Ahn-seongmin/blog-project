package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BoardDao;
import model.BoardDto;
import model.MemberDto;
import model.ReplyDao;
import model.ReplyDto;

public class MypageCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		//세션객체 생성
		//loginMember 속성을 MemberDto객체로 형변환
		//loginMember가 null하고 같으면 로그인 페이지로 이동
		//loginMember에 있는 id를 변수에 저장하고
		//id를 replydao, boarddao객체를 생성하고 메서드를 호출하면서 id를 보낸다
		//메서드를 처리하고 리턴된 가변밸열이 값 3개를 속성에 담는다
		HttpSession session = request.getSession(false);
		MemberDto member = (session == null) ? null : (MemberDto) session.getAttribute("loginMember");

		if (member == null) {
		    response.sendRedirect(request.getContextPath() + "/member/login");
		    return false;   // redirect 했으니 forward 하지 않게 false
		}

		String memberId = member.getId();

		// 2. 내 글 / 내 댓글 / 좋아요한 글 조회
		BoardDao boardDao = new BoardDao();
		List<BoardDto> myList = boardDao.selectListByWriter(memberId);
		List<BoardDto> likedList = boardDao.selectLikedList(memberId);
		List<ReplyDto> myReplyList = new ReplyDao().selectListByWriter(memberId);

		// 3. JSP에서 쓸 이름으로 담기
		request.setAttribute("myList", myList);
		request.setAttribute("myReplyList", myReplyList);
		request.setAttribute("likedList", likedList);

		return true;   // mypage.jsp로 forward
	}
}
