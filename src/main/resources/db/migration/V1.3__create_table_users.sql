CREATE TABLE IF NOT EXISTS users (
    id serial NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role varchar(10) default 'USER' check (role in ('ADMIN', 'USER')),
    CONSTRAINT pk_users PRIMARY KEY (id)
);
