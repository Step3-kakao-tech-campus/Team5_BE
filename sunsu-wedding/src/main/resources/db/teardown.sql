SET REFERENTIAL_INTEGRITY FALSE;
truncate table user_tb;
truncate table portfolio_tb;
truncate table imageitem_tb;
truncate table priceitem_tb;
truncate table match_tb;
truncate table quotation_tb;
SET REFERENTIAL_INTEGRITY TRUE;

-- planner 비밀번호 : planner1234!
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`, `is_active`, `dtype`) VALUES ('2','planner@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL', 'true', 'planner');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`, `is_active`, `dtype`) VALUES ('3','planner2@gmail.com','{bcrypt}$2a$10$89SwVjyXVDhK3GFcN4c8Bu3kQlNiWqjaTvgiXaCi9D/1eWx2w7CBa','planner2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'planner');
-- JPA 테스트 할 때 DummyEntity가 생겨서 전체 테스트 시 PRIMARY_KEY_VIOLATION 에러나서 일단 id 2,3으로 해뒀습니다!
-- couple 비밀번호 : couple1234!
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`, `is_active`, `dtype`) VALUES ('4','couple@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','NORMAL', 'true', 'couple');
INSERT INTO user_tb (`id`,`email`,`password`,`username`,`created_at`,`payed_at`,`grade`, `is_active`, `dtype`) VALUES ('5','couple2@gmail.com','{bcrypt}$2a$10$bKgX34po45/xYw1Dd8C81OYW4dkkVQV5lHd7a.06m1gBX689XERA.','couple2','2023-09-16 01:06:55.00','2023-09-20 15:26:55.00','PREMIUM', 'true', 'couple');

INSERT INTO portfolio_tb (`id`, `planner_id`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `created_at`) VALUES ('1', '2', 'test1', 'test1', '부산', 'none', 'none', '1000000', '10', '1000000', '1000000', '1000000', '2023-09-15 15:26:55.00');
INSERT INTO portfolio_tb (`id`, `planner_id`, `title`, `description`, `location`, `career`, `partner_company`, `total_price`, `contract_count`, `avg_price`, `min_price`, `max_price`, `created_at`) VALUES ('2', '3', 'test2', 'test2', '부산', 'none', 'none', '2000000', '20', '2000000', '2000000', '2000000', '2023-09-20 15:26:55.00');

INSERT INTO priceitem_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('1', '1', '스튜디오1', '500000');
INSERT INTO priceitem_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('2', '1', '드레스1', '300000');
INSERT INTO priceitem_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('3', '1', '메이크업1', '200000');
INSERT INTO priceitem_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('4', '2', '스튜디오2', '500000');
INSERT INTO priceitem_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('5', '2', '드레스2', '300000');
INSERT INTO priceitem_tb (`id`, `portfolio_id`, `item_title`, `item_price`) VALUES ('6', '2', '메이크업2', '200000');

INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('1', '1', '1-1.jpg', '/Users/seokjun/Downloads/images/', '522499', 'true');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('2', '1', '1-2.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('3', '1', '1-3.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('4', '1', '1-4.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('5', '1', '1-5.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('6', '1', '2-1.jpg', '/Users/seokjun/Downloads/images/', '522499', 'true');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('7', '1', '2-2.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('8', '1', '2-3.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('9', '1', '2-4.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');
INSERT INTO imageitem_tb (`id`, `portfolio_id`, `origin_file_name`, `file_path`, `file_size`, `thumbnail`) VALUES  ('10', '1', '2-5.jpg', '/Users/seokjun/Downloads/images/', '522499', 'false');

INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `status`, `price`, `confirmed_price`, `confirmed_at`, `created_at`, `is_active`) VALUES ('1', '2', '4', 'CONFIRMED', '1000000', '1000000', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `status`, `price`, `confirmed_price`, `confirmed_at`, `created_at`, `is_active`) VALUES ('2', '2', '4', 'UNCONFIRMED', '1000000', '0', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `status`, `price`, `confirmed_price`, `confirmed_at`, `created_at`, `is_active`) VALUES ('3', '2', '5', 'UNCONFIRMED', '1000000', '0', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO match_tb (`id`, `planner_id`, `couple_id`, `status`, `price`, `confirmed_price`, `confirmed_at`, `created_at`, `is_active`) VALUES ('4', '2', '4', 'UNCONFIRMED', '1000000', '0', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');

INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('1', '1', 'test', '1000000', 'abc', 'asdf', 'CONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('2', '1', 'test2', '1000000', 'abc2', 'asdf2', 'CONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('3', '2', 'test', '1000000', 'abc', 'asdf', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('4', '2', 'test2', '1000000', 'abc2', 'asdf2', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');
INSERT INTO quotation_tb (`id`, `match_id`, `title`, `price`, `company`, `description`, `status`, `modified_at`, `created_at`, `is_active`) VALUES ('5', '3', 'test2', '1000000', 'abc2', 'asdf2', 'UNCONFIRMED', '2023-10-08 08:30:12.00', '2023-10-08 08:30:12.00', 'true');