create type users_role_enum as enum('admin', 'user', 'super_admin','moderator');
create type users_status_enum as enum('active', 'inactive', 'deleted','pending','banned');

create table if not exists users (
    id UUID primary key,
    cover varchar(255),
    avatar varchar(255),
    username varchar(100) not null unique,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    password varchar(100) not null,
    salt varchar(100) not null,
    bio varchar(255),
    email varchar(100) not null unique,
    phone varchar(20) not null unique,
    followers_count int default 0,
    posts_count int default 0,
    status users_status_enum not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    role users_role_enum not null
);

create table if not exists topics (
    id UUID primary key,
    name varchar(100) not null unique,
    posts_count int default 0,
    created_at timestamp not null,
    updated_at timestamp not null,
    color varchar(10) not null
);
create type posts_type_enum as enum('text', 'image', 'video','link','event','poll','product','advertisement');

create table if not exists posts (
    id UUID primary key,
    content text not null,
    image varchar(255),
    author_id UUID not null,
    topic_id UUID not null,
    liked_count int default 0,
    comments_count int default 0,
    is_featured int default 0,
    type posts_type_enum,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (author_id) references users(id),
    foreign key (topic_id) references topics(id)
);

create table if not exists tags (
    id UUID primary key,
    name varchar(100) not null unique,
    posts_count int default 0,
    created_at timestamp not null,
    updated_at timestamp not null
);


create type comments_type_enum as enum('active', 'hidden', 'deleted','reported');

create table if not exists comments (
    id UUID primary key,
    user_id UUID not null,
    post_id UUID not null,
    parent_id UUID,
    content text not null,
    liked_count int default 0,
    reply_count int default 0,
    status comments_type_enum not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (user_id) references users(id),
    foreign key (post_id) references posts(id),
    foreign key (parent_id) references comments(id)
);

create table if not exists comments_likes (
    comment_id UUID not null,
    user_id UUID not null,
    created_at timestamp not null,
    primary key (comment_id, user_id),
    foreign key (comment_id) references comments(id),
    foreign key (user_id) references users(id)
);

create table if not exists posts_likes (
    post_id UUID not null,
    user_id UUID not null,
    created_at timestamp not null,
    primary key (post_id, user_id),
    foreign key (post_id) references posts(id),
    foreign key (user_id) references users(id)
);

create type chat_room_type_enum as enum('private_chat', 'group', 'public_chat', 'channel', 'support', 'broadcast');

create table if not exists chat_rooms (
    id UUID primary key,
    creator_id UUID not null,
    receiver_id UUID not null,
    type chat_room_type_enum not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    deleted_at timestamp,
    foreign key (creator_id) references users(id),
    foreign key (receiver_id) references users(id)
);

create type notification_action_enum as enum('mark_as_read','delete');

create table if not exists notifications (
    id UUID primary key,
    receiver_id UUID not null,
    content text,
    action notification_action_enum not null,
    is_sent int default 1,
    is_read int default 1,
    created_at timestamp not null,
    updated_at timestamp not null,
    actor_id UUID,
    foreign key (receiver_id) references users(id),
    foreign key (actor_id) references users(id)
);

create table if not exists chat_messages (
    id UUID primary key,
    room_id UUID not null,
    sender_id UUID not null,
    content text not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    foreign key (room_id) references chat_rooms(id),
    foreign key (sender_id) references users(id)
);

create table if not exists followers (
    follower_id UUID not null,
    following_id UUID not null,
    created_at timestamp not null,
    primary key (follower_id, following_id),
    foreign key (follower_id) references users(id),
    foreign key (following_id) references users(id)
);

create table if not exists posts_tags (
    post_id UUID not null,
    tag_id UUID not null,
    created_at timestamp not null,
    primary key (post_id, tag_id),
    foreign key (post_id) references posts(id),
    foreign key (tag_id) references tags(id)
);


