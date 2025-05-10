package com.cleanme.service;

import com.cleanme.dto.CreateReservationDto;
import com.cleanme.dto.ReservationDto;
import com.cleanme.dto.UpdateReservationDto;
import com.cleanme.entity.ReservationEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.repository.ReservationRepository;
import com.cleanme.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;
    private final UsersRepository usersRepository;

    public ReservationService(ReservationRepository reservationRepository, UsersRepository usersRepository) {
        this.reservationRepository = reservationRepository;
        this.usersRepository = usersRepository;
    }

    public List<ReservationDto> getReservations(UUID id){
        return reservationRepository.findByUser_Uid(id).stream().map(r -> {

            String cleanerName = r.getCleaner() != null
                    ? r.getCleaner().getFirstName() + " " + r.getCleaner().getLastName()
                    : null;

            return new ReservationDto(
                    r.getRid(),
                    r.getDate(),
                    r.getTime(),
                    r.getLocation(),
                    r.getStatus(),
                    r.getComment(),
                    cleanerName
            );
        }).toList();

    }

    public ReservationDto getReservation(UUID id){
        ReservationEntity reservation = reservationRepository.findReservationEntityByRid(id);

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
        UsersEntity user = usersRepository.findUsersEntityByUid(myId);
        UsersEntity cleaner = usersRepository.findUsersEntityByUid(dto.getCleanerID());

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

    public ReservationDto updateReservationDto(UUID id, UUID myID, UpdateReservationDto dto) {
        ReservationEntity reservation = reservationRepository.findReservationEntityByRid(id);
        UsersEntity cleaner = usersRepository.findUsersEntityByUid(dto.getCleanerId());

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
        ReservationEntity reservation = reservationRepository.findReservationEntityByRid(id);

        if (reservation == null || !reservation.getUser().getUid().equals(myID)) {
            throw new RuntimeException("Reservation not found or not owned by user.");
        }

        this.reservationRepository.deleteByRid(id);
    }
}
