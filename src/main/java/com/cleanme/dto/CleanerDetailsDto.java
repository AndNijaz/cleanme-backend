package com.cleanme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CleanerDetailsDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal hourlyRate;
    private String availability;
    private String bio;
}