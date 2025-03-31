package com.kubilay.configsync.service.specificconfiguration.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.kubilay.configsync.config.FilePathsConfig;
import com.kubilay.configsync.exception.BusinessException;
import com.kubilay.configsync.model.dto.specificconfiguration.DatasourceDTO;
import com.kubilay.configsync.model.request.specificconfiguration.SpecificConfigRequest;
import com.kubilay.configsync.model.response.specificconfiguration.SpecificConfigResponse;
import com.kubilay.configsync.service.specificconfiguration.SpecificConfigurationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.kubilay.configsync.exception.BusinessException.ServiceException.CONFIG_FILE_COULD_NOT_READ;
import static com.kubilay.configsync.exception.BusinessException.ServiceException.CONFIG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SpecificConfigurationServiceImpl implements SpecificConfigurationService {

    private static final String SPECIFIC_CONFIG_KEY = "SPECIFIC_CONFIG_";

    private final FilePathsConfig filePathsConfig;
    private final Validator validator;

    public String createSpecificConfig(SpecificConfigRequest request) {
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

    public SpecificConfigResponse getSpecificConfigByID(String id) {
        SpecificConfigResponse config;
        try {
            String filePath = getFilePath(id);
            String yamlContent = Files.readString(Path.of(filePath));

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            config = objectMapper.readValue(yamlContent, SpecificConfigResponse.class);
        } catch (IOException e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }

        validateDatasource(config.getDatasource());

        return config;
    }

    public void updateSpecificConfig(String id, SpecificConfigRequest request) {
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

    public void deleteSpecificConfig(String id) {
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

    public SpecificConfigResponse getAllSpecificConfigs() {
        File directory = new File(filePathsConfig.getSpecificConfigPath());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new BusinessException(CONFIG_NOT_FOUND.getKey() , CONFIG_NOT_FOUND.getStatus());
        }

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        SpecificConfigResponse allSpecificConfigs = new SpecificConfigResponse();
        DatasourceDTO datasourceDTO = new DatasourceDTO();
        datasourceDTO.setPages(new HashMap<>());
        datasourceDTO.setUrls(new HashMap<>());
        datasourceDTO.setHosts(new HashMap<>());

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(filePathsConfig.getSpecificConfigPath()), "SPECIFIC_CONFIG_*.yaml")) {
            for (Path file : stream) {
                SpecificConfigResponse specificConfig = yamlMapper.readValue(Files.newBufferedReader(file), SpecificConfigResponse.class);
                if (specificConfig.getDatasource() != null){
                    mergeMaps(specificConfig.getDatasource().getPages(), datasourceDTO.getPages());
                    mergeMaps(specificConfig.getDatasource().getUrls(), datasourceDTO.getUrls());
                    mergeMaps(specificConfig.getDatasource().getHosts(), datasourceDTO.getHosts());
                }
            }
        }  catch (Exception e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }

        validateDatasource(datasourceDTO);

        allSpecificConfigs.setDatasource(datasourceDTO);
        return allSpecificConfigs;
    }

    public SpecificConfigResponse getAllSpecificConfigsByPageAndUrlAndHost(String page, String url, String host) {
        File directory = new File(filePathsConfig.getSpecificConfigPath());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new BusinessException(CONFIG_NOT_FOUND.getKey() , CONFIG_NOT_FOUND.getStatus());
        }

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        SpecificConfigResponse allSpecificConfigs = new SpecificConfigResponse();
        DatasourceDTO datasourceDTO = new DatasourceDTO();
        datasourceDTO.setPages(new HashMap<>());
        datasourceDTO.setUrls(new HashMap<>());
        datasourceDTO.setHosts(new HashMap<>());

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(filePathsConfig.getSpecificConfigPath()), "SPECIFIC_CONFIG_*.yaml")) {
            for (Path file : stream) {
                SpecificConfigResponse specificConfig = yamlMapper.readValue(Files.newBufferedReader(file), SpecificConfigResponse.class);
                if (specificConfig.getDatasource() != null){
                    mergeSection(specificConfig.getDatasource().getPages(), datasourceDTO.getPages(), page);
                    mergeSection(specificConfig.getDatasource().getUrls(), datasourceDTO.getUrls(), url);
                    mergeSection(specificConfig.getDatasource().getHosts(), datasourceDTO.getHosts(), host);
                }
            }
        }  catch (Exception e) {
            throw new BusinessException(CONFIG_FILE_COULD_NOT_READ.getKey() , CONFIG_FILE_COULD_NOT_READ.getStatus());
        }

        validateDatasource(datasourceDTO);

        allSpecificConfigs.setDatasource(datasourceDTO);
        return allSpecificConfigs;
    }

    public List<String> getAllSpecificConfigIds() {
        File directory = new File(filePathsConfig.getSpecificConfigPath());
        File[] files = Objects.requireNonNull(directory.listFiles((dir, name) -> name.startsWith(SPECIFIC_CONFIG_KEY) && name.endsWith(".yaml")));
        return Arrays.stream(files).map(file -> file.getName().replace(SPECIFIC_CONFIG_KEY, "").replace(".yaml", "")).collect(Collectors.toList());
    }

    private static void mergeMaps(Map<String, List<String>> source, Map<String, List<String>> target) {
        if (!CollectionUtils.isEmpty(source)) {
            for (Map.Entry<String, List<String>> entry : source.entrySet()) {
                target.merge(entry.getKey(), entry.getValue(), (existing, newValues) -> {
                    Set<String> mergedSet = new HashSet<>(existing);
                    mergedSet.addAll(newValues);
                    return new ArrayList<>(mergedSet);
                });
            }
        }
    }

    private void mergeSection(Map<String, List<String>> source, Map<String, List<String>> target, String filterKey) {
        if (!CollectionUtils.isEmpty(source)) {
            if (source.containsKey(filterKey)) {
                target.merge(filterKey, source.get(filterKey), (existing, newValues) -> {
                    Set<String> mergedSet = new HashSet<>(existing);
                    mergedSet.addAll(newValues);
                    return new ArrayList<>(mergedSet);
                });
            }
        }
    }

    private void validateDatasource(DatasourceDTO datasourceDTO) {
        Set<ConstraintViolation<DatasourceDTO>> violations = validator.validate(datasourceDTO);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))
            );
        }
    }

    private String getFilePath(String id) {
        String fileName = SPECIFIC_CONFIG_KEY + id + ".yaml";
        return filePathsConfig.getSpecificConfigPath() + fileName;
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
