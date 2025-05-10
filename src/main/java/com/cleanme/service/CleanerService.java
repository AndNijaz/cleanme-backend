package com.cleanme.service;

import com.cleanme.dto.CleanerSetupRequest;
import com.cleanme.entity.CleanerDetailsEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.enums.UserType;
import com.cleanme.repository.CleanerRepository;
import com.cleanme.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CleanerService {

    private final CleanerRepository cleanerRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void setupCleaner(CleanerSetupRequest request) {
        UUID cleanerId = request.getCleanerId();

        if (cleanerRepository.existsByCleaner_Uid(cleanerId)) {
            throw new RuntimeException("Cleaner details already exist.");
        }

        UsersEntity cleaner = usersRepository.findById(cleanerId)
                .orElseThrow(() -> new RuntimeException("Cleaner not found"));

        if (cleaner.getUserType() != UserType.CLEANER) {
            throw new RuntimeException("User is not a cleaner.");
        }

        CleanerDetailsEntity details = new CleanerDetailsEntity();
        details.setCleaner(cleaner);
        details.setServicesOffered(request.getServicesOffered());
        details.setHourlyRate(request.getHourlyRate());
        details.setAvailability(request.getAvailability());
        details.setBio(request.getBio());

        cleanerRepository.save(details);
    }
}
