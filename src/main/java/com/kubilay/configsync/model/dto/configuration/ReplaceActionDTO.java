package com.kubilay.configsync.model.dto.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplaceActionDTO extends ActionDTO {

    @NotNull(message = "Selector is required for replace action")
    private String selector;

    @NotNull(message = "NewElement is required for replace action")
    private String newElement;

    public ReplaceActionDTO() {
        this.type = "replace";
    }
}
