-- 테이블 삭제DROP TABLE test_uploadfile_tb;DROP TABLE test_board_tb;DROP TABLE test_code_tb;

-- 시퀀스 삭제DROP SEQUENCE test_board_seq;DROP SEQUENCE test_uploadfile_seq;

-------D
--코드
-------D
create table test_code_tb(
    code        varchar2(10),       --코드
    decode      varchar2(15),       --코드명
    code_pid    varchar2(5),        --상위코드
    create_dt       date default sysdate,         --생성일시
    update_dt       date default sysdate          --수정일시
);
--기본키
alter table test_code_tb add Constraint test_code_pk primary key (code);

--외래키
alter table test_code_tb add constraint  test_code_fk1
    foreign key(CODE_PID) references test_code_tb(code);

--제약조건
alter table test_code_tb modify decode constraint code_decode_nn not null;

-------
--게시판
-------
create table test_board_tb(
    board_sq    number(10),         --게시글 번호
    board_cd    varchar2(10),       --분류카테고리(공지사항: B0101, 자유게시판: B0102, 코딩게시판: B0103)
    title       varchar2(100),      --제목
    content     clob,               --본문
    view_cnt    number(10) default 0,          --조회수
    user_nm 	varchar2(20),		--작성자
    create_DT   date default sysdate,         --생성일시
    update_dt   date default sysdate          --수정일시
);

--기본키
alter table test_board_tb add Constraint test_board_pk primary key (board_sq);

--외래키
alter table test_board_tb add constraint test_board_fk1
    foreign key(board_cd) references test_code_tb(code);

--제약조건
alter table test_board_tb modify board_cd constraint board_board_cd_nn not null;
alter table test_board_tb modify title constraint board_title_nn not null;
alter table test_board_tb modify content constraint board_content_nn not null;


--시퀀스
create sequence test_board_seq;


---------
--첨부파일
---------
create table test_uploadfile_tb(
    file_sq         number(10),     --파일아이디
    file_cd         varchar2(10),   --분류코드
    board_no        number(10),     --참조번호(게시글번호)
    store_nm  		varchar2(100),   --서버보관파일명
    upload_nm 		varchar2(100),   --업로드파일명(유저가 업로드한파일명)
	file_size       varchar2(45),   --업로드파일크기(단위byte)
	file_type       varchar2(100),   --파일유형(mimetype)
	create_dt       date default sysdate, --등록일시
	update_dt       date default sysdate  --수정일시
);

--기본키
alter table test_uploadfile_tb add constraint test_uploadfile_pk primary key(file_sq);

--외래키
alter table test_uploadfile_tb add constraint test_uploadfile_fk1
    foreign key(file_cd) references test_code_tb(code);

alter table test_uploadfile_tb add constraint test_uploadfile_fk2
    foreign key(board_no) references test_board_tb(board_sq);

--제약조건
alter table test_uploadfile_tb modify file_cd constraint uploadfile_file_cd_nn not null;
alter table test_uploadfile_tb modify board_no constraint uploadfile_board_no_nn not null;
alter table test_uploadfile_tb modify store_nm constraint uploadfile_store_nm_nn not null;
alter table test_uploadfile_tb modify upload_nm constraint uploadfile_upload_nm_nn not null;
alter table test_uploadfile_tb modify file_size constraint uploadfile_file_size_nn not null;
alter table test_uploadfile_tb modify file_type constraint uploadfile_file_type_nn not null;

--시퀀스
create sequence test_uploadfile_seq;

COMMIT;




