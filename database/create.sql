create database job4j_dreamjob;

CREATE TABLE posts(
   id SERIAL PRIMARY KEY,
   name TEXT not null,
   description text,
   created date not null);

create table candidates(
    id serial primary key,
    name varchar(50));

create table photoCandidate(
    candidate_id int references candidates(id),
    pathPhoto text not null);

create table city(
    candidate_id int references candidates(id),
    name varchar(20));

create table consumers(
    id serial primary key,
    name varchar(50) not null,
    email varchar(20) unique not null,
    password varchar(20) not null);