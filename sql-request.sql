--- DAO authentication ---

drop schema json cascade;
create schema json;
set search_path to json;

create table json.currencies (
    id varchar(255),
    num_code varchar(255),
    char_code varchar(255),
    nominal numeric,
    name varchar(255),
    value numeric,
    previous numeric,
    primary key (char_code));

create table json.users_audit_entries (
    id bigserial primary key,
    query_date timestamp,
    query_string varchar(255));

create table json.entry_query (
    id int8,
    users_audit_entries int8);

create table json.users (
	id bigserial,
	username varchar(255) not null, --30--
	password varchar(255), -- 80--
	email varchar(50) unique,
    enabled boolean,
	primary key (id)
	);

create table json.roles (
	id serial,
	name varchar(50) not null,
	primary key (id)
	);

create table json.users_roles (
	user_id bigint not null,
	role_id int not null,
	primary key (user_id, role_id),
	foreign key (user_id) references users (id),
	foreign key (role_id) references roles (id)
	);

insert into json.roles (name)
	values
	('ROLE_USER'), ('ROLE_ADMIN');

insert into json.users (username, password, email, enabled)
	values
	('user', '$2y$12$EFgNamlZ08x/UXolq6ajreNjOMmDlwRqPWyr4iUUMKGJn/35GVoau', 'user@gmail.com', true);
insert into json.users (username, password, email, enabled)
values
('guest', '$2y$12$LBqO6QQGcAYMTXI8VaCus.Mu3KPW5pDEtBRwrAj79/4RjRtSNuoLC', 'guest@gmail.com', true);

insert into json.users_roles (user_id, role_id) values (1, 2);
insert into json.users_roles (user_id, role_id) values (2, 1);



CREATE TABLE json.course
(
    date timestamp without time zone NOT NULL,
    previous_date timestamp without time zone,
    previousurl character varying(255) COLLATE pg_catalog."default",
    "timestamp" timestamp without time zone,
    CONSTRAINT course_pkey PRIMARY KEY (date)
)

    TABLESPACE pg_default;

ALTER TABLE json.course
    OWNER to postgres;


CREATE TABLE json.rate_valute_mapping
(
    course_date date NOT NULL,
    valute_char_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    valute_key character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT rate_valute_mapping_pkey PRIMARY KEY (course_date, valute_key),
    CONSTRAINT fk9opmqrkt8d0as4ocnyoct4246 FOREIGN KEY (valute_char_code)
        REFERENCES json.currencies (char_code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fki5jh5a873soqkrjuq7s7r21nc FOREIGN KEY (course_date)
        REFERENCES json.course (date) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE json.rate_valute_mapping
    OWNER to postgres;