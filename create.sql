create SEQUENCE seq_topcategory
increment by 1
start with 1;

drop sequence seq_topcategory;

insert into TOPCATEGORY (TOP_CATEGORY_ID, TOP_NAME) 
values(seq_topcategory.nextval, '케잌');
insert into TOPCATEGORY (TOP_CATEGORY_ID, TOP_NAME) 
values(seq_topcategory.nextval, '빵');
insert into TOPCATEGORY (TOP_CATEGORY_ID, TOP_NAME) 
values(seq_topcategory.nextval, '제과');
insert into TOPCATEGORY (TOP_CATEGORY_ID, TOP_NAME) 
values(seq_topcategory.nextval, '샐러드');

delete from TOPCATEGORY ;

create Sequence seq_subcategory
increment by 1
start with 1;

insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 1, '생크림');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 1, '고구마');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 1, '치즈');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 1, '초코');

insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 2, '슈크림');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 2, '피자빵');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 2, '소보로');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 2, '멜론빵');

insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 3, '초코쿠키');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 3, '딸기쿠키');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 3, '바닐라쿠키');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 3, '양갱');

insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 4, '치킨샐러드');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 4, '시저샐러드');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 4, '연어샐러드');
insert into SUBCATEGORY (SUBCATEGORY_ID, TOP_CATEGORY_ID, SUB_NAME) 
values(seq_subcategory.nextval, 4, '리코타치즈');

