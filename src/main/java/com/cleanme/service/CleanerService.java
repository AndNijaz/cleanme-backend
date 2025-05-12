package com.cleanme.service;

import com.cleanme.dto.*;
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
                details.getHourlyRate(),
                details.getAvailability(),
                details.getBio()
        );
    }

    public void updateCleanerDetails(UUID id, CleanerUpdateRequest request) {
        CleanerDetailsEntity details = cleanerRepository.findByCleaner_Uid(id)
                .orElseThrow(() -> new RuntimeException("Cleaner details not found"));

        details.setHourlyRate(request.getHourlyRate());
        details.setAvailability(request.getAvailability());
        details.setBio(request.getBio());

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
                            details.getAvailability().toLowerCase().contains(filter.getAvailability().toLowerCase());
                    return priceMatch && availabilityMatch;
                })
                .map(user -> {
                    var details = cleanerRepository.findByCleaner_Uid(user.getUid()).orElse(null);
                    return new CleanerDetailsDto(
                            user.getUid(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            details.getHourlyRate(),
                            details.getAvailability(),
                            details.getBio()
                    );
                })
                .toList();
    }

    public List<ReservationDto> getCleanerReservations(UUID id) {
        return reservationRepository.findByCleaner_Uid(id).stream()
                .map(reservation -> new ReservationDto(
                        reservation.getRid(),
                        reservation.getDate(),
                        reservation.getTime(),
                        reservation.getLocation(),
                        reservation.getStatus(),
                        reservation.getComment(),
                        reservation.getCleaner().getFirstName() + " " + reservation.getCleaner().getLastName()
                ))
                .toList();
    }
}
