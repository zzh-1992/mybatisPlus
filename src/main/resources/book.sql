create table book
(
    id        int primary key auto_increment comment '主键',
    full_name varchar(20) not null comment '全称',
    parent_id int comment '父级id',
    status    smallint comment '状态:1 正常;-1 失效'
) comment '书-树状结构';

INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (1, 'fruit', null, 1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (3, 'grape', 1, 1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (5, 'banana', 1, 1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (7, 'watermelon', 1, 1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (9, 'grape_1', 3, -1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (11, 'grape_2', 3, 1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (13, 'banana_1', 5, -1);
INSERT INTO grapefruit.book (id, full_name, parent_id, status)
VALUES (15, 'banana_1', 5, -1);