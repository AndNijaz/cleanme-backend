package com.cleanme.dto.auth;

import com.cleanme.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UUID userId;
    private String firstName;
    private String lastName;
    private UserType userType;
}
