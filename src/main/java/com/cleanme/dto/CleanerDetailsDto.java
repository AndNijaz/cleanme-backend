package com.cleanme.dto;

import com.cleanme.dto.auth.CleanerSetupRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CleanerDetailsDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String servicesOffered;
    private BigDecimal hourlyRate;
    private List<Map<String, CleanerSetupRequest.TimeRange>> availability;
    private List<String> bio;
}