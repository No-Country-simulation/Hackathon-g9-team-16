package br.com.techmind.service;

import br.com.techmind.dto.request.TeamRequestDTO;
import br.com.techmind.dto.response.TeamResponseDTO;
import br.com.techmind.exception.DuplicateResourceException;
import br.com.techmind.exception.ResourceNotFoundException;
import br.com.techmind.model.entity.Team;
import br.com.techmind.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Lista todos os times cadastrados
     */
    public List<TeamResponseDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um time pelo ID
     * @throws ResourceNotFoundException se o time não existir
     */
    public TeamResponseDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time não encontrado com ID: " + id));
        return toResponseDTO(team);
    }

    /**
     * Cria um novo time
     * @throws DuplicateResourceException se já existir um time com o mesmo nome
     */
    public TeamResponseDTO createTeam(TeamRequestDTO request) {
        // 🔒 VALIDAÇÃO DE NOME ÚNICO - CORRIGIDA!
        if (teamRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Já existe um time com o nome: " + request.getName());
        }

        Team team = new Team();
        team.setName(request.getName());
        team.setTechnology(request.getTechnology());
        team.setDescription(request.getDescription());

        Team savedTeam = teamRepository.save(team);
        return toResponseDTO(savedTeam);
    }

    /**
     * Atualiza um time existente
     * @throws ResourceNotFoundException se o time não existir
     * @throws DuplicateResourceException se o novo nome já pertencer a outro time
     */
    public TeamResponseDTO updateTeam(Long id, TeamRequestDTO request) {
        // Verifica se o time existe
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time não encontrado com ID: " + id));

        // 🔒 VALIDAÇÃO DE NOME ÚNICO NA ATUALIZAÇÃO
        // Se o nome foi alterado e já existe em outro time, bloqueia
        if (!team.getName().equals(request.getName()) &&
                teamRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Já existe um time com o nome: " + request.getName());
        }

        team.setName(request.getName());
        team.setTechnology(request.getTechnology());
        team.setDescription(request.getDescription());

        Team updatedTeam = teamRepository.save(team);
        return toResponseDTO(updatedTeam);
    }

    /**
     * Deleta um time pelo ID
     * @throws ResourceNotFoundException se o time não existir
     */
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Time não encontrado com ID: " + id);
        }
        teamRepository.deleteById(id);
    }

    /**
     * Converte uma entidade Team para TeamResponseDTO
     */
    private TeamResponseDTO toResponseDTO(Team team) {
        return new TeamResponseDTO(
                team.getId(),
                team.getName(),
                team.getTechnology(),
                team.getDescription(),
                team.getCreatedAt()
        );
    }
}