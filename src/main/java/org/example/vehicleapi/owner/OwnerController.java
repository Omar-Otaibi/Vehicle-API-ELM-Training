package org.example.vehicleapi.owner;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody OwnerDTO loginRequest) {
        // Since we are reusing OwnerDTO for login, we only care about email and password fields here
        String tokenResponse = ownerService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/getAllOwners")
    public ResponseEntity<List<OwnerDTO>> getAllOwners() {
        List<OwnerDTO> savedOwners = ownerService.getAllOwners();
        return new ResponseEntity<>(savedOwners, HttpStatus.OK);
    }

    @GetMapping("/api/getOwnerInfo")
    public ResponseEntity<OwnerDTO> getOwnerInfo() {
        OwnerDTO ownerInfo = ownerService.getOwnerInfo();
        return new ResponseEntity<>(ownerInfo, HttpStatus.OK);
    }
}
