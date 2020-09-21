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

DROP DATABASE bmw_shop;
