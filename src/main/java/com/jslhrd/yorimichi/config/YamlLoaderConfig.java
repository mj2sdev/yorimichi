package com.jslhrd.yorimichi.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class YamlLoaderConfig {
	
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource resource = new ClassPathResource("api.yaml");
		if (resource.exists()) {
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(resource);
			configurer.setProperties(yaml.getObject());
		} else {
			configurer.setProperties(new java.util.Properties());
		}
		return configurer;
	}
}
