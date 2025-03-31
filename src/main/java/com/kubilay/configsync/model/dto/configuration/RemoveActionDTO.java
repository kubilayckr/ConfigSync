package com.kubilay.configsync.model.dto.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveActionDTO extends ActionDTO {

    @NotNull(message = "Selector is required for remove action")
    private String selector;

    public RemoveActionDTO() {
        this.type = "remove";
    }
}
