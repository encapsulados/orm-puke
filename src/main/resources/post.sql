create table orm_encapsulados.post
(
    content   text not null,
    id        int auto_increment
        primary key,
    author_id int  null
);

