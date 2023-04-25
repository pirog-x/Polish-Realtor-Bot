package com.pirog.PolishRealtorBot.dao.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_parser_settings")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParserSettings {
    @Id
    @Column(name = "user_id")
    Long userId;

    @NotBlank(message = "Language cannot be blank.")
    @Column(name = "language")
    String language;

    @NotBlank(message = "City cannot be blank.")
    @Column(name = "city")
    String city;

    @Min(0)
    @Column(name = "min_price")
    Integer minPrice;

    @Min(0)
    @Column(name = "max_price")
    Integer maxPrice;

    @Column(name = "ad_type")
    String adType;

    @Max(4)
    @Column(name = "num_of_rooms")
    Integer numOfRooms;
}
