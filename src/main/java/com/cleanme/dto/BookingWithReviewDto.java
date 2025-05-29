package com.cleanme.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class BookingWithReviewDto {
    private UUID bookingId;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private String comment;
    private String cleanerName;
    private UUID cleanerId;

    // Optional
    private ReviewDto review;
}