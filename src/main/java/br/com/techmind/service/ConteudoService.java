package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConteudoService {

    private static final Logger log = LoggerFactory.getLogger(ConteudoService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String FASTAPI_URL = "http://127.0.0.1:8000/conteudo";

    @SuppressWarnings("unchecked")
    public ConteudoResponse processar(ConteudoRequest request) {
        log.info("Processando solicitação de análise de conteúdo com título: '{}'", request.getTitulo());

        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("titulo", request.getTitulo());
            payload.put("texto", request.getTexto());

            Map<String, Object> apiResponse = restTemplate.postForObject(FASTAPI_URL, payload, Map.class);

            if (apiResponse != null) {
                ConteudoResponse response = new ConteudoResponse();

                Map<String, Object> classificacao = (Map<String, Object>) apiResponse.get("classificacao");
                if (classificacao != null) {
                    response.setCategoria((String) classificacao.get("subarea"));

                    Object confianca = classificacao.get("confianca_subarea");
                    if (confianca instanceof Number) {
                        response.setProbabilidade(((Number) confianca).doubleValue());
                    } else {
                        response.setProbabilidade(1.0);
                    }
                } else {
                    response.setCategoria("Indefinido");
                    response.setProbabilidade(0.0);
                }

                response.setPalavrasChave((List<String>) apiResponse.get("palavras_chave"));
                response.setResumo(request.getTitulo());

                log.info("Conteúdo analisado com sucesso via IA. Categoria: '{}', Probabilidade: {}",
                        response.getCategoria(), response.getProbabilidade());

                return response;
            }
        } catch (Exception e) {
            log.error("Erro ao integrar com o modelo FastAPI: {}", e.getMessage());
        }

        ConteudoResponse fallback = new ConteudoResponse();
        fallback.setCategoria("Erro de IA");
        fallback.setProbabilidade(0.0);
        fallback.setPalavrasChave(List.of("Modelo offline", "Inicie o FastAPI"));
        fallback.setResumo("Por favor, verifique se a API do FastAPI (Python) está rodando localmente na porta 8000.");
        return fallback;
    }
}
