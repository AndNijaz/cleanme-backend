package com.cleanme.entity;

import com.cleanme.dto.auth.CleanerSetupRequest;
import com.cleanme.utilities.AvailabilityConverter;
import com.cleanme.utilities.BioConverter;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "cleaner_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleanerDetailsEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "cleaner_id", referencedColumnName = "UID", nullable = false)
    private UsersEntity cleaner;

    @Column(name = "services_offered")
    private String servicesOffered;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "availability")
    @Convert(converter = AvailabilityConverter.class)
    private List<Map<String, CleanerSetupRequest.TimeRange>> availability;

    @Column(name = "bio")
    @Convert(converter = BioConverter.class)
    private List<String> bio;
}
