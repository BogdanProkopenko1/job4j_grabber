create table post(
    id serial primary key,
    name varchar(256),
    text text,
    link text,
    created date
);