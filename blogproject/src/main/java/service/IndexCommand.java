package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDao;
import model.BoardDto;

public class IndexCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		List<BoardDto> list = new BoardDao().selectPopularList();
		
		request.setAttribute("poplist", list);
		
		
		return true;
	}

}
