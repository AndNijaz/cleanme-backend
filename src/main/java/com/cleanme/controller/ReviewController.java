package com.cleanme.controller;

import com.cleanme.dto.BookingWithReviewDto;
import com.cleanme.dto.EditReviewDto;
import com.cleanme.dto.ReviewDto;
import com.cleanme.service.ReviewService;
import com.cleanme.utilities.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtils securityUtils;

    @PostMapping("/{reservationId}")
    public ResponseEntity<ReviewDto> reviewReservation(@PathVariable UUID reservationId,
                                                       @RequestBody ReviewDto reviewDto) {
        System.out.println("KAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADIIIIIIIIIIIIR");
        System.out.println(reviewDto);
        System.out.println("KAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADIIIIIIIIIIIIR");

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

    @GetMapping("/reviews/bookings")
    public ResponseEntity<List<BookingWithReviewDto>> getAllBookingsWithReviews(Authentication auth) {
        UUID userId = securityUtils.extractUserId(auth);
        List<BookingWithReviewDto> result = reviewService.getBookingsWithReviewsForUser(userId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable UUID reviewId, @RequestBody EditReviewDto editReviewDto) {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(reviewId);
        System.out.println(editReviewDto);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

        reviewService.updateReview(reviewId, editReviewDto);
        return ResponseEntity.ok().body(Map.of("success", true));
    }



}
