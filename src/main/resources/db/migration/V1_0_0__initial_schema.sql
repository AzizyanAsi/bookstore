-- initial DB schema

-- SEQUENCES
-- Batch processing tables only (set default increment of hibernate)

CREATE SEQUENCE public.system_user_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


-- TABLES

CREATE TABLE public.role (
    id                      bigserial, -- auto-increment identity (static data table)
    description             varchar(100)                NOT NULL,
    role_type               varchar(20)                 NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_role_type
        UNIQUE (role_type)
);



CREATE TABLE public.system_user (
    id                      bigint                      NOT NULL,
    first_name              varchar(255)                NOT NULL,
    last_name               varchar(255)                NOT NULL,
    contact_email           varchar(255)                NOT NULL,
    password                varchar(255)                NOT NULL,
    contact_phone_number    varchar(25)                 NOT NULL,
    active                  boolean                     NOT NULL,
    role_id                 bigint                      NOT NULL,
    created                 timestamp                   NOT NULL,
    updated                 timestamp                   NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_contact_email
        UNIQUE (contact_email),
    CONSTRAINT uk_contact_phone_number
        UNIQUE (contact_phone_number),
    CONSTRAINT fk_system_user__role_id
        FOREIGN KEY (role_id) REFERENCES role
 );

CREATE TABLE author (
   id                       bigserial, -- auto-increment identity (static data table)
   first_name               varchar(255)                  NOT NULL,
   last_name                varchar(255)                  NOT NULL,

   PRIMARY KEY (id)
);

CREATE TABLE genre (
   id                       bigserial, -- auto-increment identity (static data table)
   name                     varchar(100)                  NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT uk_genre_name
      UNIQUE (name)

);


CREATE TABLE public.book (
   id                        bigserial, -- auto-increment identity (static data table)
   title                     varchar(100)                 NOT NULL,
   price                     decimal(10,2)                NOT NULL,
   genre_id                  bigint                       NOT NULL,
   author_id                 bigint                       NOT NULL,

   PRIMARY KEY (id),
   CONSTRAINT fk_book__genre_id
       FOREIGN KEY (genre_id) REFERENCES genre,
   CONSTRAINT fk_book__author_id
       FOREIGN KEY (author_id) REFERENCES author
);

