package com.masi.employee.service;

import com.masi.employee.service.dto.ConfigDTO;
import com.masi.employee.service.web.client.ConfigClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConfigManager {
    private final Map<String, String> config = new ConcurrentHashMap<>();
    private final ConfigClient configClient;
    public static final String MAX_WORKING_HOURS = "MAX_WORKING_HOURS";
    public static final String MIN_WORKING_HOURS = "MIN_WORKING_HOURS";

    public ConfigManager(ConfigClient configClient) {
        this.configClient = configClient;
    }

    public String getConfigFromCache(String key, String defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }

    public Mono<String> getConfig(String key, String defaultValue) {
        if (config.containsKey(key)) {
            return Mono.just(config.get(key));
        } else {
            return configClient.getConfig(key, defaultValue).map(configDTO -> {
                config.put(key, configDTO.getValue());
                return configDTO.getValue();
            });
        }
    }

    public <T> Mono<T> getConfig(String key, T defaultValue, String description) {
        if (config.containsKey(key)) {
            return Mono.just((T) config.get(key));
        } else {
            var type = defaultValue.getClass().getSimpleName().toUpperCase();
            return configClient.getConfig(key, defaultValue.toString(), type, description).map(configDTO -> {
                config.put(key, configDTO.getValue());
                return (T) configDTO.getValue();
            });
        }
    }
}
