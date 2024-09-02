create table if not exists application_user (
    id  bigserial not null,
    email varchar(255),
    fist_name varchar(255),
    password varchar(255),
    patronymic varchar(255),
    role varchar(255),
    status varchar(255),
    surname varchar(255),
    username varchar(255),
    primary key (id)
                                            );

create table if not exists login_approving_code (
    id  bigserial not null,
    code int4,
    email varchar(255),
    primary key (id)
                                  );

create table if not exists password_reseating_code (
    id  bigserial not null,
    code int4,
    email varchar(255),
    primary key (id)
                                     );

create table if not exists registration_approving_code (
    id  bigserial not null,
    code int4,
    email varchar(255),
    primary key (id)
                                                       );

create table scheduled_event (
    id  bigserial not null,
    date timestamp,
    description varchar(255),
    has_passed boolean,
    priority varchar(255),
    title varchar(255),
    application_user_id int8,
    primary key (id)
                             );

alter table application_user
    add constraint UK_cb61p28hanadv7k0nx1ec0n5l unique (email);

alter table application_user
    add constraint UK_6c0v0rco93sykgyetukfmkkod unique (username);

alter table scheduled_event
    add constraint FKielqemiduf17phx6ft4dcil2d foreign key (application_user_id) references application_user;
