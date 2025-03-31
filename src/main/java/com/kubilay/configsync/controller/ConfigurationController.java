package com.kubilay.configsync.controller;

import com.kubilay.configsync.model.request.configuration.ConfigRequest;
import com.kubilay.configsync.model.response.configuration.ConfigResponse;
import com.kubilay.configsync.service.configuration.ConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configurations")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createConfig(@Valid @RequestBody ConfigRequest request) {
        return new ResponseEntity<>(configurationService.createConfig(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ConfigResponse> getConfigByID(@PathVariable String id) {
        return new ResponseEntity<>(configurationService.getConfigByID(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateConfig(@PathVariable String id, @Valid @RequestBody ConfigRequest request) {
        configurationService.updateConfig(id, request);
        return new ResponseEntity<>((Object) null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteConfig(@PathVariable String id) {
        configurationService.deleteConfig(id);
        return new ResponseEntity<>((Object) null, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ConfigResponse> getAllConfigs() {
        return new ResponseEntity<>(configurationService.getAllConfigs(), HttpStatus.OK);
    }

    @GetMapping("/config-ids")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<String>> getAllConfigIds() {
        return new ResponseEntity<>(configurationService.getAllConfigIds(), HttpStatus.OK);
    }
}
