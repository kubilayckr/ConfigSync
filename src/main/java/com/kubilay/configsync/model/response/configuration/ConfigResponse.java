package com.kubilay.configsync.model.response.configuration;

import com.kubilay.configsync.model.dto.configuration.ActionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConfigResponse {

    @NotNull(message = "Actions field is required")
    @Valid
    private List<@Valid ActionDTO> actions;
}
