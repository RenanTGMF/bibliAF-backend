create table if not exists genres (
    id serial not null,
    name varchar(255) not null,
    constraint pk_genres primary key (id)
);