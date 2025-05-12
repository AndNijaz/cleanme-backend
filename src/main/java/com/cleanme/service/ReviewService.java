package com.cleanme.service;

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
import java.util.UUID;

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
        return dto;
    }
}
