package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

public class BoardDao {
	
	public void insert(BoardDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql="insert into board (bno, title, content, writer, img_path)"
					+" values(seq_board.nextval, ?, ?, ?, ?)";
		
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getWriter());
			pstmt.setString(4, dto.getImgPath());
			
			int result = pstmt.executeUpdate(); 
			System.out.println("■ DB INSERT 성공 행 개수: " + result);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	//검색 조건 맞는 where 절 만들기 (selectCount, selectList 둘 다 같은 조건을 써야 해서 분리
	public String buildWhere(String searchType, String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        return "";
	    }

	    if ("title".equals(searchType)) {
	        return " where b.title like ? ";
	    } else if ("content".equals(searchType)) {
	        return " where b.content like ? ";
	    } else if ("writer".equals(searchType)) {
	        return " where m.nickname like ? ";
	    }
	    return "";
	}
	
	public int selectCount(String searchType, String keyword) {
		String where  = buildWhere(searchType, keyword);
		String sql = "select count(*) from board b join member m on"
					 + " b.writer = m.id" + where;
		
		try (Connection conn = DBUtil.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			if(!where.isEmpty()) {
				pstmt.setString(1, "%"+keyword+"%");
			}
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	//게시글 목록 조회(검색 + 페이징)
	//startRow 가져올 시작 순번(1부터)
	//endRow 가져올 끝 번호
	//한페이지 9개일때 2페이지 -> startRow=11, endRow =18
	
	public List<BoardDto> selectList(String searchType, String keyword, int startRow, int endRow){
		String where = buildWhere(searchType, keyword);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from (\n"
				+ "    select rownum as rn, t.*from(\n"
				+ "        select b.bno,b.title,b.writer,m.nickname as writer_nickname, \n"
				+ "            to_char(b.reg_date,'YYYY-MM-DD') as reg_date,b.view_cnt,b.like_cnt,b.img_path\n"
				+ "        from board b\n"
				+ "        join member m\n"
				+ "        on b.writer = m.id\n"
				+ 		   where
				+ "        order by b.bno desc\n"
				+ "        ) t where rownum <= ?\n"
				+ "    ) where rn >= ?\n";
				
		List<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql.toString());
			
			//? 대입
			int idx = 1;
			if(!where.isEmpty()) {
				pstmt.setString(idx++, "%"+keyword+"%");
			}
			pstmt.setInt(idx++ , endRow);
			pstmt.setInt(idx++, startRow);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDto dto = new BoardDto();
				dto.setBno(rs.getInt("bno"));
				dto.setWriter(rs.getString("writer"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setImgPath(rs.getString("img_path"));
				dto.setRegDate(rs.getString("reg_date")); 
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				list.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public BoardDto selectList1(int bno){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select b.bno,b.title,b.content,b.writer,m.nickname \n"
				+ "       as writer_nickname,to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\n"
				+ "       b.view_cnt,b.like_cnt,b.img_path\n"
				+ "from board b\n"
				+ "join member m\n"
				+ "on b.writer = m.id\n"
				+ "where b.bno =?";
		
		BoardDto dto = null;
		
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new BoardDto();
				dto.setBno(rs.getInt("bno"));
				dto.setWriter(rs.getString("writer"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setImgPath(rs.getString("img_path"));
				dto.setContent(rs.getString("content"));
				dto.setRegDate(rs.getString("reg_date")); 
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	
	public void increViewCnt(int bno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update board set view_cnt = view_cnt + 1 where bno=?";
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String delete(int bno, String writer) {
		String sql1 = "select img_path from board where bno=? and writer = ?";
		String sql="delete from board where bno=? and writer = ?";
		String imgPath = null;
		try(Connection conn = DBUtil.getInstance()){
			try(PreparedStatement pstmt = conn.prepareStatement(sql1)){
				pstmt.setInt(1, bno);
				pstmt.setString(2, writer);
				try(ResultSet rs = pstmt.executeQuery()){
					if(rs.next()) {
						imgPath = rs.getString("img_path");
					}else {
						return null;
					}
				}
			}
			try(PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setInt(1, bno);
				pstmt.setString(2, writer);
				pstmt.executeUpdate();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return imgPath;
	}
	
	public void update(BoardDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql="update board set title=?,content=?,img_path=?, mod_date = sysdate"
				+ " where bno = ? and writer = ?";
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2,dto.getContent());
			pstmt.setString(3,dto.getImgPath());
			pstmt.setInt(4,dto.getBno());
			pstmt.setString(5,dto.getWriter());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//인기글
	public List<BoardDto> selectPopularList(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from (\n"
				+ "    select b.bno,b.title,m.nickname as writer_nickname, \n"
				+ "    to_char(b.reg_date,'YYYY-MM-DD') as reg_date, \n"
				+ "    b.view_cnt,b.like_cnt,b.img_path\n"
				+ "    from board b\n"
				+ "    join member m\n"
				+ "    on b.writer = m.id\n"
				+ "    order by b.like_cnt desc, b.view_cnt desc, b.bno desc)\n"
				+ "where rownum <= 6";
		
		List<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = DBUtil.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDto dto = new BoardDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setWriterNickname(rs.getString("writer_nickname"));
				dto.setRegDate(rs.getString("reg_date"));
				dto.setViewCnt(rs.getInt("view_cnt"));
				dto.setLikeCnt(rs.getInt("like_cnt"));
				dto.setImgPath(rs.getString("img_path"));
				list.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//내가 쓴 글
	public List<BoardDto> selectListByWriter(String writer) {
	    List<BoardDto> list = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    String sql = "select bno, title, to_char(reg_date, 'YYYY-MM-DD') as reg_date, "
	               + "view_cnt, like_cnt, img_path "
	               + "from board "
	               + "where writer = ? "
	               + "order by bno desc";

	    try {
	        conn = DBUtil.getInstance();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, writer);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            BoardDto dto = new BoardDto();
	            dto.setBno(rs.getInt("bno"));
	            dto.setTitle(rs.getString("title"));
	            dto.setRegDate(rs.getString("reg_date"));
	            dto.setViewCnt(rs.getInt("view_cnt"));
	            dto.setLikeCnt(rs.getInt("like_cnt"));
	            dto.setImgPath(rs.getString("img_path"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}
	
	//좋아요 한 글
	public List<BoardDto> selectLikedList(String memberId) {
	    List<BoardDto> list = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    String sql = "select b.bno, b.title, m.nickname as writer_nickname, b.like_cnt, b.img_path "
	               + "from board_like l "
	               + "join board b on l.bno = b.bno "
	               + "join member m on b.writer = m.id "
	               + "where l.member_id = ? "
	               + "order by l.like_date desc";

	    try {
	        conn = DBUtil.getInstance();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, memberId);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            BoardDto dto = new BoardDto();
	            dto.setBno(rs.getInt("bno"));
	            dto.setTitle(rs.getString("title"));
	            dto.setWriterNickname(rs.getString("writer_nickname"));
	            dto.setLikeCnt(rs.getInt("like_cnt"));
	            dto.setImgPath(rs.getString("img_path"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}
	
	
}

