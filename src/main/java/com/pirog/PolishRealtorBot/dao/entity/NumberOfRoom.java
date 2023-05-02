package com.pirog.PolishRealtorBot.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "number_of_rooms")
@Getter
@Setter
public class NumberOfRoom implements Serializable {
    @Id
    @Enumerated(EnumType.STRING)
    private NumberOfRoomEnum number;

    @Enumerated(EnumType.STRING)
    @ManyToMany(mappedBy = "numberOfRooms")
    private Set<UserParserSettings> userSettings;
}
