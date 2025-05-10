package com.cleanme.controller;

import com.cleanme.dto.CreateReservationDto;
import com.cleanme.dto.ReservationDto;
import com.cleanme.dto.UpdateReservationDto;
import com.cleanme.entity.ReservationEntity;
import com.cleanme.service.ReservationService;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ReservationDto getReservation(@PathVariable UUID id){
        return this.reservationService.getReservation(id);
    }

    @PostMapping()
    public ResponseEntity<ReservationDto> createReservation(@RequestBody CreateReservationDto dto){

        ReservationDto reservation = reservationService.createReservation(myID, dto);

        return ResponseEntity.status(201).body(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable UUID id, @RequestBody UpdateReservationDto dto){

        ReservationDto updateReservationDto = reservationService.updateReservationDto(id, myID, dto);

        return ResponseEntity.ok(updateReservationDto);
    }
}
