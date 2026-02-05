package org.example.vehicleapi.vehicle;

import jakarta.persistence.*;
import lombok.*;
import org.example.vehicleapi.owner.Owner;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Vehicles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( length = 20, nullable = false)
    private String brand;
    @Column(length = 20)
    private String model;
    @Column(length = 4)
    private int year;

    @Column(length = 6, unique = true)
    private String plate;

    @Column(length = 17, unique = true)
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Version
    @Column(columnDefinition = "bigint default 0")
    private Long version;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModified;
}
