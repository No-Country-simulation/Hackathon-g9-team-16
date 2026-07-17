package br.com.techmind.controller;

import br.com.techmind.dto.request.TeamRequestDTO;
import br.com.techmind.dto.response.TeamResponseDTO;
import br.com.techmind.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Endpoints para gerenciamento de times")
public class TeamController {

    @Autowired
    private TeamService teamService;

    // ==================== LISTAR TODOS ====================
    @GetMapping
    @Operation(summary = "Listar todos os times")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    // ==================== BUSCAR POR ID ====================
    @GetMapping("/{id}")
    @Operation(summary = "Buscar time por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Time encontrado"),
            @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    // ==================== CRIAR TIME ====================
    @PostMapping
    @Operation(summary = "Criar um novo time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Time criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Time já existe")
    })
    public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO request) {
        TeamResponseDTO response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== ATUALIZAR TIME ====================
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Time atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Time não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de nome")
    })
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequestDTO request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }

    // ==================== DELETAR TIME ====================
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Time deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}