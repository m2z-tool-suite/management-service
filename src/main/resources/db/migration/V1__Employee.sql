CREATE TABLE employee
(
    id                varchar(64),
    email             varchar(64) unique not null,
    identity_provider char(1)            not null,
    first_name        varchar(64)        not null,
    last_name         varchar(64)        not null
);

CREATE UNIQUE INDEX application_user_unique_index_username ON employee (email);