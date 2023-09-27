SET REFERENTIAL_INTEGRITY FALSE;
truncate table planner_tb;
truncate table couple_tb;
--truncate table portfolio_tb;
--truncate table imageitem_tb;
--truncate table priceitem_tb;
--truncate table match_tb;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO planner_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('1','planner@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL');
INSERT INTO planner_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('2','planner2@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM');
-- JPA 테스트 할 때 DummyEntity가 생겨서 전체 테스트 시 PRIMARY_KEY_VIOLATION 에러나서 일단 id 2,3으로 해뒀습니다!
INSERT INTO couple_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('2','couple@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL');
INSERT INTO couple_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('3','couple2@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM');
