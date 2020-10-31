package com.roche.orderservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

@Configuration
@Import({ SpringDataRestConfiguration.class, BeanValidatorPluginsConfiguration.class })
public class SpringFoxConfig {
}
