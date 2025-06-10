package com.cleanme.service;

import com.cleanme.dto.*;
import com.cleanme.dto.CleanerDetailsDto;
import com.cleanme.dto.auth.CleanerSetupRequest;
import com.cleanme.entity.CleanerDetailsEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.enums.UserType;
import com.cleanme.repository.CleanerRepository;
import com.cleanme.repository.ReservationRepository;
import com.cleanme.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CleanerService {

    private final CleanerRepository cleanerRepository;
    private final UsersRepository usersRepository;
    private final ReservationRepository reservationRepository;

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

    public List<CleanerDetailsDto> getAllCleaners() {
        return usersRepository.findByUserType(UserType.CLEANER).stream()
                .map(user -> {
                    var details = cleanerRepository.findByCleaner_Uid(user.getUid()).orElse(null);
                    return new CleanerDetailsDto(
                            user.getUid(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            details != null ? details.getServicesOffered() : null,
                            details != null ? details.getHourlyRate() : null,
                            details != null ? details.getAvailability() : null,
                            details != null ? details.getBio() : null
                    );
                })
                .toList();
    }

    public CleanerDetailsDto getCleanerById(UUID id) {
        UsersEntity user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cleaner not found"));

        if (user.getUserType() != UserType.CLEANER) {
            throw new RuntimeException("User is not a cleaner");
        }

        CleanerDetailsEntity details = cleanerRepository.findByCleaner_Uid(id)
                .orElseThrow(() -> new RuntimeException("Cleaner details not found"));

        return new CleanerDetailsDto(
                user.getUid(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                details.getServicesOffered(),
                details.getHourlyRate(),
                details.getAvailability(),
                details.getBio()
        );
    }

    public void updateCleanerDetails(UUID id, CleanerUpdateRequest request) {
        // Find the user first to validate they exist and are a cleaner
        UsersEntity cleaner = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cleaner.getUserType() != UserType.CLEANER) {
            throw new RuntimeException("User is not a cleaner");
        }

        // Find existing cleaner details or create new ones
        CleanerDetailsEntity details = cleanerRepository.findByCleaner_Uid(id)
                .orElse(new CleanerDetailsEntity());

        // If this is a new cleaner details entity, set the cleaner reference
        if (details.getCleaner() == null) {
            details.setCleaner(cleaner);
        }

        // Update fields conditionally
        if (request.getServicesOffered() != null) {
            details.setServicesOffered(request.getServicesOffered());
        }
        if (request.getHourlyRate() != null) {
            details.setHourlyRate(request.getHourlyRate());
        }
        if (request.getAvailability() != null) {
            details.setAvailability(request.getAvailability());
        }
        if (request.getBio() != null) {
            details.setBio(request.getBio());
        }

        cleanerRepository.save(details);
    }

    public List<CleanerDetailsDto> filterCleaners(FilterDto filter) {
        return usersRepository.findByUserType(UserType.CLEANER).stream()
                .filter(user -> {
                    var details = cleanerRepository.findByCleaner_Uid(user.getUid()).orElse(null);
                    if (details == null) return false;
                    boolean priceMatch = filter.getMinRate() == null || filter.getMaxRate() == null ||
                            (details.getHourlyRate().compareTo(filter.getMinRate()) >= 0 &&
                                    details.getHourlyRate().compareTo(filter.getMaxRate()) <= 0);
                    boolean availabilityMatch = filter.getAvailability() == null ||
                            details.getAvailability().stream().anyMatch(map ->
                                    map.keySet().stream().anyMatch(day ->
                                            day.equalsIgnoreCase(filter.getAvailability())
                                    )
                            );
                    return priceMatch && availabilityMatch;
                })
                .map(user -> {
                    var details = cleanerRepository.findByCleaner_Uid(user.getUid()).orElse(null);
                    return new CleanerDetailsDto(
                            user.getUid(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            details.getServicesOffered(),
                            details.getHourlyRate(),
                            details.getAvailability(),
                            details.getBio()
                    );
                })
                .toList();
    }

    public List<ReservationDto> getCleanerReservations(UUID id) {
        return reservationRepository.findByCleaner_Uid(id).stream()
                .map(reservation -> {
                    String cleanerName = reservation.getCleaner() != null
                            ? reservation.getCleaner().getFirstName() + " " + reservation.getCleaner().getLastName()
                            : null;
                    
                    String clientName = reservation.getUser() != null
                            ? reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName()
                            : null;
                    
                    String clientPhone = reservation.getUser() != null
                            ? reservation.getUser().getPhone()
                            : null;
                    
                    return new ReservationDto(
                            reservation.getRid(),
                            reservation.getDate(),
                            reservation.getTime(),
                            reservation.getLocation(),
                            reservation.getStatus(),
                            reservation.getComment(),
                            cleanerName,
                            clientName,
                            clientPhone
                    );
                })
                .toList();
    }
}
