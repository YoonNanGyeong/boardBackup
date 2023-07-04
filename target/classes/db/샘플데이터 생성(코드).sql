--샘플데이터 of code
insert into test_code_tb (code,decode,code_pid) values ('B01','게시판',NULL);
insert into test_code_tb (code,decode,code_pid) values ('B0101','공지사항','B01');
insert into test_code_tb (code,decode,code_pid) values ('B0102','자유게시판','B01');
insert into test_code_tb (code,decode,code_pid) values ('B0103','코딩게시판','B01');
insert into test_code_tb (code,decode,code_pid) values ('F01','첨부파일',NULL);
insert into test_code_tb (code,decode,code_pid) values ('F0101','공지사항','F01');
insert into test_code_tb (code,decode,code_pid) values ('F0102','자유게시판','F01');
insert into test_code_tb (code,decode,code_pid) values ('F0103','코딩게시판','F01');
COMMIT;
