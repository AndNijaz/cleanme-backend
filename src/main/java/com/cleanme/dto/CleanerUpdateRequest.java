package com.cleanme.dto;

import com.cleanme.dto.auth.CleanerSetupRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CleanerUpdateRequest {
    private BigDecimal hourlyRate;
    private List<Map<String, CleanerSetupRequest.TimeRange>> availability;
    private List<String> bio;

}