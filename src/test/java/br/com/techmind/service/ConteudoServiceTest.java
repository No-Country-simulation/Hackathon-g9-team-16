package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConteudoServiceTest {

    private ConteudoService conteudoService;

    @BeforeEach
    void setUp() {
        conteudoService = new ConteudoService();
    }

    @Test
    @DisplayName("Deve categorizar como Backend e extrair palavras-chave quando o texto se referir a Java e Spring")
    void deveCategorizarComoBackend() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Arquitetura de Microsserviços com Spring Boot");
        request.setTexto("Desenvolvimento de APIs RESTful utilizando Java 17, Spring Boot, Hibernate e banco de dados PostgreSQL.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Backend", response.getCategoria());
        assertTrue(response.getProbabilidade() >= 0.90);
        assertTrue(response.getPalavrasChave().contains("Java"));
        assertTrue(response.getPalavrasChave().contains("Spring"));
        assertNotNull(response.getResumo());
    }

    @Test
    @DisplayName("Deve categorizar como Frontend quando o texto tratar de React e TypeScript")
    void deveCategorizarComoFrontend() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Construindo UIs modernas com React");
        request.setTexto("Aprenda a utilizar React, TypeScript, Tailwind CSS e Next para criar aplicações web extremamente responsivas.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Frontend", response.getCategoria());
        assertTrue(response.getProbabilidade() >= 0.90);
        assertTrue(response.getPalavrasChave().contains("React"));
        assertTrue(response.getPalavrasChave().contains("Typescript"));
    }

    @Test
    @DisplayName("Deve categorizar como DevOps quando o texto citar Docker e Kubernetes")
    void deveCategorizarComoDevOps() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Orquestração de Containers em Nuvem");
        request.setTexto("Implementação de pipelines de CI/CD utilizando Docker, Kubernetes, AWS e Terraform para automação da infraestrutura.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("DevOps", response.getCategoria());
        assertTrue(response.getProbabilidade() >= 0.90);
        assertTrue(response.getPalavrasChave().contains("Docker"));
        assertTrue(response.getPalavrasChave().contains("Kubernetes"));
    }

    @Test
    @DisplayName("Deve categorizar como Data & AI quando o texto abordar Machine Learning e Python")
    void deveCategorizarComoDataEAI() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Introdução à Inteligência Artificial");
        request.setTexto("Modelos de machine learning treinados em Python utilizando TensorFlow, PyTorch e Pandas para análise preditiva de dados.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Data & AI", response.getCategoria());
        assertTrue(response.getProbabilidade() >= 0.90);
        assertTrue(response.getPalavrasChave().contains("Python"));
    }

    @Test
    @DisplayName("Deve categorizar como Cybersecurity quando o texto tratar de JWT e Segurança")
    void deveCategorizarComoCybersecurity() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Boas Práticas de Segurança em Web APIs");
        request.setTexto("Proteção contra ataques XSS e CSRF utilizando tokens JWT, OAuth e criptografia SSL/TLS em todas as rotas.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Cybersecurity", response.getCategoria());
        assertTrue(response.getProbabilidade() >= 0.90);
        assertTrue(response.getPalavrasChave().contains("Jwt"));
    }

    @Test
    @DisplayName("Deve utilizar categoria padrão 'Tecnologia Geral' para textos sem palavras-chave específicas")
    void deveUsarCategoriaPadraoParaTextoGenerico() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Reflexões sobre a transformação digital no mercado de trabalho");
        request.setTexto("A evolução acelerada do setor produtivo demanda novos modelos de gestão, cultura de inovação e aprendizado contínuo.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response);
        assertEquals("Tecnologia Geral", response.getCategoria());
        assertEquals(0.70, response.getProbabilidade());
        assertFalse(response.getPalavrasChave().isEmpty());
    }

    @Test
    @DisplayName("Deve truncar o resumo caso o texto seja longo")
    void deveTruncarResumoParaTextoLongo() {
        ConteudoRequest request = new ConteudoRequest();
        request.setTitulo("Título do Artigo");
        request.setTexto("Este é um texto significativamente longo projetado para testar o comportamento do método de geração de resumo da classe ConteudoService, garantindo que o retorno seja limitado sem exceder os limites configurados.");

        ConteudoResponse response = conteudoService.processar(request);

        assertNotNull(response.getResumo());
        assertTrue(response.getResumo().length() <= 200);
    }
}
