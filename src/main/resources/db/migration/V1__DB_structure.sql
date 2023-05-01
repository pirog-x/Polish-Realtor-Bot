create table if not exists number_of_rooms (
    number varchar(255) not null,
    primary key (number)
);

create table if not exists settings__number_of_rooms (
  user_id int8 not null,
  number_of_rooms varchar(255) not null,
  primary key (user_id, number_of_rooms)
);

create table if not exists user_parser_settings (
  user_id int8 not null,
  ad_type varchar(255),
  city varchar(255),
  language varchar(255),
  max_price int4,
  min_price int4,
  primary key (user_id)
);

alter table if exists settings__number_of_rooms
    add constraint number_of_rooms_fk
    foreign key (number_of_rooms) references number_of_rooms;

alter table if exists settings__number_of_rooms
    add constraint user_id_fk
    foreign key (user_id) references user_parser_settings;
