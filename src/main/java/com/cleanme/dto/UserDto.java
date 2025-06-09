package com.cleanme.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String street;
    private String streetExtra;
    // Add other fields as needed
} 