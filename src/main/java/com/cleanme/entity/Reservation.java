package com.cleanme.entity;

import jakarta.persistence.*;

@Entity
@Table("reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user")
    private long userId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cleaner")
    private long cleanerId;
    @Column(name = "date")
    String date;
    @Column(name = "time")
    String time;
    @Column(name = "location")
    String  location;
    @Column(name = "status")
    String Status;
    @Column(name = "comment")
    String comment;
}
