create table orm_encapsulados.comment
(
    id                int auto_increment
        primary key,
    text              text null,
    post_id           int  null,
    author_id         int  null,
    parent_comment_id int  null
);

