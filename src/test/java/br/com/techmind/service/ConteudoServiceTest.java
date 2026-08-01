package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ConteudoServiceTest {

    private ConteudoService conteudoService;
    private OciGenerativeAiService ociGenerativeAiService;

    @BeforeEach
    void setUp() {
        ociGenerativeAiService = Mockito.mock(OciGenerativeAiService.class);
        conteudoService = new ConteudoService(ociGenerativeAiService);
    }

    @Test
    @DisplayName("Deve retornar resposta processada via OCI Generative AI quando configurado")
    void deveProcessarComSucessoViaOci() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Arquitetura de Microsserviços com Spring Boot");
        request.setTexto("Desenvolvimento de APIs RESTful utilizando Java 17, Spring Boot e banco de dados PostgreSQL.");

        ConteudoResponse expected = new ConteudoResponse();
        expected.setCategoria("Backend");
        expected.setProbabilidade(0.95);
        expected.setPalavrasChave(List.of("Java", "Spring Boot"));
        expected.setResumo(request.getTitulo());

        when(ociGenerativeAiService.processarComOci(anyString(), anyString()))
                .thenReturn(Optional.of(expected));

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Backend", response.getCategoria());
        assertEquals(0.95, response.getProbabilidade());
        assertTrue(response.getPalavrasChave().contains("Java"));
    }

    @Test
    @DisplayName("Deve retornar resposta de fallback gracioso quando os serviços de IA estiverem indisponíveis")
    void deveRetornarFallbackQuandoServicosOffline() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Título do Artigo");
        request.setTexto("Texto de teste com comprimento suficiente para validação.");

        when(ociGenerativeAiService.processarComOci(anyString(), anyString()))
                .thenReturn(Optional.empty());

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Erro de IA", response.getCategoria());
        assertEquals(0.0, response.getProbabilidade());
        assertNotNull(response.getResumo());
    }
}
