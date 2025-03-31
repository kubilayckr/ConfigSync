package com.kubilay.configsync.model.request.specificconfiguration;

import com.kubilay.configsync.model.dto.specificconfiguration.DatasourceDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificConfigRequest {

    @NotNull(message = "Datasource field is required")
    @Valid
    private DatasourceDTO datasource;
}
