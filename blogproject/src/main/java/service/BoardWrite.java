package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.BoardDao;
import model.BoardDto;
import model.MemberDto;

public class BoardWrite implements Command {
	
	private static final String UPLOAD_DIR = "/Users/anseongmin/Desktop/upload/";

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		//로그인한 사람만 글쓰기 가능
		HttpSession session = request.getSession();
		MemberDto memberdto = (MemberDto)session.getAttribute("loginMember");
		if(memberdto == null) {
			response.sendRedirect(request.getContextPath()+"/member/login");
			return false;
		}
		
		//get 요청시 글쓰기 화면 보여주기
		if("get".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		
		BoardDto dto = new BoardDto();
		dto.setTitle(request.getParameter("title"));
		dto.setContent(request.getParameter("content"));
		dto.setWriter(memberdto.getId());
		
		
		//이미지 요청 - 첨부했을 때만 저장
		Part filePart = request.getPart("imgFile");
		//jsp/sevlet에서 파일 업로드를 처리할 때 사용하는 servlet 표준 API
		//Part는 업로드된 파일 하나를 표현하는 객체
		//request.getPart("imgFile") http요청으로 전송된 데이터 중에서 name="imgFile"인 파일을 가져와라 
		if(filePart != null && filePart.getSize() > 0) {
			String saveFileName = saveFile(filePart);
			dto.setImgPath(saveFileName);
		}
		//BoardDao dao = new BoardDao();
		 //dao.insert(dto);
		
		//response.sendRedirect(request.getContextPath()+"/blog/list");
		//return false;
	//}//doCommand
	BoardDao dao = new BoardDao();
	dao.insert(dto);
	
	// 💡여기에 이 두 줄을 붙여넣으세요! (기존 return false;는 지우기)
	response.sendRedirect(request.getContextPath() + "/blog/list");
	return false; 
	
} 
	public String saveFile(Part filePart) throws IOException{
		//원본파일 구하기
		//path : 파일이나 디렉토리의 경로 정보를 나타내는 객체
		//files : 파일 생성, 삭제, 읽기, 쓰기,복사등 실제 동작을 처리하는 정적 메서드 집합
		String oriFileName = filePart.getSubmittedFileName();
		//업로드한 원래 파일명
		System.out.println("original file : "+oriFileName);
		//String saveFileName = System.currentTimeMillis()+"_"+oriFileName;
		//currentTimeMillis() 현지시간을 밀리 초단위로 반환하는 메서드
		String saveFileName = UUID.randomUUID()+"_"+oriFileName;
		System.out.println("UUID:"+saveFileName);
		
		Path uploadDir = Paths.get(UPLOAD_DIR);
		//문자열로 되어 있는 경로를 Path 객체로 바꿔라
		//path : 경로를 표현하는 객체
		//paths : path객체를 만들어 주는 클래스
		if(!Files.exists(uploadDir)) {
			//uploadDir 즉 F:/upload 폴더가 존재하지 않으면
			Files.createDirectories(uploadDir);
			//상위 폴더부터 하위 폴더까지 자동으로 만들어 준다
		}
		Path targetPath = uploadDir.resolve(saveFileName);
		//resolve() : 폴더 경로 + 파일 이름을 합쳐서 완전한 파일 경로를 만들어라
		try(InputStream input = filePart.getInputStream()){
			//InputStream 은 데이터를 읽어오는 통로
			Files.copy(input, targetPath);
			//input에서 파일 데이터를 읽어서 targetPath에 복사
		}
		return saveFileName;
	}

}
