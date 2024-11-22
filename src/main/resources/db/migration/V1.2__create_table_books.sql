create table if not exists books (
    id serial not null,
    image text not null,
    title varchar(255) not null,
    author_id bigint not null,
    genre_id bigint not null,
    publication_year int,
    isbn varchar(20) unique,
    summary text,
    copies int default 1,
    copies_available int default 0,
    constraint pk_books primary key (id),
    constraint fk_books_authors foreign key (author_id) references authors(id),
    constraint fk_books_genres foreign key (genre_id) references genres(id)
);