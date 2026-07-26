package br.com.techmind.database.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade responsável por representar os conteúdos técnicos
 * armazenados no banco de dados.
 *
 * <p>
 * Armazena os dados originais enviados pelo usuário
 * juntamente com as informações de classificação
 * geradas pela Inteligência Artificial.
 * </p>
 */
@Entity
@Table(name = "conteudos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConteudoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    private String areaPrincipal;

    private String subarea;

    private Double confiancaArea;

    private Double confiancaSubarea;

    @Column(columnDefinition = "TEXT")
    private String palavrasChave;

}
