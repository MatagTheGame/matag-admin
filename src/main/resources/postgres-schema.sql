create table matag_user
(
    id                bigserial primary key,
    username          varchar(25)  not null unique,
    password          varchar(255) not null unique,
    email_address     varchar(100) not null unique,
    status            varchar(20)  not null,
    type              varchar(20)  not null,
    created_at        timestamp    not null,
    updated_at        timestamp    not null
);

create table matag_user_verification
(
    id                bigserial primary key,
    matag_user_id     bigint    not null unique,
    verification_code char(10),
    valid_until       timestamp,
    attempts          int not null,
    foreign key (id) references matag_user (id) on delete cascade
);

create table matag_session
(
    id            bigserial primary key,
    session_id    char(36) unique,
    matag_user_id bigint    not null,
    created_at    timestamp not null,
    valid_until   timestamp not null,
    foreign key (matag_user_id) references matag_user (id) on delete cascade
);

create table game
(
    id          bigserial primary key,
    created_at  timestamp   not null,
    type        varchar(20) not null,
    status      varchar(20) not null,
    result      varchar(20),
    finished_at timestamp
);

create table game_session
(
    id             bigserial primary key,
    game_id        bigint not null,
    session_id     bigint unique,
    player_id      bigint not null,
    player_options varchar(255),
    foreign key (game_id) references game (id) on delete cascade,
    foreign key (session_id) references matag_session (id) on delete set null,
    foreign key (player_id) references matag_user (id) on delete set null
);

-- insert Guest (password: password)
insert into matag_user(username, password, email_address, status, type, created_at, updated_at)
values ('Guest', '{argon2}$argon2id$v=19$m=65536,t=4,p=8$LI8W+vC+a36vqqNbKu9RXw$/PGm7X3l6DEro/p7KYnOmKUW7a0+vX4NeynAP6QEV7M', 'guest@matag.com', 'ACTIVE', 'GUEST', current_timestamp, current_timestamp);




-- ### USEFUL QUERIES ###

-- select
select * from matag_user;
select * from matag_user_verification;
select * from matag_session;
select * from game;
select * from game_session;


-- composite
select * from game g join game_session gs on g.id = gs.game_id;
















-- delete stuff
delete from game_session;
delete from game;
delete from matag_session;
delete from matag_session where valid_until < current_timestamp;
delete from matag_user where username = 'antonio85';


-- drop everything
drop table game_session;
drop table game;
drop table matag_session;
drop table matag_user_verification;
drop table matag_user;



-- change stuff
ALTER TABLE matag_user ALTER COLUMN type SET NOT NULL;
ALTER TABLE game add column finished_at timestamp;
ALTER TABLE matag_user drop column status;
