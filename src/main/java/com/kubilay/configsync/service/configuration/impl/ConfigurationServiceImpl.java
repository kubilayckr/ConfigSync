package com.kubilay.configsync.service.configuration.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.kubilay.configsync.config.FilePathsConfig;
import com.kubilay.configsync.exception.BusinessException;
import com.kubilay.configsync.model.dto.configuration.ActionDTO;
import com.kubilay.configsync.model.request.configuration.ConfigRequest;
import com.kubilay.configsync.model.response.configuration.ConfigResponse;
import com.kubilay.configsync.service.configuration.ConfigurationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.kubilay.configsync.exception.BusinessException.ServiceException.CONFIG_FILE_COULD_NOT_READ;
import static com.kubilay.configsync.exception.BusinessException.ServiceException.CONFIG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final String CONFIG_KEY = "CONFIG_";

    private final FilePathsConfig filePathsConfig;
    private final Validator validator;

    public String createConfig(ConfigRequest request) {
        String id = UUID.randomUUID().toString();
        String filePath = getFilePath(id);

        try {
            Path path = Path.of(filePath);
            Files.createDirectories(path.getParent());

            File file = new File(filePath);
            ObjectMapper objectMapper = getCustomizedObjectMapper();
            objectMapper.writeValue(file, request);
        } catch (IOException e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }

        return id;
    }

    public ConfigResponse getConfigByID(String id) {
        ConfigResponse config;
        try {
            String filePath = getFilePath(id);
            String yamlContent = Files.readString(Path.of(filePath));

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            config = objectMapper.readValue(yamlContent, ConfigResponse.class);
        } catch (IOException e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }

        validateActions(config.getActions());

        return config;
    }

    public void updateConfig(String id, ConfigRequest request) {
        String filePath = getFilePath(id);
        File yamlFile = new File(filePath);

        if (!yamlFile.exists()) {
            throw new BusinessException(CONFIG_NOT_FOUND.getKey() , CONFIG_NOT_FOUND.getStatus());
        }

        try {
            ObjectMapper objectMapper = getCustomizedObjectMapper();
            objectMapper.writeValue(yamlFile, request);
        } catch (IOException e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }
    }

    public void deleteConfig(String id) {
        String filePath = getFilePath(id);
        File yamlFile = new File(filePath);

        if (!yamlFile.exists()) {
            throw new BusinessException(CONFIG_NOT_FOUND.getKey() , CONFIG_NOT_FOUND.getStatus());
        }

        try {
            Files.delete(Path.of(filePath));
        } catch (Exception e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }
    }

    public ConfigResponse getAllConfigs() {
        File directory = new File(filePathsConfig.getConfigPath());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new BusinessException(CONFIG_NOT_FOUND.getKey() , CONFIG_NOT_FOUND.getStatus());
        }

        ConfigResponse allConfigs = new ConfigResponse();
        allConfigs.setActions(new ArrayList<>());
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

        for (File file : Objects.requireNonNull(directory.listFiles((dir, name) -> name.startsWith(CONFIG_KEY) && name.endsWith(".yaml")))) {
            try {
                ConfigResponse config = yamlMapper.readValue(file, ConfigResponse.class);
                validateActions(config.getActions());
                allConfigs.getActions().addAll(config.getActions());
            } catch (IOException e) {
                throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
            }
        }

        return allConfigs;
    }

    public List<String> getAllConfigIds() {
        File directory = new File(filePathsConfig.getConfigPath());
        File[] files = Objects.requireNonNull(directory.listFiles((dir, name) -> name.startsWith(CONFIG_KEY) && name.endsWith(".yaml")));
        return Arrays.stream(files).map(file -> file.getName().replace(CONFIG_KEY, "").replace(".yaml", "")).collect(Collectors.toList());
    }

    private void validateActions(List<ActionDTO> actions) {
        for (ActionDTO action : actions) {
            Set<ConstraintViolation<ActionDTO>> violations = validator.validate(action);
            if (!violations.isEmpty()) {
                throw new IllegalArgumentException(
                        violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "))
                );
            }
        }
    }

    private String getFilePath(String id) {
        String fileName = CONFIG_KEY + id + ".yaml";
        return filePathsConfig.getConfigPath() + fileName;
    }

    private static ObjectMapper getCustomizedObjectMapper() {
        YAMLFactory yamlFactory = new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR);
        return new ObjectMapper(yamlFactory );
    }
}
