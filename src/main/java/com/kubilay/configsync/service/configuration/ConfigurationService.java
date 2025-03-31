package com.kubilay.configsync.service.configuration;

import com.kubilay.configsync.model.request.configuration.ConfigRequest;
import com.kubilay.configsync.model.response.configuration.ConfigResponse;

import java.util.List;

public interface ConfigurationService {

    String createConfig(ConfigRequest request);

    ConfigResponse getConfigByID(String id);

    void updateConfig(String id, ConfigRequest request);

    void deleteConfig(String id);

    ConfigResponse getAllConfigs();

    List<String> getAllConfigIds();
}
