package com.cleanme.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "cleaner")
    private UsersEntity cleaner;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String time;

    @Column(name = "location")
    private String location;

    @Column(name = "status")
    private String status;

    @Column(name = "comment")
    private String comment;

    @Override
    public String toString() {
        return "ReservationEntity{" +
                "rid=" + rid +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public void setRid(UUID rid) {
        this.rid = rid;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public void setCleaner(UsersEntity cleaner) {
        this.cleaner = cleaner;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(String status) {
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }
}


