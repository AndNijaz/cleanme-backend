package com.cleanme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(
        name = "favourites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "cleaner_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private UsersEntity client;

    @ManyToOne
    @JoinColumn(name = "cleaner_id", nullable = false)
    private UsersEntity cleaner;
}
