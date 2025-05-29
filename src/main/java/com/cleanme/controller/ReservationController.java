package com.cleanme.controller;

import com.cleanme.dto.CreateReservationDto;
import com.cleanme.dto.ReservationDto;
import com.cleanme.dto.UpdateReservationDto;
import com.cleanme.service.ReservationService;
import com.cleanme.utilities.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    // TODO: replace with authenticated user ID when security is implemented
    private final UUID myID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private final ReservationService reservationService;
    private final SecurityUtils securityUtils;

    @GetMapping("/all")
    public List<ReservationDto> getReservations(Authentication auth){
        UUID userId = securityUtils.extractUserId(auth);
        return reservationService.getReservations(userId);
    }

    @GetMapping("/{id}")
    public ReservationDto getReservation(@PathVariable UUID id){
        return this.reservationService.getReservation(id);
    }

    @PostMapping()
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody CreateReservationDto dto){
        ReservationDto reservation = reservationService.createReservation(myID, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable UUID id, @Valid @RequestBody UpdateReservationDto dto){
        ReservationDto updateReservationDto = reservationService.updateReservationDto(id, myID, dto);
        return ResponseEntity.ok(updateReservationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id){
        reservationService.deleteReservation(id, myID);
        return ResponseEntity.noContent().build();
    }
}
