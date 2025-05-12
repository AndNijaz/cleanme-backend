package com.cleanme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationDto {
    private UUID id;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}