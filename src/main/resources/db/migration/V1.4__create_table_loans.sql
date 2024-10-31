create table if not exists loans (
   id serial not null,
    book_id bigint not null,
    user_id bigint not null,
    loan_date date not null,
    return_date date,
    status varchar(10) default 'PENDING' check (status in ('PENDING', 'RETURNED', 'OVERDUE')),
    constraint pk_loans primary key (id),
    constraint fk_loans_books foreign key (book_id) references books(id),
    constraint fk_loans_users foreign key (user_id) references users(id)
);
