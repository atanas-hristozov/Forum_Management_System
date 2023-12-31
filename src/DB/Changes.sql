alter table comments
    add constraint comments_users_id_fk
        foreign key (author) references users (id);

/*NEW TABLE*/
create table users
(
    id         int auto_increment
        primary key,
    first_name varchar(32)  not null,
    last_name  varchar(32)  not null,
    email      varchar(255) not null,
    username   varchar(255) not null,
    password   varchar(255) not null,
    is_admin   tinyint(1)   not null,
    is_banned  tinyint(1)   not null
);

create table admins_phone_numbers
(
    id           int auto_increment
        primary key,
    phone_number varchar(255) not null,
    user_id      int          not null,
    constraint user_id_fk
        foreign key (user_id) references users (id)
);

create table posts
(
    id                int auto_increment
        primary key,
    title             varchar(255) not null,
    content           text         not null,
    likes             int          null,
    dislikes          int          null,
    user_id           int          not null,
    timestamp_created timestamp    not null,
    constraint posts_users_id
        foreign key (user_id) references users (id)
);

create table comments
(
    id        int auto_increment
        primary key,
    text      text not null,
    post_id   int  null,
    author_id int  not null,
    constraint comments_post_id_fk
        foreign key (post_id) references posts (id),
    constraint comments_users_id_fk
        foreign key (author_id) references users (id)
);


/*// Update 30.10*/
create table posts_tags
(
    post_id int null,
    tag_id  int not null,
    constraint posts_tags_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint posts_tags_tag_id_fk
        foreign key (tag_id) references tags (id)
);

create table tags
(
    id      int auto_increment
        primary key,
    content varchar(255) not null
);

