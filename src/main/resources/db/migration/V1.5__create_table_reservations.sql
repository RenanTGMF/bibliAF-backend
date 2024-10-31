create table if not exists reservations (
    id serial not null,
    book_id bigint not null,
    user_id bigint not null,
    reservation_date date not null,
    status varchar(10) default 'ACTIVE' check (status in ('ACTIVE', 'CANCELLED')),
    constraint pk_reservations primary key (id),
    constraint fk_reservations_books foreign key (book_id) references books(id),
    constraint fk_reservations_users foreign key (user_id) references users(id)
);