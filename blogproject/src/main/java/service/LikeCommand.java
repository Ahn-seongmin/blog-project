package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LikeDao;
import model.MemberDto;

public class LikeCommand implements Command {

    @Override
    public boolean doCommand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        MemberDto dto = (MemberDto) session.getAttribute("loginMember");
        
        Map<String, Object> result = new HashMap<String, Object>();

        // 1. 로그인 여부 체크
        if (dto == null) {
            result.put("success", false);
            result.put("message", "로그인 하세요.");
            sendJson(response, result);
            return false;
        }

        // 2. 파라미터 수신 (rno로 통일)
        int rno = Integer.parseInt(request.getParameter("rno"));
        
        // 3. 좋아요 토글 비즈니스 로직 수행
        LikeDao dao = new LikeDao();
        // 비즈니스 로직 결과에 따라 성공 여부를 유동적으로 처리하는 것이 좋습니다.
        boolean isSuccess = dao.toggle(rno, dto.getId()); 

        // 4. 결과 JSON 처리
        if (isSuccess) {
            result.put("success", true);
            result.put("message", "좋아요 상태가 변경되었습니다.");
        } else {
            result.put("success", false);
            result.put("message", "처리 중 오류가 발생했습니다.");
        }
        
        sendJson(response, result);
        return true; 
    }

    // JSON 전송 유틸리티 메서드 (void 타입이므로 내부 return false 제거)
    private void sendJson(HttpServletResponse response, Map<String, Object> result) {
        response.setContentType("application/json; charset=utf-8");
        Gson gson = new Gson();
        String json = gson.toJson(result);
        
        try {
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush(); // 스트림을 확실히 비워줍니다.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
