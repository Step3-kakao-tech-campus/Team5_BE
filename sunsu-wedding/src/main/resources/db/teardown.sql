SET REFERENTIAL_INTEGRITY FALSE;
truncate table planner_tb;
truncate table couple_tb;
--truncate table portfolio_tb;
--truncate table imageitem_tb;
--truncate table priceitem_tb;
--truncate table match_tb;

INSERT INTO planner_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('1','planner@gmail.com','planner1234!','planner','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL');
INSERT INTO planner_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('2','planner2@gmail.com','planner1234!','planner2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM');
INSERT INTO couple_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('1','couple@gmail.com','couple1234!','couple','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL');
INSERT INTO couple_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`) VALUES ('2','couple2@gmail.com','couple1234!','couple2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM');
