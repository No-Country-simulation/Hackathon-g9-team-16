package br.com.techmind.database.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String texto;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

}
