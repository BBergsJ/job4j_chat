create table if not exists persons (
    id serial primary key,
    name varchar(200) unique,
    password varchar(200)
);

create table if not exists messages (
    id serial primary key,
    text varchar(2000),
    created timestamp without time zone not null default now()
);

create table if not exists roles (
    id serial primary key,
    name varchar(200)
);

create table if not exists rooms (
    id serial primary key
);