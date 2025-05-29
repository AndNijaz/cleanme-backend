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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UsersRepository usersRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         ReservationRepository reservationRepository,
                         UsersRepository usersRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.usersRepository = usersRepository;
    }

    public ReviewDto  reviewReservation(UUID reservationId, ReviewDto dto) {
        ReservationEntity reservation = reservationRepository
                .findReservationEntityByRid(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation Not Found"));
        if (reservation == null) {
            throw new RuntimeException("Reservation Not Found");
        }

        ReviewEntity review = new ReviewEntity();
        review.setReservation(reservation);
        review.setCleaner(reservation.getCleaner());
        review.setUser(reservation.getUser());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setDate(LocalDate.now());

        ReviewEntity saved = reviewRepository.save(review);
        return mapToDto(saved);


    }

    public List<ReviewDto> getAllReviewsForCleaner(UUID cleanerId) {
        UsersEntity cleaner = usersRepository.findUsersEntityByUid(cleanerId)
                .orElseThrow(() -> new RuntimeException("Cleaner Not Found"));

        List<ReviewEntity> reviews = reviewRepository.findByCleaner(cleaner);

        return reviews.stream()
                .map(this::mapToDto)
                .toList();
    }

    private ReviewDto mapToDto(ReviewEntity entity) {
        ReviewDto dto = new ReviewDto();
        dto.setId(entity.getId());
        dto.setReservationId(entity.getReservation().getRid());
        dto.setCleanerId(entity.getCleaner().getUid());
        dto.setUserId(entity.getUser().getUid());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setDate(entity.getDate());
        String cleanerFullName = entity.getCleaner().getFirstName() + " " + entity.getCleaner().getLastName();
        dto.setCleanerName(cleanerFullName);
        return dto;
    }

    public List<ReviewDto> getAllReviewsByUser(UUID userId) {
        UsersEntity user = usersRepository.findUsersEntityByUid(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ReviewEntity> reviews = reviewRepository.findByUser(user);

        return reviews.stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<BookingWithReviewDto> getBookingsWithReviewsForUser(UUID userId) {
        UsersEntity user = usersRepository.findUsersEntityByUid(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ReservationEntity> reservations = reservationRepository.findByUser_Uid(userId);
        List<ReviewEntity> reviews = reviewRepository.findByUser(user);

        // Map reviews by reservation ID for quick lookup
        Map<UUID, ReviewEntity> reviewMap = reviews.stream()
                .collect(Collectors.toMap(
                        r -> r.getReservation().getRid(),
                        r -> r
                ));

        return reservations.stream()
                .map(reservation -> {
                    BookingWithReviewDto dto = new BookingWithReviewDto();
                    dto.setBookingId(reservation.getRid());
                    dto.setDate(reservation.getDate());
                    dto.setTime(reservation.getTime());
                    dto.setLocation(reservation.getLocation());
                    dto.setComment(reservation.getComment());

                    UsersEntity cleaner = reservation.getCleaner();
                    dto.setCleanerId(cleaner.getUid());
                    dto.setCleanerName(cleaner.getFirstName() + " " + cleaner.getLastName());

                    // Add review if it exists
                    if (reviewMap.containsKey(reservation.getRid())) {
                        dto.setReview(mapToDto(reviewMap.get(reservation.getRid())));
                    }

                    return dto;
                })
                .toList();
    }

    public void updateReview(UUID reviewId, EditReviewDto reviewDto) {
        ReviewEntity existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        existingReview.setDate(LocalDate.now());
        // Add any other fields you allow to be updated

        reviewRepository.save(existingReview);
    }



}
