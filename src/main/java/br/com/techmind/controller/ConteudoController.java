package br.com.techmind.controller;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import br.com.techmind.service.ConteudoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conteudo")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @PostMapping
    public ConteudoResponse processar(
            @RequestBody @Valid ConteudoRequest request) {

        return conteudoService.processar(request);
    }
}