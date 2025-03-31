package com.kubilay.configsync.model.dto.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterActionDTO extends ActionDTO {

    @NotNull(message = "OldValue is required for alter action")
    private String oldValue;

    @NotNull(message = "NewValue is required for alter action")
    private String newValue;

    public AlterActionDTO() {
        this.type = "alter";
    }
}