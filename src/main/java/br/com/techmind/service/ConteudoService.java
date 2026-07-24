package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConteudoService {

    private final OciGenerativeAiService ociGenerativeAiService;

    public ConteudoService(OciGenerativeAiService ociGenerativeAiService) {
        this.ociGenerativeAiService = ociGenerativeAiService;
    }

    public ConteudoResponse processar(ConteudoRequest request) {
        // Tenta processar utilizando a integração OCI se configurada
        Optional<ConteudoResponse> ociResult = ociGenerativeAiService.processarComOci(
                request.getTitulo(),
                request.getTexto()
        );

        if (ociResult.isPresent()) {
            return ociResult.get();
        }

        // Fallback local se a OCI não estiver configurada no ambiente
        ConteudoResponse response = new ConteudoResponse();
        response.setCategoria("Backend");
        response.setProbabilidade(0.95);
        response.setPalavrasChave(
                List.of("Java", "Spring Boot", "API REST", "OCI Integration Ready")
        );
        response.setResumo(
                "Introdução ao desenvolvimento de APIs REST utilizando Spring Boot (Pronto para conexão OCI)."
        );

        return response;
    }
}