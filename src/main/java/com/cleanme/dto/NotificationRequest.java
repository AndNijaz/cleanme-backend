package com.cleanme.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationRequest {
    private UUID userId;
    private String message;
}