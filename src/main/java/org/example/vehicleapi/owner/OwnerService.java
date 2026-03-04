package org.example.vehicleapi.owner;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PasswordEncoder passwordEncoder;

    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        if (ownerRepository.findByEmail(ownerDTO.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner.setPassword(passwordEncoder.encode(ownerDTO.password()));

        Owner save = ownerRepository.save(owner);
        return ownerMapper.toDTO(save);
    }

    public String login(OwnerDTO loginRequest) {
        Owner owner = ownerRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Use the injected encoder to verify
        if (!passwordEncoder.matches(loginRequest.password(), owner.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return "Login successful! (Ready for JWT)";
    }


    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream().map(ownerMapper::toDTO).toList();
    }

    public OwnerDTO getOwnerInfo(long ownerId) {
        return ownerRepository.findById(ownerId).map(ownerMapper::toDetailedDTO).orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));
    }

}
