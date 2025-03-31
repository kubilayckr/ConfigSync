package com.kubilay.configsync.model.dto.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertActionDTO extends ActionDTO {

    @NotNull(message = "Position is required for insert action")
    private String position;

    @NotNull(message = "Target is required for insert action")
    private String target;

    @NotNull(message = "Element is required for insert action")
    private String element;

    public InsertActionDTO() {
        this.type = "insert";
    }
}
