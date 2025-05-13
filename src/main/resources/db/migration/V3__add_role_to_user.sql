USE spring_api;

alter table users
    add role varchar(20) default 'USER' not null;
