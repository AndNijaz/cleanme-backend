package com.cleanme.utilities;

import com.cleanme.entity.UsersEntity;
import com.cleanme.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UsersRepository usersRepository;

    public UUID extractUserId(Authentication auth) {
        String email = auth.getName();
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUid();
    }
}
