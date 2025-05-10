package com.cleanme.dto;

import com.cleanme.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
public class ReservationDto {
    private UUID rid;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private ReservationStatus status;
    private String comment;
    private String cleanerName;

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getCleanerName() {
        return cleanerName;
    }

    public UUID getRid() {
        return rid;
    }
}
