package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConteudoService {

    public ConteudoResponse processar(ConteudoRequest request) {

        ConteudoResponse response = new ConteudoResponse();

        response.setCategoria("Backend");
        response.setProbabilidade(0.95);
        response.setPalavrasChave(
                List.of("Java", "Spring Boot", "API REST")
        );
        response.setResumo(
                "Introdução ao desenvolvimento de APIs REST utilizando Spring Boot."
        );

        return response;
    }
}