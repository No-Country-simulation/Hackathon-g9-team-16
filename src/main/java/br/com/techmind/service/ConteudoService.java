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
import java.util.Optional;

@Service
public class ConteudoService {

    private static final Logger log = LoggerFactory.getLogger(ConteudoService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String FASTAPI_URL = "http://127.0.0.1:8000/conteudo";
    
    private final OciGenerativeAiService ociGenerativeAiService;

    public ConteudoService(OciGenerativeAiService ociGenerativeAiService) {
        this.ociGenerativeAiService = ociGenerativeAiService;
    }

    @SuppressWarnings("unchecked")
    public ConteudoResponse processar(ConteudoRequest request) {
        log.info("Processando solicitação de análise de conteúdo com título: '{}'", request.getTitulo());

        // 1. Tenta processar utilizando a integração OCI se configurada
        try {
            Optional<ConteudoResponse> ociResult = ociGenerativeAiService.processarComOci(
                    request.getTitulo(),
                    request.getTexto()
            );

            if (ociResult.isPresent()) {
                log.info("Conteúdo analisado com sucesso via OCI Generative AI.");
                return ociResult.get();
            }
        } catch (Exception e) {
            log.warn("Integração OCI não disponível ou falhou, tentando modelo local: {}", e.getMessage());
        }

        // 2. Fallback: Tenta processar via FastAPI local (Modelo da Equipe)
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

                log.info("Conteúdo analisado com sucesso via modelo local (FastAPI). Categoria: '{}'", response.getCategoria());
                return response;
            }
        } catch (Exception e) {
            log.error("Erro ao integrar com o modelo FastAPI local: {}", e.getMessage());
        }

        // 3. Fallback final caso tudo falhe
        ConteudoResponse fallback = new ConteudoResponse();
        fallback.setCategoria("Erro de IA");
        fallback.setProbabilidade(0.0);
        fallback.setPalavrasChave(List.of("Modelos offline", "Inicie o FastAPI ou configure OCI"));
        fallback.setResumo("Não foi possível processar o texto. Verifique se a IA da Oracle ou o FastAPI local estão ativos.");
        return fallback;
    }
}