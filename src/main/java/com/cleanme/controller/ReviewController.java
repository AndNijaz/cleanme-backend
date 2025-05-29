package com.cleanme.controller;

import com.cleanme.dto.ReviewDto;
import com.cleanme.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{reservationId}")
    public ResponseEntity<ReviewDto> reviewReservation(@PathVariable UUID reservationId,
                                                       @RequestBody ReviewDto reviewDto) {
        ReviewDto savedReview = this.reviewService.reviewReservation(reservationId, reviewDto);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/cleaner/{cleanerId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForCleaner(@PathVariable UUID cleanerId) {
        List<ReviewDto> reviews = reviewService.getAllReviewsForCleaner(cleanerId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getUserReviews(@PathVariable UUID userId) {
        List<ReviewDto> reviews = reviewService.getAllReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }
}
