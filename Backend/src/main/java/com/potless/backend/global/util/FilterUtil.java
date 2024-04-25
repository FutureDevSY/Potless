package com.potless.backend.global.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "filter-path")
public class FilterUtil {

    private Map<String, String> paths;

    public Map<String, String> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, String> paths) {
        this.paths = paths;
    }
}