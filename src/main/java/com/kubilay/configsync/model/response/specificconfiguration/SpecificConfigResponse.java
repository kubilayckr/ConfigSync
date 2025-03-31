package com.kubilay.configsync.model.response.specificconfiguration;

import com.kubilay.configsync.model.dto.specificconfiguration.DatasourceDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificConfigResponse {

    @NotNull(message = "Datasource field is required")
    @Valid
    private DatasourceDTO datasource;
}
