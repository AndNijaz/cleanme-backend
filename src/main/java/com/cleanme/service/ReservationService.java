package com.cleanme.service;

import com.cleanme.dto.CreateReservationDto;
import com.cleanme.dto.ReservationDto;
import com.cleanme.dto.UpdateReservationDto;
import com.cleanme.entity.ReservationEntity;
import com.cleanme.enums.ReservationStatus;
import com.cleanme.entity.UsersEntity;
import com.cleanme.repository.ReservationRepository;
import com.cleanme.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;
    private final UsersRepository usersRepository;

    private ReservationDto mapToReservationDto(ReservationEntity reservation) {
        String cleanerName = reservation.getCleaner() != null
                ? reservation.getCleaner().getFirstName() + " " + reservation.getCleaner().getLastName()
                : null;

        return new ReservationDto(
                reservation.getRid(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getLocation(),
                reservation.getStatus(), // ako koristiš enum
                reservation.getComment(),
                cleanerName
        );
    }

    public ReservationService(ReservationRepository reservationRepository, UsersRepository usersRepository) {
        this.reservationRepository = reservationRepository;
        this.usersRepository = usersRepository;
    }

    public List<ReservationDto> getReservations(UUID id){
        return reservationRepository.findByUser_Uid(id).stream().map(this::mapToReservationDto).toList();
    }

    public ReservationDto getReservation(UUID id){
        ReservationEntity reservation = reservationRepository.findReservationEntityByRid(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        String cleanerName = reservation.getCleaner() != null
                ? reservation.getCleaner().getFirstName() + " " + reservation.getCleaner().getLastName()
                : null;

        return new ReservationDto(
                reservation.getRid(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getLocation(),
                reservation.getStatus(),
                reservation.getComment(),
                cleanerName
        );
    }

    @Transactional
    public ReservationDto createReservation(UUID myId, CreateReservationDto dto) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println(dto);
        System.out.println("--------------------------------------------------------------------");
        UsersEntity user = usersRepository.findUsersEntityByUid(myId).orElseThrow(() -> new RuntimeException("User not found"));
        UsersEntity cleaner = usersRepository.findUsersEntityByUid(dto.getCleanerID()).orElseThrow(() -> new RuntimeException("Cleaner not found"));

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUser(user);
        reservation.setCleaner(cleaner);
        reservation.setDate(dto.getDate());
        reservation.setTime(dto.getTime());
        reservation.setLocation(dto.getLocation());
        reservation.setStatus(dto.getStatus());
        reservation.setComment(dto.getComment());

        ReservationEntity saved = reservationRepository.save(reservation);

        String cleanerName = cleaner.getFirstName() + " " + cleaner.getLastName();

        return new ReservationDto(
                saved.getRid(),
                saved.getDate(),
                saved.getTime(),
                saved.getLocation(),
                saved.getStatus(),
                saved.getComment(),
                cleanerName
        );
    }

    @Transactional
    public ReservationDto updateReservationDto(UUID id, UUID myID, UpdateReservationDto dto) {
        ReservationEntity reservation = reservationRepository.findReservationEntityByRid(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        UsersEntity cleaner = usersRepository.findUsersEntityByUid(dto.getCleanerId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (reservation == null || !reservation.getUser().getUid().equals(myID)) {
            throw new RuntimeException("Reservation not found or not owned by user"); //
        }

        reservation.setCleaner(cleaner);
        reservation.setDate(dto.getDate());
        reservation.setTime(dto.getTime());
        reservation.setLocation(dto.getLocation());
        reservation.setStatus(dto.getStatus());
        reservation.setComment(dto.getComment());

        ReservationEntity saved = reservationRepository.save(reservation);

        String cleanerName = cleaner != null ? cleaner.getFirstName() + " " + cleaner.getLastName() : null;

        return  new ReservationDto(
                saved.getRid(),
                saved.getDate(),
                saved.getTime(),
                saved.getLocation(),
                saved.getStatus(),
                saved.getComment(),
                cleanerName);
    }

    @Transactional
    public void deleteReservation(UUID id, UUID myID) {
        ReservationEntity reservation = reservationRepository.findReservationEntityByRid(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation == null || !reservation.getUser().getUid().equals(myID)) {
            throw new RuntimeException("Reservation not found or not owned by user.");
        }

        this.reservationRepository.deleteByRid(id);
    }
}
