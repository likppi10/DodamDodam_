package com.ssafy.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("Demo API").version(appVersion)
                .description("SSAFY 구미 2반 D203팀의 DodamDodam Project")
                .termsOfService("https://swagger.io/terms/")
                .contact(new Contact().name("hanjibung").email("wjs1724@naver.com"))
                .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"));

        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:8080");

        Server awsServer = new Server();
        awsServer.setDescription("aws");
        awsServer.setUrl("https://happydodam.com");

        return new OpenAPI()
                .components(new Components())
                .info(info)
                .servers(Arrays.asList(localServer, awsServer));
    }
}
