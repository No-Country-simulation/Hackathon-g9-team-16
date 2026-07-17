package br.com.techmind.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
@EntityListeners(AuditingEntityListener.class)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "A tecnologia é obrigatória")
    @Size(max = 50, message = "A tecnologia deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String technology;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtores
    public Team() {}

    public Team(String name, String technology, String description) {
        this.name = name;
        this.technology = technology;
        this.description = description;
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