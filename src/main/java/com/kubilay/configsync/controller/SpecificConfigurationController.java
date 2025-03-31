package com.kubilay.configsync.controller;

import com.kubilay.configsync.model.request.specificconfiguration.SpecificConfigRequest;
import com.kubilay.configsync.model.response.specificconfiguration.SpecificConfigResponse;
import com.kubilay.configsync.service.specificconfiguration.SpecificConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specific-configurations")
@RequiredArgsConstructor
public class SpecificConfigurationController {

    private final SpecificConfigurationService specificConfigurationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createSpecificConfig(@Valid @RequestBody SpecificConfigRequest request) {
        return new ResponseEntity<>(specificConfigurationService.createSpecificConfig(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SpecificConfigResponse> getSpecificConfigByID(@PathVariable String id) {
        return new ResponseEntity<>(specificConfigurationService.getSpecificConfigByID(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateSpecificConfig(@PathVariable String id, @Valid @RequestBody SpecificConfigRequest request) {
        specificConfigurationService.updateSpecificConfig(id, request);
        return new ResponseEntity<>((Object) null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteSpecificConfig(@PathVariable String id) {
        specificConfigurationService.deleteSpecificConfig(id);
        return new ResponseEntity<>((Object) null, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SpecificConfigResponse> getAllSpecificConfigs() {
        return new ResponseEntity<>(specificConfigurationService.getAllSpecificConfigs(), HttpStatus.OK);
    }

    @GetMapping("/specific")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SpecificConfigResponse> getAllSpecificConfigsByPageAndUrlAndHost(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String host) {
        return new ResponseEntity<>(specificConfigurationService.getAllSpecificConfigsByPageAndUrlAndHost(page, url, host), HttpStatus.OK);
    }

    @GetMapping("/specific-config-ids")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<String>> getAllSpecificConfigIds() {
        return new ResponseEntity<>(specificConfigurationService.getAllSpecificConfigIds(), HttpStatus.OK);
    }
}
