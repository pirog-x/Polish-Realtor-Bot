package com.pirog.PolishRealtorBot.dao.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "number_of_rooms")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NumberOfRoom implements Serializable {
    @Id
    @Enumerated(EnumType.STRING)
    NumberOfRoomEnum number;

    @Enumerated(EnumType.STRING)
    @ManyToMany(mappedBy = "numberOfRooms")
    Set<UserParserSettings> userSettings;
}
