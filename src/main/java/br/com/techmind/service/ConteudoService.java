package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConteudoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String FASTAPI_URL = "http://127.0.0.1:8000/conteudo";

    @SuppressWarnings("unchecked")
    public ConteudoResponse processar(ConteudoRequest request) {
        try {
            // Cria o corpo da requisição compatível com o FastAPI (Pydantic model ConteudoEntrada)
            Map<String, String> payload = new HashMap<>();
            payload.put("titulo", request.getTitulo());
            payload.put("texto", request.getTexto());

            // Envia o POST para a API do modelo de IA (FastAPI)
            Map<String, Object> apiResponse = restTemplate.postForObject(FASTAPI_URL, payload, Map.class);

            if (apiResponse != null) {
                ConteudoResponse response = new ConteudoResponse();
                
                // Mapeia o JSON multinível do FastAPI para o contrato simples da API Java
                Map<String, Object> classificacao = (Map<String, Object>) apiResponse.get("classificacao");
                if (classificacao != null) {
                    // Categoria maps to subarea
                    response.setCategoria((String) classificacao.get("subarea"));
                    
                    // Probabilidade maps to confianca_subarea
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
                
                // Palavras-chave maps to palavras_chave
                response.setPalavrasChave((List<String>) apiResponse.get("palavras_chave"));
                
                // Resumo maps to o título processado
                response.setResumo(request.getTitulo());
                
                return response;
            }
        } catch (Exception e) {
            System.err.println("Erro ao integrar com o modelo FastAPI: " + e.getMessage());
        }

        // Fallback robusto offline de segurança
        ConteudoResponse fallback = new ConteudoResponse();
        fallback.setCategoria("Erro de IA");
        fallback.setProbabilidade(0.0);
        fallback.setPalavrasChave(List.of("Modelo offline", "Inicie o FastAPI"));
        fallback.setResumo("Por favor, verifique se a API do FastAPI (Python) está rodando localmente na porta 8000.");
        return fallback;
    }
}