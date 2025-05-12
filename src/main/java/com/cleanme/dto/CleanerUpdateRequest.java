package com.cleanme.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CleanerUpdateRequest {
    private BigDecimal hourlyRate;
    private String availability;
    private String bio;
}