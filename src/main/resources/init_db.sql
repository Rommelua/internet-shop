CREATE DATABASE bmw_shop
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE TABLE public.products
(
    id bigserial NOT NULL,
    name character varying(255) NOT NULL,
    price double precision,
    deleted boolean NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);

INSERT INTO products(name, price)
VALUES ('BMW 320 I', '50000'),
       ('BMW 530 D', '75000'),
       ('BMW X5M50D', '110000');

CREATE TABLE public.users
(
    id bigserial NOT NULL,
    name character varying(256) NOT NULL,
    login character varying(256) NOT NULL,
    password character varying(256) NOT NULL,
    deleted boolean NOT NULL DEFAULT false,
    salt bytea NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (login)
);

CREATE TABLE public.orders
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    deleted boolean NOT NULL DEFAULT false,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
);

CREATE TABLE public.orders_products
(
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    CONSTRAINT order_id FOREIGN KEY (order_id)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT product_id FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE public.shopping_carts
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    deleted boolean NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);

CREATE TABLE public.shopping_carts_products
(
    cart_id bigint NOT NULL,
    product_id bigint NOT NULL,
    FOREIGN KEY (cart_id)
        REFERENCES public.shopping_carts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE public.roles
(
    id bigserial NOT NULL,
    role_name character varying(256) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO roles(role_name)
VALUES ('ADMIN'),
       ('USER');

CREATE TABLE public.users_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    FOREIGN KEY (role_id)
        REFERENCES public.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

DROP DATABASE bmw_shop;
