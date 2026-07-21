package br.com.techmind.controller;

import br.com.techmind.dto.request.ConteudoRequest;
import br.com.techmind.dto.response.ConteudoResponse;
import br.com.techmind.service.IAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conteudo")
@Tag(name = "Conteúdo", description = "Endpoints para classificação de conteúdo com IA")
public class ConteudoController {

    @Autowired
    private IAService iaService;

    @PostMapping("/classificar")
    @Operation(summary = "Classifica um conteúdo técnico")
    public ResponseEntity<ConteudoResponse> classificar(@RequestBody ConteudoRequest request) {
        ConteudoResponse response = iaService.classificar(request);
        return ResponseEntity.ok(response);
    }
}