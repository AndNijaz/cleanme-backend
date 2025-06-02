package com.cleanme.service;

import com.cleanme.dto.CleanerUpdateRequest;
import com.cleanme.dto.FilterDto;
import com.cleanme.dto.ReservationDto;
import com.cleanme.dto.auth.CleanerSetupRequest;
import com.cleanme.entity.CleanerDetailsEntity;
import com.cleanme.entity.ReservationEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.enums.ReservationStatus;
import com.cleanme.enums.UserType;
import com.cleanme.repository.CleanerRepository;
import com.cleanme.repository.ReservationRepository;
import com.cleanme.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CleanerServiceTest {

    @Mock
    private CleanerRepository cleanerRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CleanerService cleanerService;

    private UUID cleanerId;
    private UsersEntity cleanerUser;

    @BeforeEach
    void setUp() {
        cleanerId = UUID.randomUUID();

        cleanerUser = new UsersEntity();
        cleanerUser.setUid(cleanerId);
        cleanerUser.setUserType(UserType.CLEANER);
        cleanerUser.setFirstName("Jane");
        cleanerUser.setLastName("Doe");
        cleanerUser.setEmail("jane.doe@example.com");
    }

    @Test
    void setupCleaner_successful() {
        CleanerSetupRequest.TimeRange range = new CleanerSetupRequest.TimeRange();
        range.setFrom("09:00");
        range.setTo("17:00");

        CleanerSetupRequest request = new CleanerSetupRequest(
                cleanerId,
                "Window Cleaning",
                BigDecimal.valueOf(25),
                List.of(Map.of("Monday", range)),
                List.of("Experienced cleaner")
        );

        when(cleanerRepository.existsByCleaner_Uid(cleanerId)).thenReturn(false);
        when(usersRepository.findById(cleanerId)).thenReturn(Optional.of(cleanerUser));

        cleanerService.setupCleaner(request);

        verify(cleanerRepository).save(any(CleanerDetailsEntity.class));
    }


    @Test
    void setupCleaner_throwsWhenCleanerAlreadyExists() {
        CleanerSetupRequest request = new CleanerSetupRequest();
        request.setCleanerId(cleanerId);

        when(cleanerRepository.existsByCleaner_Uid(cleanerId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> cleanerService.setupCleaner(request));
        verify(cleanerRepository, never()).save(any());
    }

    @Test
    void setupCleaner_throwsWhenUserNotFound() {
        CleanerSetupRequest request = new CleanerSetupRequest();
        request.setCleanerId(cleanerId);

        when(cleanerRepository.existsByCleaner_Uid(cleanerId)).thenReturn(false);
        when(usersRepository.findById(cleanerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cleanerService.setupCleaner(request));
    }

    @Test
    void setupCleaner_throwsWhenUserIsNotCleaner() {
        CleanerSetupRequest request = new CleanerSetupRequest();
        request.setCleanerId(cleanerId);

        UsersEntity user = new UsersEntity();
        user.setUid(cleanerId);
        user.setUserType(UserType.CLIENT);

        when(cleanerRepository.existsByCleaner_Uid(cleanerId)).thenReturn(false);
        when(usersRepository.findById(cleanerId)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> cleanerService.setupCleaner(request));
    }

    @Test
    void getAllCleaners_returnsAllCleanersWithDetails() {
        UsersEntity cleaner1 = new UsersEntity();
        cleaner1.setUid(UUID.randomUUID());
        cleaner1.setFirstName("John");
        cleaner1.setLastName("Smith");
        cleaner1.setEmail("john@clean.com");
        cleaner1.setUserType(UserType.CLEANER);

        CleanerDetailsEntity details1 = new CleanerDetailsEntity();
        details1.setCleaner(cleaner1);
        details1.setHourlyRate(BigDecimal.valueOf(20));

        CleanerSetupRequest.TimeRange range = new CleanerSetupRequest.TimeRange();
        range.setFrom("09:00");
        range.setTo("17:00");

        Map<String, CleanerSetupRequest.TimeRange> availability = Map.of("Monday", range);
        details1.setAvailability(List.of(availability));
        details1.setBio(List.of("Bio"));

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner1));
        when(cleanerRepository.findByCleaner_Uid(cleaner1.getUid())).thenReturn(Optional.of(details1));

        var result = cleanerService.getAllCleaners();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals(BigDecimal.valueOf(20), result.get(0).getHourlyRate());
    }


    @Test
    void getAllCleaners_handlesCleanersWithoutDetails() {
        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(UUID.randomUUID());
        cleaner.setFirstName("Alice");
        cleaner.setLastName("Johnson");
        cleaner.setEmail("alice@clean.com");
        cleaner.setUserType(UserType.CLEANER);

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(cleaner.getUid())).thenReturn(Optional.empty());

        var result = cleanerService.getAllCleaners();

        assertEquals(1, result.size());
        assertNull(result.get(0).getHourlyRate());
        assertNull(result.get(0).getAvailability());
        assertNull(result.get(0).getBio());
    }

    @Test
    void getCleanerById_returnsValidCleanerDetails() {
        UUID id = UUID.randomUUID();

        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(id);
        cleaner.setFirstName("Sara");
        cleaner.setLastName("Lee");
        cleaner.setEmail("sara@clean.com");
        cleaner.setUserType(UserType.CLEANER);

        CleanerDetailsEntity details = new CleanerDetailsEntity();
        details.setCleaner(cleaner);
        details.setHourlyRate(BigDecimal.valueOf(30));

        CleanerSetupRequest.TimeRange range = new CleanerSetupRequest.TimeRange();
        range.setFrom("10:00");
        range.setTo("18:00");

        details.setAvailability(List.of(Map.of("Tuesday", range)));
        details.setBio(List.of("Fast and reliable"));

        when(usersRepository.findById(id)).thenReturn(Optional.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(id)).thenReturn(Optional.of(details));

        var result = cleanerService.getCleanerById(id);

        assertEquals("Sara", result.getFirstName());
        assertEquals(BigDecimal.valueOf(30), result.getHourlyRate());
        assertEquals("Fast and reliable", result.getBio().get(0));
    }

    @Test
    void getCleanerById_throwsWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cleanerService.getCleanerById(id));
    }

    @Test
    void getCleanerById_throwsWhenUserIsNotCleaner() {
        UUID id = UUID.randomUUID();

        UsersEntity user = new UsersEntity();
        user.setUid(id);
        user.setUserType(UserType.CLIENT);

        when(usersRepository.findById(id)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> cleanerService.getCleanerById(id));
    }

    @Test
    void getCleanerById_throwsWhenCleanerDetailsNotFound() {
        UUID id = UUID.randomUUID();

        UsersEntity user = new UsersEntity();
        user.setUid(id);
        user.setUserType(UserType.CLEANER);

        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        when(cleanerRepository.findByCleaner_Uid(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cleanerService.getCleanerById(id));
    }

    @Test
    void updateCleanerDetails_successfulUpdate() {
        UUID cleanerId = UUID.randomUUID();

        CleanerDetailsEntity existingDetails = new CleanerDetailsEntity();
        existingDetails.setId(UUID.randomUUID());

        CleanerUpdateRequest request = new CleanerUpdateRequest();
        request.setHourlyRate(BigDecimal.valueOf(40));

        CleanerSetupRequest.TimeRange range = new CleanerSetupRequest.TimeRange();
        range.setFrom("08:00");
        range.setTo("16:00");

        request.setAvailability(List.of(Map.of("Wednesday", range)));
        request.setBio(List.of("Updated bio"));

        when(cleanerRepository.findByCleaner_Uid(cleanerId)).thenReturn(Optional.of(existingDetails));

        cleanerService.updateCleanerDetails(cleanerId, request);

        assertEquals(BigDecimal.valueOf(40), existingDetails.getHourlyRate());
        assertEquals("Updated bio", existingDetails.getBio().get(0));
        verify(cleanerRepository).save(existingDetails);
    }

    @Test
    void updateCleanerDetails_throwsWhenCleanerNotFound() {
        UUID cleanerId = UUID.randomUUID();

        CleanerUpdateRequest request = new CleanerUpdateRequest();
        request.setHourlyRate(BigDecimal.valueOf(30));

        when(cleanerRepository.findByCleaner_Uid(cleanerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cleanerService.updateCleanerDetails(cleanerId, request));
    }

    @Test
    void filterCleaners_filtersByPriceOnly() {
        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(UUID.randomUUID());
        cleaner.setFirstName("Marko");
        cleaner.setLastName("Matic");
        cleaner.setEmail("marko@clean.com");
        cleaner.setUserType(UserType.CLEANER);

        CleanerDetailsEntity details = new CleanerDetailsEntity();
        details.setCleaner(cleaner);
        details.setHourlyRate(BigDecimal.valueOf(20));
        details.setAvailability(Collections.emptyList());
        details.setBio(List.of("Bio"));

        FilterDto filter = new FilterDto();
        filter.setMinRate(BigDecimal.valueOf(15));
        filter.setMaxRate(BigDecimal.valueOf(25));

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(cleaner.getUid())).thenReturn(Optional.of(details));

        var result = cleanerService.filterCleaners(filter);

        assertEquals(1, result.size());
        assertEquals("Marko", result.get(0).getFirstName());
    }

    @Test
    void filterCleaners_filtersByAvailabilityOnly() {
        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(UUID.randomUUID());
        cleaner.setUserType(UserType.CLEANER);

        CleanerSetupRequest.TimeRange range = new CleanerSetupRequest.TimeRange();
        range.setFrom("09:00");
        range.setTo("17:00");

        CleanerDetailsEntity details = new CleanerDetailsEntity();
        details.setCleaner(cleaner);
        details.setHourlyRate(BigDecimal.valueOf(100)); // Out of price range
        details.setAvailability(List.of(Map.of("Friday", range)));
        details.setBio(List.of("Bio"));

        FilterDto filter = new FilterDto();
        filter.setAvailability("Friday");

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(cleaner.getUid())).thenReturn(Optional.of(details));

        var result = cleanerService.filterCleaners(filter);

        assertEquals(1, result.size());
    }

    @Test
    void filterCleaners_filtersByPriceAndAvailability() {
        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(UUID.randomUUID());
        cleaner.setUserType(UserType.CLEANER);

        CleanerSetupRequest.TimeRange range = new CleanerSetupRequest.TimeRange();
        range.setFrom("10:00");
        range.setTo("14:00");

        CleanerDetailsEntity details = new CleanerDetailsEntity();
        details.setCleaner(cleaner);
        details.setHourlyRate(BigDecimal.valueOf(22));
        details.setAvailability(List.of(Map.of("Saturday", range)));
        details.setBio(List.of("Bio"));

        FilterDto filter = new FilterDto();
        filter.setMinRate(BigDecimal.valueOf(20));
        filter.setMaxRate(BigDecimal.valueOf(25));
        filter.setAvailability("Saturday");

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(cleaner.getUid())).thenReturn(Optional.of(details));

        var result = cleanerService.filterCleaners(filter);

        assertEquals(1, result.size());
    }

    @Test
    void filterCleaners_skipsCleanersWithNoDetails() {
        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(UUID.randomUUID());
        cleaner.setUserType(UserType.CLEANER);

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(cleaner.getUid())).thenReturn(Optional.empty());

        var result = cleanerService.filterCleaners(new FilterDto());

        assertTrue(result.isEmpty());
    }

    @Test
    void filterCleaners_returnsEmptyWhenNoMatch() {
        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(UUID.randomUUID());
        cleaner.setUserType(UserType.CLEANER);

        CleanerDetailsEntity details = new CleanerDetailsEntity();
        details.setCleaner(cleaner);
        details.setHourlyRate(BigDecimal.valueOf(50));
        details.setAvailability(List.of());

        FilterDto filter = new FilterDto();
        filter.setMinRate(BigDecimal.valueOf(10));
        filter.setMaxRate(BigDecimal.valueOf(20));
        filter.setAvailability("Sunday");

        when(usersRepository.findByUserType(UserType.CLEANER)).thenReturn(List.of(cleaner));
        when(cleanerRepository.findByCleaner_Uid(cleaner.getUid())).thenReturn(Optional.of(details));

        var result = cleanerService.filterCleaners(filter);

        assertTrue(result.isEmpty());
    }

    @Test
    void getCleanerReservations_returnsMappedReservations() {
        UUID cleanerId = UUID.randomUUID();

        UsersEntity cleaner = new UsersEntity();
        cleaner.setUid(cleanerId);
        cleaner.setFirstName("Ena");
        cleaner.setLastName("Kovač");

        ReservationEntity reservation = new ReservationEntity();
        reservation.setRid(UUID.randomUUID());
        reservation.setCleaner(cleaner);
        reservation.setDate(LocalDate.of(2025, 6, 1));
        reservation.setTime(LocalTime.of(10, 30));
        reservation.setLocation("Address 1");
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setComment("Please be on time");

        when(reservationRepository.findByCleaner_Uid(cleanerId)).thenReturn(List.of(reservation));

        var result = cleanerService.getCleanerReservations(cleanerId);

        assertEquals(1, result.size());
        var dto = result.get(0);
        assertEquals("Ena Kovač", dto.getCleanerName());
        assertEquals("Please be on time", dto.getComment());
        assertEquals(LocalTime.of(10, 30), dto.getTime());
    }

    @Test
    void getCleanerReservations_returnsEmptyListWhenNoneFound() {
        UUID cleanerId = UUID.randomUUID();

        when(reservationRepository.findByCleaner_Uid(cleanerId)).thenReturn(Collections.emptyList());

        var result = cleanerService.getCleanerReservations(cleanerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
