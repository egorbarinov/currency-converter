--- DAO authentication ---

drop schema valcurs cascade;
create schema valcurs;
set search_path to valcurs;

CREATE TABLE valcurs.course
(
    date timestamp without time zone NOT NULL,
    previous_date timestamp without time zone,
    previousurl varchar(255),
    timestamp timestamp without time zone,
    PRIMARY KEY (date)
);


-- Table: json.rate_valute_mapping

-- DROP TABLE json.rate_valute_mapping;

CREATE TABLE valcurs.rate_valute_mapping
(
    course_date timestamp without time zone,
    valute_pk bigserial,
    valute_key varchar(255)
);

create table valcurs.currencies (
    pk bigserial,
    id varchar(255),
    num_code varchar(255),
    char_code varchar(255),
    nominal numeric,
    name varchar(255),
    value numeric,
    previous numeric,
    primary key (pk));



create table valcurs.users (
    id bigserial,
    username varchar(255) not null, --30--
    password varchar(255), -- 80--
    email varchar(50) unique,
    enabled boolean,
    role varchar(255),
    activate_code varchar(80),
    primary key (id)
);


create table valcurs.entry_query (
    id int8,
    users_audit_entries int8);



create table valcurs.users_audit_entries (
    id bigserial,
    query_date timestamp,
    query_string varchar(255),
    primary key (id));



insert into valcurs.users (username, password, email, enabled, role)
values
       ('user', '$2y$12$EFgNamlZ08x/UXolq6ajreNjOMmDlwRqPWyr4iUUMKGJn/35GVoau', 'user@gmail.com', true, 'ADMIN');

insert into valcurs.users (username, password, email, enabled, role)
values
       ('guest', '$2y$12$LBqO6QQGcAYMTXI8VaCus.Mu3KPW5pDEtBRwrAj79/4RjRtSNuoLC', 'guest@gmail.com', true, 'CLIENT');

