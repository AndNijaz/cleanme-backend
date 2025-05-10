package com.cleanme.controller;

import com.cleanme.dto.AuthResponse;
import com.cleanme.dto.CleanerSetupRequest;
import com.cleanme.dto.LoginRequest;
import com.cleanme.dto.RegisterRequest;
import com.cleanme.service.AuthService;
import com.cleanme.service.CleanerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CleanerService cleanerService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/cleaner-setup")
    public ResponseEntity<Void> setupCleaner(@RequestBody @Valid CleanerSetupRequest request) {
        cleanerService.setupCleaner(request);
        return ResponseEntity.ok().build();
    }

}
