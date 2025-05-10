package com.cleanme.entity;

import com.cleanme.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue
    @Column(name = "RID")
    private UUID rid;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "cleaner", nullable = false)
    private UsersEntity cleaner;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @Column(name = "comment")
    private String comment;

    public void setRid(UUID rid) {
        this.rid = rid;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public void setCleaner(UsersEntity cleaner) {
        this.cleaner = cleaner;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getRid() {
        return rid;
    }

    public UsersEntity getUser() {
        return user;
    }

    public UsersEntity getCleaner() {
        return cleaner;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }
}


