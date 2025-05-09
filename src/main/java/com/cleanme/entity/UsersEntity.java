package com.cleanme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue
    @Column(name="UID")
    private UUID uid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "user_type")
    private String user_type;


    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservationAsUser;

    @OneToMany(mappedBy = "cleaner")
    private List<ReservationEntity> reservationAsCleaner;

}
