package org.example.vehicleapi.vehicle;

import jakarta.persistence.*;
import lombok.*;
import org.example.vehicleapi.owner.Owner;

@Data
@Entity
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
    @Column(length = 6)
    private String plate;
    @Column(length = 17)
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
