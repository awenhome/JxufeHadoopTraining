-- drop table if exists movies;
-- 创建外部表movies
create external table movies(id int,title string,genres array<String>)
row format delimited
fields terminated by '::'
collection items terminated by '|'
lines terminated by '\n'
stored as textfile
location '/inputdata/ml-1m/movies';
/*
-- 对比创建内部表（由于Hive与MR需要公用数据，不推荐使用内部表）
drop table if exists movies_in;
create table movies_in(id int,title string,genres array<String>)
row format delimited
fields terminated by ':'
collection items terminated by '|'
lines terminated by '\n'
stored as textfile;
-- 上传数据到内部表
LOAD DATA LOCAL INPATH 'localdata\txt\ml-1m\movies\movies.dat' OVERWRITE INTO TABLE movies_in;*/

-- drop table if exists ratings;
-- 同理：创建外部表ratings
create external table ratings(userid int,movieid int,rating double,timebymills bigint)
row format delimited
fields terminated by ':'
lines terminated by '\n'
stored as textfile
location '/inputdata/ml-1m/ratings';

-- drop table if exists users;
-- 同理：创建外部表users
create external table users(id int,gender char(1),age int ,occupation int,zipcode char(6))
row format delimited
fields terminated by ':'
lines terminated by '\n'
stored as textfile
location '/inputdata/ml-1m/users';

-- 测试数据解析正确
select * from movies limit 2;
select * from ratings limit 2;
select * from users limit 2;

