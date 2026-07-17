package br.com.techmind.dto.response;

import java.time.LocalDateTime;

public class TeamResponseDTO {
    private Long id;
    private String name;
    private String technology;
    private String description;
    private LocalDateTime createdAt;

    public TeamResponseDTO() {}

    public TeamResponseDTO(Long id, String name, String technology, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.technology = technology;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTechnology() { return technology; }
    public void setTechnology(String technology) { this.technology = technology; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}