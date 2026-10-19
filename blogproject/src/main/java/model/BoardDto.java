package model;

public class BoardDto {
	
    private int bno;                        
    private String title;
    private String content; 
    private String writer;
    private String regDate; // ★ reg_date에서 regDate로 변경 (JSP의 ${b.regDate}와 일치)
    private String modDate; 
    private int viewCnt;
    private int likeCnt;
    private String imgPath; // ★ imgPath 하나로 통합 완료
    private String writerNickname;
    
    // Getter, Setter
    public int getBno() { return bno; }
    public void setBno(int bno) { this.bno = bno; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }
    
    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }
    
    public String getModDate() { return modDate; }
    public void setModDate(String modDate) { this.modDate = modDate; }
    
    public int getViewCnt() { return viewCnt; }
    public void setViewCnt(int viewCnt) { this.viewCnt = viewCnt; }
    
    public int getLikeCnt() { return likeCnt; }
    public void setLikeCnt(int likeCnt) { this.likeCnt = likeCnt; }
    
    public String getImgPath() { return imgPath; }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
    
    public String getWriterNickname() { return writerNickname; }
    public void setWriterNickname(String writerNickname) { this.writerNickname = writerNickname; }
}