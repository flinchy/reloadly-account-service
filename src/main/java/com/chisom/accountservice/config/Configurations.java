package com.chisom.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.service.ApiKey;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
public class Configurations {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("REST API - Account Service",
                "Rest Endpoint for Account Microservice.",
                "1.0",
                "Terms of service",
                new Contact("Chisom Iwowo", "https://www.linkedin.com/in/iwowo-chisom/", "iwowochisom@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(basePackage("com.chisom.accountservice"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
