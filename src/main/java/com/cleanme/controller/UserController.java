package com.cleanme.controller;

import com.cleanme.dto.UserDto;
import com.cleanme.service.UserService;
import com.cleanme.utilities.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SecurityUtils securityUtils;

    public UserController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        UUID userId = securityUtils.extractUserId(authentication);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(Authentication authentication, @RequestBody UserDto userDto) {
        UUID userId = securityUtils.extractUserId(authentication);
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }
} 