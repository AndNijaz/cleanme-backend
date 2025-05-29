package com.cleanme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditReviewDto {
    UUID reviewId;
    Integer rating;
    String comment;
}
