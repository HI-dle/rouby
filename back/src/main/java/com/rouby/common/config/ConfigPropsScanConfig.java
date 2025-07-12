package com.rouby.common.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("com.rouby.common.props")
public class ConfigPropsScanConfig {

}
