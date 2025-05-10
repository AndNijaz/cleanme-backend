package com.cleanme.controller;

import com.cleanme.dto.ReservationDto;
import com.cleanme.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RequestMapping("/reservation")
@RestController()
public class ReservationController {

    private UUID myID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping("/my")
    public List<ReservationDto> getReservations(){
       return reservationService.getReservations(myID);
    }

}
