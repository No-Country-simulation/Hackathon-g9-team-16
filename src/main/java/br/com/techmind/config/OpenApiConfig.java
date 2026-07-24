package br.com.techmind.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI techMindOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechMind API - Processador de Conteúdo Técnico")
                        .description("API RESTful para análise, categorização automática, extração de palavras-chave e geração de resumos de conteúdos de tecnologia.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe TechMind")
                                .email("contato@techmind.com.br")
                                .url("https://github.com/Hackathon-g9-team-16"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente Local / Desenvolvimento")
                ));
    }
}
