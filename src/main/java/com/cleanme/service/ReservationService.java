package com.cleanme.service;

import com.cleanme.dto.ReservationDto;
import com.cleanme.entity.ReservationEntity;
import com.cleanme.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

            String cleanerName = "i am cryingdgit branch";

            return new ReservationDto(
                    "r.getDate()",
                    "r.getTime()",
                    "r.getLocation()",
                    "r.getStatus()",
                    "r.getComment()",
                    cleanerName
            );
        }).toList();

    }
}
