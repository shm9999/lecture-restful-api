insert into users(id, join_date, name, password, ssn) values(90001, now(), 'User1', 'test111', '711010-1111111');
insert into users(id, join_date, name, password, ssn) values(90002, now(), 'User2', 'test222', '721114-1111111');
insert into users(id, join_date, name, password, ssn) values(90003, now(), 'User3', 'test333', '731215-1111111');

insert into post(description, user_id) values('My first post', 90001);
insert into post(description, user_id) values('My second post', 90001);