--blog 프로젝트
--회원가입
create table member (
    id varchar2 (20) not null,
    pwd varchar2 (200) not null,
    nickname varchar2(30) not null,
    email varchar2(100) not null,
    join_date date default sysdate,
    constraint pk_member primary key (id)
);

ALTER TABLE member RENAME COLUMN nikname TO nickname;
DELETE FROM member WHERE id = 'java02';
--board
create table board(
    bno number not null,                           -- 게시글 번호
    title varchar2(200) not null,                  -- 제목
    content clob not null,                         -- 내용
    writer varchar2(20) not null,                  -- 작성자 (vriter -> writer로 수정)
    reg_date date default sysdate,                 -- 작성일 
    mod_date date,                                 -- 수정일
    view_cnt number default 0,                     -- 조회수
    like_cnt number default 0,                     -- 좋아요 수
    img_path varchar2(300),                        -- 대표이미지
    
    constraint pk_board primary key (bno),
    constraint fk_board_writer foreign key (writer) references member(id) -- 컬럼명 일치 완료
);

--reply
create table reply (
    rno number not null, --댓글 번호
    bno number not null, --어떤 게스글의 댓글인지 (board.bno 참조)
    writer varchar2(20) not null, --댓글 작성자 (member.id 참조)
    content varchar2(500) not null, --댓글
    reg_date date default sysdate,
    CONSTRAINT pk_reply primary key(rno),
    CONSTRAINT fk_reply_board FOREIGN KEY (bno) REFERENCES board(bno) on DELETE CASCADE,
    CONSTRAINT fk_reply_writer FOREIGN KEY (writer) REFERENCES member(id)
);
-- on DELETE CASCADE : 게시글이 삭제되면 그 글에 딸린 댓글도 자동으로 같이 삭제

-- board_like (좋아요 - 중복 방지용 매핑 테이블)

create table board_like (
    bno number not null,
    member_id varchar2(20) not null,
    like_date date default sysdate,
    CONSTRAINT pk_board_like primary key (bno,member_id),
    CONSTRAINT fk_like_board foreign key (bno) references board(bno) on delete CASCADE,
    CONSTRAINT fk_like_member foreign key (member_id) references member(id)
);
--시퀀ㅅ (oracle에서 자동 증가 번호를 만들어 주는 객체)
create sequence seq_board;
create sequence seq_reply;

--샘플데이터 동작 확인용
-- 회원 2명
INSERT INTO MEMBER(ID, PWD, NICKNAME, EMAIL) VALUES('java01', '1234', '홍길동', 'hong@test.com');
INSERT INTO MEMBER(ID, PWD, NICKNAME, EMAIL) VALUES('java02', '1234', '김철수', 'kim@test.com');

-- 게시글 2개 (시퀀스로 번호 자동 생성)
INSERT INTO BOARD(BNO, TITLE, CONTENT, WRITER) VALUES(SEQ_BOARD.NEXTVAL, '첫 번째 글입니다', '안녕하세요, 첫 글이에요.', 'java01');
INSERT INTO BOARD(BNO, TITLE, CONTENT, WRITER) VALUES(SEQ_BOARD.NEXTVAL, '두 번째 글입니다', '내용을 적어봅니다.', 'java02');

-- 댓글 1개
INSERT INTO REPLY(RNO, BNO, WRITER, CONTENT) VALUES(SEQ_REPLY.NEXTVAL, 3, 'java01', '좋은 글이네요!');
select * from board;

-- 좋아요 1개
INSERT INTO BOARD_LIKE(BNO, MEMBER_ID) VALUES(4, 'java02');

COMMIT;

select * from member