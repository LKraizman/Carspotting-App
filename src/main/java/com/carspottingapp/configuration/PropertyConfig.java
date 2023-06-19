package com.carspottingapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spot-it")
@Configuration
@Getter @Setter
public class PropertyConfig {
    private String titleLimit;
    private String descriptionLimit;
}
