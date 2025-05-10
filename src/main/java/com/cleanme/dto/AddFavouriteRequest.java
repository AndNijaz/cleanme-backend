package com.cleanme.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddFavouriteRequest {
    private UUID clientId;
    private UUID cleanerId;
}
