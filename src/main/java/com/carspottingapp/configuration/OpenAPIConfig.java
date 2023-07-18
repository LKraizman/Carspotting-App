package com.carspottingapp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${carspotting.openapi.url}")
    private String url;

    @Bean
    public OpenAPI carspottingAPI(){
        Server devServer = new Server();
        devServer.setUrl(url);
        devServer.description("Server URL");

        Contact contact = new Contact();
        contact.setEmail("checkyoslf@gmail.com");
        contact.setName("L.Kraizman");
        contact.setUrl("in progress");

        License mitlecense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Spot IT API")
                .version("0.0.1")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.")
                .license(mitlecense);
        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
