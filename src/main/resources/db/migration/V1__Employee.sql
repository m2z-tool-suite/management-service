CREATE TABLE employee
(
    id                varchar(64) primary key,
    email             varchar(64) unique                                             not null,
    identity_provider char(1)                                                        not null,
    first_name        varchar(64)                                                    not null,
    last_name         varchar(64)                                                    not null,
    created_at        timestamp without time zone default (now() at time zone 'utc') not null
);

CREATE UNIQUE INDEX application_employee_unique_index_id_and_email ON employee (id, email);
CREATE UNIQUE INDEX application_employee_unique_index_email ON employee (email);