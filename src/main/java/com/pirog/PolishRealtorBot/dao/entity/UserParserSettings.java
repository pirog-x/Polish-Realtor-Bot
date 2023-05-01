package com.pirog.PolishRealtorBot.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user_parser_settings")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParserSettings implements Serializable {
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

    @NotBlank(message = "Ad type cannot be blank")
    @Column(name = "ad_type")
    String adType;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "settings__number_of_rooms",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "number_of_rooms", referencedColumnName = "number")
    )
    Set<NumberOfRoom> numberOfRooms;
}
