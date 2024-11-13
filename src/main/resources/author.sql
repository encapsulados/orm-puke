create table orm_encapsulados.author
(
    id       int auto_increment
        primary key,
    username varchar(255) not null,
    email    varchar(255) null
);

