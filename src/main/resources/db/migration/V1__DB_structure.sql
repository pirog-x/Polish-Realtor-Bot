CREATE TABLE if NOT EXISTS number_of_rooms
(
    number VARCHAR(255) NOT NULL,
    PRIMARY key (number)
);

CREATE TABLE if NOT EXISTS settings__number_of_rooms
(
    user_id         int8         NOT NULL,
    number_of_rooms VARCHAR(255) NOT NULL,
    PRIMARY key (user_id, number_of_rooms)
);

CREATE TABLE if NOT EXISTS user_parser_settings
(
    user_id   int8 NOT NULL,
    ad_type   VARCHAR(255),
    city      VARCHAR(255),
    LANGUAGE  VARCHAR(255),
    max_price int4,
    min_price int4,
    PRIMARY key (user_id)
);

ALTER TABLE if EXISTS settings__number_of_rooms
    ADD CONSTRAINT number_of_rooms_fk FOREIGN key (number_of_rooms) REFERENCES number_of_rooms;

ALTER TABLE if EXISTS settings__number_of_rooms
    ADD CONSTRAINT user_id_fk FOREIGN key (user_id) REFERENCES user_parser_settings;