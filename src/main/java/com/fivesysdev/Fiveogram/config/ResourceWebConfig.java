package com.fivesysdev.Fiveogram.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ResourceWebConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = environment.getProperty("file.storage.mapping");
        String resourceHandlerPath = environment.getProperty("file.storage.prefix") + "/**";

        registry.addResourceHandler(resourceHandlerPath).addResourceLocations(location);
    }
}