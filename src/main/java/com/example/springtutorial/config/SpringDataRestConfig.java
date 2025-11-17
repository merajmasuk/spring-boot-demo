package com.example.springtutorial.config;

import com.example.springtutorial.handlers.InstanceEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class SpringDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry registry) {
        config.setBasePath("/api/rest");
        config.setRepositoryDetectionStrategy(
                RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED
        );
    }

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer(InstanceEventHandler handler) {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener v) {
                // stub
            }
        };
    }
}
