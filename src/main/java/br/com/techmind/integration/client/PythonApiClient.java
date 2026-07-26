package br.com.techmind.integration.client;

import br.com.techmind.integration.dto.request.PythonConteudoRequest;
import br.com.techmind.integration.dto.response.PythonConteudoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente responsável pela comunicação entre a aplicação Java
 * e a API de Inteligência Artificial desenvolvida em Python.
 *
 * <p>
 * Essa classe realiza chamadas HTTP para enviar conteúdos técnicos
 * para processamento da IA e recebe como retorno informações como:
 * classificação, palavras-chave e conteúdos relacionados.
 * </p>
 */
@Component
public class PythonApiClient {


    private final RestClient restClient;

    @Value("${python.api.url}")
    private String pythonApiUrl;


    public PythonApiClient(RestClient restClient) {
        this.restClient = restClient;
    }


    /**
     * Envia um conteúdo técnico para processamento da Inteligência Artificial.
     *
     * <p>
     * O conteúdo recebido é convertido para JSON e enviado através de uma
     * requisição HTTP POST para a API Python.
     * </p>
     *
     * @param request dados do conteúdo que será analisado pela IA
     *
     * @return resposta contendo classificação, palavras-chave
     * e conteúdos relacionados gerados pela IA
     *
     * @throws RuntimeException caso ocorra falha na comunicação externa
     */
    public PythonConteudoResponse processarConteudo(
            PythonConteudoRequest request) {


        return restClient.post()
                .uri(pythonApiUrl + "/conteudo")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(PythonConteudoResponse.class);

    }
}