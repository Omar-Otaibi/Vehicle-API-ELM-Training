package org.example.vehicleapi.owner;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/createOwner")
    public ResponseEntity<OwnerDTO> createOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
        OwnerDTO savedOwner = ownerService.createOwner(ownerDTO);
        return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
    }

    @GetMapping("/getAllOwners")
    public ResponseEntity<List<OwnerDTO>> getAllOwners() {
        List<OwnerDTO> savedOwners = ownerService.getAllOwners();
        return new ResponseEntity<>(savedOwners, HttpStatus.OK);
    }
}
