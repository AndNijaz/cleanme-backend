package com.cleanme.service;

import com.cleanme.dto.ReservationDto;
import com.cleanme.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationDto> getReservations(UUID id){
        return reservationRepository.findByUser_Uid(id).stream().map(r -> {

            String cleanerName = r.getCleaner() != null
                    ? r.getCleaner().getFirstName() + " " + r.getCleaner().getLastName()
                    : null;

            return new ReservationDto(
                    r.getDate(),
                    r.getTime(),
                    r.getLocation(),
                    r.getStatus(),
                    r.getComment(),
                    cleanerName
            );
        }).toList();

    }
}
