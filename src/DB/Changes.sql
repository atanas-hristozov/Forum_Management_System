alter table comments
    add constraint comments_users_id_fk
        foreign key (author) references users (id);