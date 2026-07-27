package br.com.techmind.controller;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import br.com.techmind.service.ConteudoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConteudoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConteudoService conteudoService;

    @Test
    @DisplayName("POST /conteudo - Deve retornar HTTP 200 OK com a análise quando a requisição for válida")
    void deveProcessarConteudoComSucesso() throws Exception {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Spring Boot Rest API");
        request.setTexto("Desenvolvimento de APIs com Java 17 e Spring Boot Framework.");

        ConteudoResponse responseMock = new ConteudoResponse(
                "Backend", 0.95, List.of("Java", "Spring Boot"), "Resumo do texto"
        );

        Mockito.when(conteudoService.processar(any(ConteudoRequest.class))).thenReturn(responseMock);

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("Backend"))
                .andExpect(jsonPath("$.probabilidade").value(0.95))
                .andExpect(jsonPath("$.palavrasChave[0]").value("Java"))
                .andExpect(jsonPath("$.resumo").value("Resumo do texto"));
    }

    @Test
    @DisplayName("POST /conteudo - Deve retornar HTTP 400 Bad Request quando os campos estiverem em branco")
    void deveRetornar400QuandoCamposEstiveremEmBranco() throws Exception {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("");
        request.setTexto("");

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Requisição Inválida"))
                .andExpect(jsonPath("$.errors.titulo").exists())
                .andExpect(jsonPath("$.errors.texto").exists());
    }

    @Test
    @DisplayName("POST /conteudo - Deve retornar HTTP 400 Bad Request quando o título for muito curto")
    void deveRetornar400QuandoTituloForMuitoCurto() throws Exception {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("AB");
        request.setTexto("Texto válido com mais de dez caracteres.");

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.titulo").value("O título deve possuir entre 3 e 200 caracteres"));
    }
}
