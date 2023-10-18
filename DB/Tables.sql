create table users
(
    id          int auto_increment
        primary key,
    first_Name  varchar(32)  not null,
    last_Name   varchar(32)  not null,
    email       varchar(255) not null,
    username    varchar(255) not null,
    password    varchar(255) not null,
    isAdmin     tinyint(1)   not null,
    phoneNumber varchar(255) null
);

create table comments
(
    id     int auto_increment
        primary key,
    text   text not null,
    author int  not null,
    constraint comments_users_id_fk
        foreign key (author) references users (id)
);

create table posts
(
    id         int auto_increment
        primary key,
    title      varchar(255) not null,
    content    text         not null,
    `like`     int          null,
    dislike    int          null,
    user_id    int          not null,
    comment_id int          null,
    constraint posts_comments_id_fk
        foreign key (comment_id) references comments (id),
    constraint posts_users_id
        foreign key (user_id) references users (id)
);

