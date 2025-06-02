package com.cleanme.service;

import com.cleanme.dto.BookingWithReviewDto;
import com.cleanme.dto.EditReviewDto;
import com.cleanme.dto.ReviewDto;
import com.cleanme.entity.ReservationEntity;
import com.cleanme.entity.ReviewEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.repository.ReservationRepository;
import com.cleanme.repository.ReviewRepository;
import com.cleanme.repository.UsersRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private ReviewService reviewService;

    private UUID reservationId, userId, cleanerId, reviewId;
    private UsersEntity user, cleaner;
    private ReservationEntity reservation;
    private ReviewEntity review;

    @BeforeEach
    void setup() {
        reservationId = UUID.randomUUID();
        userId = UUID.randomUUID();
        cleanerId = UUID.randomUUID();
        reviewId = UUID.randomUUID();

        user = new UsersEntity();
        user.setUid(userId);
        user.setFirstName("User");
        user.setLastName("Test");

        cleaner = new UsersEntity();
        cleaner.setUid(cleanerId);
        cleaner.setFirstName("Cleaner");
        cleaner.setLastName("Test");

        reservation = new ReservationEntity();
        reservation.setRid(reservationId);
        reservation.setUser(user);
        reservation.setCleaner(cleaner);

        review = new ReviewEntity();
        review.setId(reviewId);
        review.setReservation(reservation);
        review.setUser(user);
        review.setCleaner(cleaner);
        review.setRating(5);
        review.setComment("Great service");
        review.setDate(LocalDate.now());
    }

    // Test 1: Happy path for reviewReservation
    @Test
    void reviewReservation_success() {
        ReviewDto dto = new ReviewDto(null, reservationId, cleanerId, userId, 5, "Great service", null, null);

        when(reservationRepository.findReservationEntityByRid(reservationId)).thenReturn(Optional.of(reservation));
        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(inv -> {
            ReviewEntity entity = inv.getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        ReviewDto result = reviewService.reviewReservation(reservationId, dto);

        assertThat(result).isNotNull();
        assertThat(result.getRating()).isEqualTo(5);
        assertThat(result.getComment()).isEqualTo("Great service");
    }

    // Test 2: Error path for reviewReservation
    @Test
    void reviewReservation_throwsIfReservationNotFound() {
        when(reservationRepository.findReservationEntityByRid(reservationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.reviewReservation(reservationId, new ReviewDto()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Reservation Not Found");
    }

    // Test 3: getAllReviewsForCleaner
    @Test
    void getAllReviewsForCleaner_success() {
        when(usersRepository.findUsersEntityByUid(cleanerId)).thenReturn(Optional.of(cleaner));
        when(reviewRepository.findByCleaner(cleaner)).thenReturn(List.of(review));

        List<ReviewDto> result = reviewService.getAllReviewsForCleaner(cleanerId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCleanerName()).isEqualTo("Cleaner Test");
    }

    // Test 4: getAllReviewsByUser
    @Test
    void getAllReviewsByUser_success() {
        when(usersRepository.findUsersEntityByUid(userId)).thenReturn(Optional.of(user));
        when(reviewRepository.findByUser(user)).thenReturn(List.of(review));

        List<ReviewDto> result = reviewService.getAllReviewsByUser(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
    }

    // Test 5: getBookingsWithReviewsForUser
    @Test
    void getBookingsWithReviewsForUser_success() {
        when(usersRepository.findUsersEntityByUid(userId)).thenReturn(Optional.of(user));
        when(reservationRepository.findByUser_Uid(userId)).thenReturn(List.of(reservation));
        when(reviewRepository.findByUser(user)).thenReturn(List.of(review));

        List<BookingWithReviewDto> result = reviewService.getBookingsWithReviewsForUser(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookingId()).isEqualTo(reservationId);
        assertThat(result.get(0).getReview()).isNotNull();
    }

    // Test 6: updateReview
    @Test
    void updateReview_success() {
        EditReviewDto editReviewDto = new EditReviewDto();
        editReviewDto.setRating(4);
        editReviewDto.setComment("Updated comment");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewService.updateReview(reviewId, editReviewDto);

        verify(reviewRepository).save(any(ReviewEntity.class));
        assertThat(review.getComment()).isEqualTo("Updated comment");
        assertThat(review.getRating()).isEqualTo(4);
    }

    // Optional: Error path for updateReview
    @Test
    void updateReview_throwsIfNotFound() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.updateReview(reviewId, new EditReviewDto()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Review not found");
    }
}
