package org.example.vehicleapi.owner;

import jakarta.persistence.*;
import lombok.Data;
import org.example.vehicleapi.vehicle.Vehicles;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Vehicles> vehicles = new ArrayList<>();
}
