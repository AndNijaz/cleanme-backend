package com.cleanme.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CleanerSetupRequest {

    @NotNull
    private UUID cleanerId;

    @NotBlank
    private String servicesOffered;

    @NotNull
    private BigDecimal hourlyRate;

    @NotEmpty
    @Valid
    private List<Map<String, TimeRange>> availability;

    @NotEmpty
    @Valid
    private List<String> bio;

    @Data
    public static class TimeRange {
        private String from;
        private String to;
    }
}

