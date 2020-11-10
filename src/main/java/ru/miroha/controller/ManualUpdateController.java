package ru.miroha.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.miroha.service.ScheduledGooglePlayGameUpdateService;

/**
 * Controller allows to update library manually without waiting for a scheduled start.
 */
@RestController
@RequestMapping("/update")
@Slf4j
public class ManualUpdateController {

    private final ScheduledGooglePlayGameUpdateService scheduledGooglePlayGameUpdateService;

    public ManualUpdateController(ScheduledGooglePlayGameUpdateService scheduledGooglePlayGameUpdateService) {
        this.scheduledGooglePlayGameUpdateService = scheduledGooglePlayGameUpdateService;
    }

    @GetMapping
    public ResponseEntity<String> startManualUpdate() {
        log.info("Manual update started.");
        scheduledGooglePlayGameUpdateService.updateLibraryOnSchedule();
        log.info("Manual update finished.");
        return ResponseEntity.ok("Update finished.");
    }
}
