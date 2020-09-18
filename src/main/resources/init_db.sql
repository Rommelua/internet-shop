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

DROP DATABASE bmw_shop;
