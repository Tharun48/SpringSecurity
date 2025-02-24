create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);


INSERT  INTO users VALUES ('user', '{noop}EazyBytes@12345', '1');
INSERT  INTO authorities VALUES ('user', 'read');

INSERT  INTO users VALUES ('admin', '{bcrypt}$2a$12$88.f6upbBvy0okEa7OfHFuorV29qeK.sVbB9VQ6J6dWM1bW6Qef8m', '1');
INSERT  INTO authorities VALUES ('admin', 'admin');



CREATE TABLE customer (
  id serial4 NOT NULL,
  email varchar(45) NOT NULL,
  pwd varchar(200) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO public.customer
(id, email, pwd, "role")
VALUES(1, 'tharunreddyy48@gmail.com', '{noop}Bptppa@1', 'user');


CREATE TABLE public.authorities (
	id serial4 NOT NULL,
	customer_id int4 NULL,
	name varchar(255) NULL,
	CONSTRAINT authorities_pkey PRIMARY KEY (id),
	CONSTRAINT authorities_fkey FOREIGN KEY (customer_id) REFERENCES public.customer(id)
);

INSERT INTO public.authorities
(id, customer_id, "name")
VALUES(1, 1, 'ROLE_VIEW_BALANCE');
INSERT INTO public.authorities
(id, customer_id, "name")
VALUES(2, 1, 'VIEW_ACCOUNT');


CREATE TABLE public.contact (
	id serial4 NOT NULL,
	contact_name varchar(45) NOT NULL,
	subject varchar(80) NOT NULL,
	message varchar(200) NOT NULL,
	CONSTRAINT contact_pkey PRIMARY KEY (id)
);