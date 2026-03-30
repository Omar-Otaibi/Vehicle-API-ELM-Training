package org.example.vehicleapi.owner;

import lombok.RequiredArgsConstructor;

import org.example.vehicleapi.config.JwtService;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnerService implements UserDetailsService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

        return jwtService.generateToken(owner.getEmail());
    }

    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream().map(ownerMapper::toDTO).toList();
    }

    public OwnerDTO getOwnerInfo() {
        // Get the email that was extracted from the JWT token
        String tokenEmail = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        // Look up the database using that exact email
        return ownerRepository.findByEmail(tokenEmail)
                .map(ownerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Logged in user not found in the database"));
    }

    @NullMarked
    @Override
    public UserDetails loadUserByUsername(String email) throws  UsernameNotFoundException {
        // Find the owner in the database
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Convert your Owner into a Spring Security User object
        return User.builder()
                .username(owner.getEmail())
                .password(owner.getPassword())
                .roles("USER")
                .build();
    }
}
