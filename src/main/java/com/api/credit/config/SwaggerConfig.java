package com.api.credit.config;


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
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.creditos.com.br")
                                .description("Servidor de Produção")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("API de Créditos Tributários")
                .description("""
                    API para gerenciamento de créditos tributários do ISSQN.

                    **Funcionalidades principais:**
                    - Consulta de créditos por número do crédito
                    - Consulta de créditos por NFSE
                    - Validação de dados tributários
                    - Integração com Kafka para processamento assíncrono

                    **Versão:** 1.0.0
                    **Ambiente:** Desenvolvimento
                    """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Equipe de Desenvolvimento")
                        .email("dev@creditos.com.br")
                        .url("https://github.com/empresa/api-creditos"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"))
                .termsOfService("https://api.creditos.com.br/terms");
    }
}