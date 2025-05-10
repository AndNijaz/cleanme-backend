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


    private UUID myID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }



    @GetMapping("/my")
    public List<ReservationDto> getReservations(){
       return reservationService.getReservations(myID);
    }

}
