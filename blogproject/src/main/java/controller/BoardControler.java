package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BoardList;
import service.BoardModify;
import service.BoardView;
import service.BoardWrite;
import service.BoardDelete;
import service.Command;
import service.IndexCommand;
import service.LikeCommand;
import service.PopularCommand;

@WebServlet("/blog/*")
@MultipartConfig(
		maxFileSize = 10 * 1024 * 1024,
		maxRequestSize = 20 * 1024 * 1024
		)
public class BoardControler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardControler() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String pathInfo = request.getPathInfo();
	    String page = null;
	    Command command = null;
	    
	    if (pathInfo == null) pathInfo = "/list";
	    
	    switch(pathInfo){
	    case "/list": 
	        command = new BoardList();
	        page = "/board/list.jsp";
	        break; // ★ 누락되었던 break 추가 완료!
	    
	    case "/write":
	        command = new BoardWrite();
	        page = "/board/write.jsp";
	        break;
	        
	    case "/insert": 
	        page = null; 
	        break;
	    case"/view":
	    	command = new BoardView();
	    	page="/board/view.jsp";
	        break;
	    case"/delete":
	    	command = new BoardDelete();
	    	page=null;
	    	break;
	    case"/update":
	    	command = new BoardModify();
	    	page = "/board/modify.jsp";
	    	break;
	    case"/like":
	    	command = new LikeCommand();
	    	page = null;
	    	break;
	    case"/popular":
	    	command = new PopularCommand();
	    	page = "/board/popular.jsp";
	    	break;
	    case"/index":
	    	command = new IndexCommand();
	    	page = "/index.jsp";
	    	break;
	    default: 
	        System.out.println("■ 없는 요청 주소입니다: " + pathInfo);
	        response.sendRedirect(request.getContextPath() + "/blog/list");
	        return; 
	    }
	    
	    boolean needForward = false;
	    if (command != null) {
	        needForward = command.doCommand(request, response);
	    }
	    
	    if(needForward && page != null) {
	        request.getRequestDispatcher(page).forward(request, response);
	    }
	   
	}
}
