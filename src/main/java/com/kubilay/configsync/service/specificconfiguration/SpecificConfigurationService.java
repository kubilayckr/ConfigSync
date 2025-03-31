package com.kubilay.configsync.service.specificconfiguration;

import com.kubilay.configsync.model.request.specificconfiguration.SpecificConfigRequest;
import com.kubilay.configsync.model.response.specificconfiguration.SpecificConfigResponse;

import java.util.List;

public interface SpecificConfigurationService {

    String createSpecificConfig(SpecificConfigRequest request);

    SpecificConfigResponse getSpecificConfigByID(String id);

    void updateSpecificConfig(String id, SpecificConfigRequest request);

    void deleteSpecificConfig(String id);

    SpecificConfigResponse getAllSpecificConfigs();

    SpecificConfigResponse getAllSpecificConfigsByPageAndUrlAndHost(String page, String url, String host);

    List<String> getAllSpecificConfigIds();
}
