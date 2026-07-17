package br.com.techmind.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeamRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "A tecnologia é obrigatória")
    @Size(max = 50, message = "A tecnologia deve ter no máximo 50 caracteres")
    private String technology;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String description;

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTechnology() { return technology; }
    public void setTechnology(String technology) { this.technology = technology; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}