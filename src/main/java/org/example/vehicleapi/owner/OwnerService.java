package org.example.vehicleapi.owner;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);

        Owner save = ownerRepository.save(owner);
        return ownerMapper.toDTO(save);
    }


    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream().map(ownerMapper::toDTO).toList();
    }

    public OwnerDTO getOwnerInfo(long ownerId) {
        return ownerRepository.findById(ownerId).map(ownerMapper::toDetailedDTO).orElse(null);
    }

}
