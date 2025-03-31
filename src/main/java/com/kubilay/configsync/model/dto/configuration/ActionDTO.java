package com.kubilay.configsync.model.dto.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ActionDTO {

    @NotNull(message = "Type field is required")
    protected  String type;

    @JsonCreator
    public static ActionDTO createAction(@JsonProperty("type") String type) {
        return switch (type) {
            case "remove" -> new RemoveActionDTO();
            case "replace" -> new ReplaceActionDTO();
            case "insert" -> new InsertActionDTO();
            case "alter" -> new AlterActionDTO();
            default -> throw new IllegalArgumentException("Unknown action type: " + type);
        };
    }
}
