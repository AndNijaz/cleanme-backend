package com.cleanme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private UUID id;
    private UUID reservationId;
    private UUID cleanerId;
    private UUID userId;
    private int rating;
    private String comment;
    private LocalDate date;
    private String cleanerName;
}
