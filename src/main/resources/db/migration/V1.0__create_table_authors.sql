create table if not exists authors (
    id serial not null,
    name varchar(255) not null,
    bio text,
    constraint pk_authors primary key (id)
);