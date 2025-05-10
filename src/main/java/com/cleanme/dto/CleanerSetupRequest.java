package com.cleanme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CleanerSetupRequest {

    @NotNull
    private UUID cleanerId;

    @NotBlank
    private String servicesOffered;

    @NotNull
    private BigDecimal hourlyRate;

    @NotBlank
    private String availability;

    @NotBlank
    private String bio;
}
