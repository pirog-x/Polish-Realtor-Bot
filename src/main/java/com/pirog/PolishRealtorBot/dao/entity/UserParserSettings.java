package com.pirog.PolishRealtorBot.dao.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_parser_settings")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParserSettings {
    @Id
    @Column(name = "user_id")
    Long userId;

    @Column(name = "city")
    String city;

    @Column(name = "min_price")
    Integer minPrice;

    @Column(name = "max_price")
    Integer maxPrice;

    @Column(name = "ad_type")
    String adType;

    @Column(name = "num_of_rooms")
    Integer numOfRooms;
}
