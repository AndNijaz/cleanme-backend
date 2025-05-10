package com.cleanme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FavouriteDto {
    private UUID cleanerId;
    private String cleanerName;
}

// Ako bude potrebe za vise informacija o cleaneru dodaj