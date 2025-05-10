package com.cleanme.entity;

import com.cleanme.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType user_type;

    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservationAsUser;

    @OneToMany(mappedBy = "cleaner")
    private List<ReservationEntity> reservationAsCleaner;

    public UUID getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserType getUser_type() {
        return user_type;
    }

    public List<ReservationEntity> getReservationAsUser() {
        return reservationAsUser;
    }

    public List<ReservationEntity> getReservationAsCleaner() {
        return reservationAsCleaner;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_type(UserType user_type) {
        this.user_type = user_type;
    }

    public void setReservationAsUser(List<ReservationEntity> reservationAsUser) {
        this.reservationAsUser = reservationAsUser;
    }

    public void setReservationAsCleaner(List<ReservationEntity> reservationAsCleaner) {
        this.reservationAsCleaner = reservationAsCleaner;
    }
}
