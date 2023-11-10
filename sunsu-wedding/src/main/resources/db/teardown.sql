SET REFERENTIAL_INTEGRITY FALSE;
truncate table user_tb;
truncate table token_tb;
truncate table email_code_tb;
truncate table payment_tb;
truncate table portfolio_tb;
truncate table portfolio_image_item_tb;
truncate table price_item_tb;
truncate table match_tb;
truncate table quotation_tb;
truncate table chat_tb;
truncate table review_tb;
truncate table review_imageitem_tb;
truncate table favorite_tb;
SET REFERENTIAL_INTEGRITY TRUE;

-- planner 비밀번호 : planner1234!
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('1','planner0@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner0','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('2','planner1@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('3','planner2@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('6','planner5@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner5','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('7','planner6@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner6','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('8','planner7@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner7','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('9','planner8@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner8','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('10','planner9@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner9','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('11','planner10@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner10','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('12','planner11@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner11','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('13','planner12@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner12','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('14','planner13@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner13','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('15','planner14@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner14','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('16','planner15@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner15','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('17','planner16@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner16','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('19','planner17@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner17','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'false', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('20','planner18@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner18','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('21','planner19@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner19','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');

-- couple 비밀번호 : couple1234!
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('4','couple@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL', 'true', 'couple');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('5','couple2@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'couple');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('18','couple3@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple3','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'couple');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`upgrade_at`,`grade`, `is_active`, `dtype`) VALUES ('22','couple4@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple4','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'couple');

-- token
INSERT INTO token_tb (`id`,`user_id`,`access_token`,`refresh_token`) VALUES ('1', '3','accesToken1', 'refreshToken1');
INSERT INTO token_tb (`id`,`user_id`,`access_token`,`refresh_token`) VALUES ('2', '4','accesToken2', 'refreshToken2');

-- mail code
INSERT INTO email_code_tb (`id`, `email`, `code`, `confirmed`, `created_at`, `is_active`) VALUES ('1', 'ssarmango@nate.com', '123456', 'true', '2023-11-06 09:00:00.00', 'true');
INSERT INTO email_code_tb (`id`, `email`, `code`, `confirmed`, `created_at`, `is_active`) VALUES ('2', 'asdf@naver.com', '123456', 'true', '2023-11-06 10:00:00.00', 'true');
INSERT INTO email_code_tb (`id`, `email`, `code`, `confirmed`, `created_at`, `is_active`) VALUES ('3', 'test@naver.com', '123456', 'false', '2023-11-06 13:00:00.00', 'true');
INSERT INTO email_code_tb (`id`, `email`, `code`, `confirmed`, `created_at`, `is_active`) VALUES ('4', 'couple@gmail.com', '123456', 'true', '2023-11-06 13:00:00.00', 'true');

-- payment
INSERT INTO payment_tb (`id`,`user_id`,`order_id`,`payment_key`, `payed_amount`, `created_at`, `payed_at`, `is_active`) VALUES ('1', '4','order', 'payment', '1000', '2023-10-16 01:06:55.00', '2023-10-16 01:06:55.10', 'true');
INSERT INTO payment_tb (`id`,`user_id`,`order_id`,`payment_key`, `payed_amount`, `created_at`, `payed_at`, `is_active`) VALUES ('2', '10','order2', 'payment2', '1000', '2023-10-16 01:06:55.00', '2023-10-16 01:06:55.10', 'true');


INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('1', '2', 'planner1', 'test1', 'test1', '부산', 'none', 'none', '1000000', '10', '1000000', '1000000', '1000000', 5, '2023-09-15 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('2', '3', 'planner2', 'test2', 'test2', '부산', 'none', 'none', '2000000', '20', '2000000', '2000000', '2000000', 5, '2023-09-22 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('3', '6', 'planner3', 'test3', 'test3', '부산', 'none', 'none', '2000000', '30', '2000000', '2000000', '2000000', 5, '2023-09-23 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('4', '7', 'planner4', 'test4', 'test4', '부산', 'none', 'none', '2000000', '40', '2000000', '2000000', '2000000', 5, '2023-09-24 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('5', '8', 'planner5', 'test5', 'test5', '부산', 'none', 'none', '2000000', '50', '2000000', '2000000', '2000000', 5, '2023-09-25 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('6', '9', 'planner6', 'test6', 'test6', '부산', 'none', 'none', '2000000', '60', '2000000', '2000000', '2000000', 5, '2023-09-26 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('7', '10','planner7', 'test7', 'test7', '부산', 'none', 'none', '2000000', '70', '2000000', '2000000', '2000000', 5, '2023-09-27 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('8', '11','planner8', 'test8', 'test8', '부산', 'none', 'none', '2000000', '80', '2000000', '2000000', '2000000', 5, '2023-09-28 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('9', '12','planner9', 'test9', 'test9', '부산', 'none', 'none', '2000000', '90', '2000000', '2000000', '2000000', 5, '2023-09-29 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('10', '13','planner10', 'test10', 'test10', '부산', 'none', 'none', '2000000', '100', '2000000', '2000000', '2000000', 5, '2023-09-30 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('11', '14','planner11', 'test11', 'test11', '부산', 'none', 'none', '2000000', '110', '2000000', '2000000', '2000000', 5, '2023-10-01 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('12', '15','planner12', 'test12', 'test12', '부산', 'none', 'none', '2000000', '120', '2000000', '2000000', '2000000', 5, '2023-10-02 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('13', '16','planner13', 'test13', 'test13', '부산', 'none', 'none', '2000000', '130', '2000000', '2000000', '2000000', 5, '2023-10-03 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('14', '17','planner14', 'test14', 'test14', '부산', 'none', 'none', '2000000', '140', '2000000', '2000000', '2000000', 5, '2023-10-04 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('15', '19','planner15', '탈퇴한 플래너의 포트폴리오', 'test14', '부산', 'none', 'none', '2000000', '140', '2000000', '2000000', '2000000', 5, '2023-10-04 15:26:55.00', 'true');
INSERT INTO portfolio_tb (`id`, `planner_id`, `planner_name`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `avg_stars`, `created_at`, `is_active`) VALUES ('16', '20','planner16', '포트폴리오 삭제 테스트 데이터', 'test14', '부산', 'none', 'none', '2000000', '140', '2000000', '2000000', '2000000', 5, '2023-10-04 15:26:55.00', 'true');


INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('1', '1', '스튜디오1', '500000');
INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('2', '1', '드레스1', '300000');
INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('3', '1', '메이크업1', '200000');
INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('4', '2', '스튜디오2', '500000');
INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('5', '2', '드레스2', '300000');
INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('6', '2', '메이크업2', '200000');
INSERT INTO price_item_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('7', '2', '메이크업2', '200000');

INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('1', '1', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('2', '1', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('3', '1', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('4', '1', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('5', '1', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('6', '2', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('7', '2', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('8', '2', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('9', '2', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('10', '2', '/wAA', 'false');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('11', '3', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('12', '4', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('14', '5', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('15', '6', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('16', '7', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('17', '8', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('18', '9', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('19', '10', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('20', '11', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('21', '12', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('22', '13', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('23', '14', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('24', '15', '/wAA', 'true');
INSERT INTO portfolio_image_item_tb (`id`, `portfolio_id`, `image`, `thumbnail`) VALUES  ('25', '16', '/wAA', 'true');

INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (1, '2023-10-08 08:30:12.00', true);
INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (2, '2023-10-08 08:30:12.00', true);
INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (3, '2023-10-08 08:30:12.00', true);
INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (4, '2023-10-08 08:30:12.00', true);
INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (5, '2023-10-08 08:30:12.00', true);
INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (6, '2023-10-08 08:30:12.00', true);
INSERT INTO chat_tb (`id`, `created_at`, `is_active`) VALUES (7, '2023-10-08 08:30:12.00', true);

INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('1', '2', '4', '1', 'CONFIRMED', '1000000', '1000000', 'WRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('2', '2', '4', '2', 'UNCONFIRMED', '1000000', '0', 'UNWRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('3', '2', '5', '3', 'UNCONFIRMED', '1000000', '0', 'UNWRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('4', '2', '4', '4', 'UNCONFIRMED', '1000000', '0', 'UNWRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('5', '3', '4', '5', 'UNCONFIRMED', '1000000', '0', 'UNWRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('6', '2', '4', '6', 'CONFIRMED', '1000000', '1000000', 'UNWRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `chat_id`, `status`, `price`, `confirmed_price`, `review_status`, `confirmed_at`, `created_at`, `is_active`) VALUES ('7', '2', '22', '7', 'UNCONFIRMED', '1000000', '1000000', 'UNWRITTEN', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');


INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('1', '6', 'test', '1000000', 'abc', 'asdf', 'CONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('2', '6', 'test2', '1000000', 'abc2', 'asdf2', 'CONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('3', '2', 'test', '1000000', 'abc', 'asdf', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('4', '2', 'test2', '1000000', 'abc2', 'asdf2', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('5', '3', 'test2', '1000000', 'abc2', 'asdf2', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('6', '5', 'test2', '1000000', 'abc2', 'asdf2', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('7', '7', 'test2', '1000000', 'abc2', 'asdf2', 'CONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('8', '7', 'test2', '1000000', 'abc2', 'asdf2', 'CONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');

INSERT INTO review_tb (`id`, `match_id`, `stars`, `content`, `created_at`, `modified_at`, `is_active`) VALUES (1, 1, 5, '최고의 플래너!', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', true);

INSERT INTO review_imageitem_tb (`id`, `review_id`, `image`, `thumbnail`) VALUES  ('1', '1', '/wAA', 'true');
INSERT INTO review_imageitem_tb (`id`, `review_id`, `image`, `thumbnail`) VALUES  ('2', '1', '/wAA', 'false');
INSERT INTO review_imageitem_tb (`id`, `review_id`, `image`, `thumbnail`) VALUES  ('3', '1', '/wAA', 'false');
INSERT INTO review_imageitem_tb (`id`, `review_id`, `image`, `thumbnail`) VALUES  ('4', '1', '/wAA', 'false');
INSERT INTO review_imageitem_tb (`id`, `review_id`, `image`, `thumbnail`) VALUES  ('5', '1', '/wAA', 'false');

INSERT INTO favorite_tb (`id`, `user_id`, `portfolio_id`,`created_at`) VALUES(1, 1, 1, '2023-10-08 08:30:12.00');
INSERT INTO favorite_tb (`id`, `user_id`, `portfolio_id`,`created_at`) VALUES(2, 1, 2, '2023-10-08 08:30:13.00');
INSERT INTO favorite_tb (`id`, `user_id`, `portfolio_id`,`created_at`) VALUES(3, 2, 1, '2023-10-08 08:30:14.00');
INSERT INTO favorite_tb (`id`, `user_id`, `portfolio_id`,`created_at`) VALUES(4, 4, 1, '2023-10-08 08:30:15.00');