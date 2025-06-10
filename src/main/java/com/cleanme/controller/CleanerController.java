package com.cleanme.controller;

import com.cleanme.dto.CleanerDetailsDto;
import com.cleanme.dto.CleanerUpdateRequest;
import com.cleanme.dto.FilterDto;
import com.cleanme.dto.ReservationDto;
import com.cleanme.service.CleanerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/cleaners")
@RequiredArgsConstructor
public class CleanerController {

    private final CleanerService cleanerService;

    @GetMapping
    public ResponseEntity<List<CleanerDetailsDto>> getAllCleaners() {
        return ResponseEntity.ok(cleanerService.getAllCleaners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CleanerDetailsDto> getCleanerById(@PathVariable UUID id) {
        try {
            CleanerDetailsDto cleaner = cleanerService.getCleanerById(id);
            return ResponseEntity.ok(cleaner);
        } catch (RuntimeException e) {
            // Return 404 if cleaner details not found
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCleaner(@PathVariable UUID id, @RequestBody CleanerUpdateRequest request) {
        try {
            cleanerService.updateCleanerDetails(id, request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // Return 404 if cleaner details not found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CleanerDetailsDto>> filterCleaners(@RequestBody FilterDto filter) {
        return ResponseEntity.ok(cleanerService.filterCleaners(filter));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDto>> getCleanerReservations(@PathVariable UUID id) {
        return ResponseEntity.ok(cleanerService.getCleanerReservations(id));
    }
}