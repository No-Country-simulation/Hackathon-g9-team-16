package br.com.techmind.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConteudoResponseDto {

    private Long id;

    private String titulo;

    private String texto;

    private LocalDateTime dataCriacao;


}