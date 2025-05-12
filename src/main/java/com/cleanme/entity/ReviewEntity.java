package com.cleanme.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "cleaner_id", nullable = false)
    private UsersEntity cleaner;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
