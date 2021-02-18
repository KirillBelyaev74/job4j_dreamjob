CREATE TABLE posts(
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   description text,
   created date not null);

create table candidates(
    id serial primary key,
    name varchar(50));

drop table posts;

drop table candidates;