package br.com.techmind.controller;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import br.com.techmind.service.ConteudoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conteudo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Análise de Conteúdo", description = "Endpoints para processamento, categorização e inteligência de texto")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Processar e classificar texto", description = "Recebe um texto com título, identifica a categoria tecnológica, extrai palavras-chave, estima precisão e gera resumo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteúdo processado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConteudoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou em branco",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno no processamento do servidor",
                    content = @Content(mediaType = "application/json"))
    })
    public ConteudoResponse processar(
            @RequestBody @Valid ConteudoRequest request) {

        return conteudoService.processar(request);
    }
}