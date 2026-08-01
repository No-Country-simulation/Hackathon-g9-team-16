package br.com.techmind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Classe responsável pela configuração dos clientes REST utilizados
 * pela aplicação.
 *
 * <p>
 * Disponibiliza os beans necessários para realizar comunicação HTTP
 * com serviços externos, como a API de Inteligência Artificial
 * desenvolvida em Python.
 * </p>
 *
 * <p>
 * As configurações definidas nesta classe são utilizadas pelo
 * {@link br.com.techmind.integration.client.PythonApiClient}
 * durante as chamadas para o serviço externo.
 * </p>
 */
@Configuration
public class RestClientConfig {


    /**
     * Cria uma instância de {@link RestClient} disponibilizada pelo Spring
     * para injeção de dependência em outras classes da aplicação.
     *
     * <p>
     * O cliente REST configurado é utilizado para realizar requisições HTTP
     * para serviços externos.
     * </p>
     *
     * @return instância configurada do cliente REST
     */
    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .requestFactory(new SimpleClientHttpRequestFactory())
                .build();
    }
}