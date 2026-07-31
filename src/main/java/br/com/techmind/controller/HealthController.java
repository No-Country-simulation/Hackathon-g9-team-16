package br.com.techmind.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Status da Aplicação", description = "Endpoints de verificação de disponibilidade")
public class HealthController {

    @GetMapping(value = {"/", "/health"})
    @CrossOrigin(origins = "*")
    @Operation(summary = "Verificar integridade da API", description = "Retorna uma mensagem de status informando que a TechMind API está online.")
    public Map<String, Object> status() {
        return Map.of(
                "status", "UP",
                "application", "TechMind API",
                "version", "1.0.0",
                "timestamp", System.currentTimeMillis()
        );
    }
}