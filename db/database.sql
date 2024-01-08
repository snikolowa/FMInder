create table chats
(
    chat_id     int auto_increment
        primary key,
    receiver_id int                                 null,
    sender_id   int                                 null,
    message     text                                null,
    timestamp   timestamp default CURRENT_TIMESTAMP not null,
    constraint chats_ibfk_1
        foreign key (receiver_id) references fminder.users (user_id),
    constraint chats_ibfk_2
        foreign key (sender_id) references fminder.users (user_id)
);

create index receiver_id
    on chats (receiver_id);

create index sender_id
    on chats (sender_id);

create table requests
(
    request_id       int auto_increment
        primary key,
    sender_user_id   int                                    null,
    receiver_user_id int                                    null,
    created_on       timestamp default CURRENT_TIMESTAMP    not null,
    updated_on       timestamp default CURRENT_TIMESTAMP    not null on update CURRENT_TIMESTAMP,
    status           enum ('Pending', 'Accepted', 'Denied') not null,
    constraint requests_ibfk_1
        foreign key (sender_user_id) references fminder.users (user_id),
    constraint requests_ibfk_2
        foreign key (receiver_user_id) references fminder.users (user_id)
);

create index receiver_user_id
    on requests (receiver_user_id);

create index sender_user_id
    on requests (sender_user_id);

create table users
(
    user_id         int auto_increment
        primary key,
    email           varchar(255)                     not null,
    password        varchar(255)                     not null,
    first_name      varchar(255)                     not null,
    last_name       varchar(255)                     not null,
    gender          enum ('Male', 'Female', 'Other') null,
    graduate_year   int                              null,
    major           varchar(255)                     null,
    profile_pic     varchar(255)                     null,
    interests       text                             null,
    profile_picture varchar(2048)                    null
);
