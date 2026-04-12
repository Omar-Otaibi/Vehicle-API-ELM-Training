package org.example.vehicleapi.vehicle.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vehicleapi.vehicle.integration.ExternalVehicleDataDTO;
import org.example.vehicleapi.vehicle.integration.RestTemplateTestService;
import org.example.vehicleapi.vehicle.repo.VehicleRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleScheduledJobs {

    private final VehicleRepository vehicleRepository;
    private final RestTemplateTestService externalService;

    //CLEAR OLD VEHICLES
    @Async("jobExecutor")
    @Scheduled(cron = "${vehicle.jobs.clear-old.cron}")
    @Transactional
    public void clearOldVehicles() {
        log.info("Starting scheduled job: Clearing vehicles older than 2005...");

        vehicleRepository.deleteVehiclesOlderThan(2020);

        log.info("Finished clearing old vehicles.");
    }

    //SYNC EXTERNAL DATA
    @Async("jobExecutor")
    @Scheduled(cron = "${vehicle.jobs.sync.cron}")
    @Transactional
    public void syncExternalData() {
        log.info("Starting scheduled job: Syncing external vehicle data...");

        // Fetch the list using your existing service
        List<ExternalVehicleDataDTO> externalDataList = externalService.fetchExternalData();

        for (ExternalVehicleDataDTO data : externalDataList) {
            try {
                Long vehicleId = Long.parseLong(data.id());

                // If the vehicle exists in our DB, update it with the new price & status
                vehicleRepository.findById(vehicleId).ifPresent(vehicle -> {
                    vehicle.setPrice(data.price());
                    vehicle.setStatus(data.status());
                    vehicleRepository.save(vehicle);
                });

            } catch (NumberFormatException e) {
                log.warn("Skipping invalid MockAPI ID: {}", data.id());
            }
        }
        log.info("Finished syncing external data.");
    }
}