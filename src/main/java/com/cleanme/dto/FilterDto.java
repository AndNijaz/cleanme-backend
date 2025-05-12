package com.cleanme.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FilterDto {
    private BigDecimal minRate;
    private BigDecimal maxRate;
    private String availability;
}