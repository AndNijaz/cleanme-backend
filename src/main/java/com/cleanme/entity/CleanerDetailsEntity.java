package com.cleanme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
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
    private String availability;

    @Column(name = "bio")
    private String bio;
}
