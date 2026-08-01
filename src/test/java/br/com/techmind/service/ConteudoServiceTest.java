package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConteudoServiceTest {

    private ConteudoService conteudoService;

    @BeforeEach
    void setUp() {
        OciGenerativeAiService dummyOciService = new OciGenerativeAiService(null, null) {
            @Override
            public Optional<ConteudoResponse> processarComOci(String titulo, String texto) {
                return Optional.empty();
            }
        };
        conteudoService = new ConteudoService(dummyOciService);
    }

    @Test
    @DisplayName("Deve retornar resposta de fallback gracioso quando os serviços de IA estiverem indisponíveis")
    void deveRetornarFallbackQuandoServicosOffline() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Título do Artigo");
        request.setTexto("Texto de teste com comprimento suficiente para validação.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Erro de IA", response.getCategoria());
        assertEquals(0.0, response.getProbabilidade());
        assertNotNull(response.getResumo());
    }
}
