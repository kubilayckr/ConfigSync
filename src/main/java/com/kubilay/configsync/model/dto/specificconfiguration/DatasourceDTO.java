package com.kubilay.configsync.model.dto.specificconfiguration;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DatasourceDTO {

    @Valid
    private Map<String, List<String>> pages;

    @Valid
    private Map<String, List<String>> urls;

    @Valid
    private Map<String, List<String>> hosts;
}