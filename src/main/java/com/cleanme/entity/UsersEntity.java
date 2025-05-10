package com.cleanme.entity;

import com.cleanme.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue
    @Column(name = "UID")
    private UUID uid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservationAsUser;

    @OneToMany(mappedBy = "cleaner")
    private List<ReservationEntity> reservationAsCleaner;
}
