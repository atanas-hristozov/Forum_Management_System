create table users
(
    id           int auto_increment
        primary key,
    first_name   varchar(32)  not null,
    last_name    varchar(32)  not null,
    email        varchar(255) not null,
    username     varchar(255) not null,
    password     varchar(255) not null,
    isAdmin      tinyint(1) not null,
    phone_number varchar(255) null
);

create table posts
(
    id      int auto_increment
        primary key,
    title   varchar(255) not null,
    content text         not null,
    `like`  int null,
    dislike int null,
    user_id int          not null,
    constraint posts_users_id
        foreign key (user_id) references users (id)
);

create table comments
(
    id      int auto_increment
        primary key,
    text    text not null,
    post_id int null,
    author  int  not null,
    constraint comments_post_id_fk
        foreign key (post_id) references posts (id)
);

create index comments_posts_id_fk
    on comments (author);

