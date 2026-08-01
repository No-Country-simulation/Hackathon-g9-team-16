package br.com.techmind.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConteudoRequestDto {

    @NotBlank(message = "O título é obrigatório.")
    @Size(
            max = 255,
            message = "O título deve possuir no máximo 255 caracteres."
    )
    private String titulo;

    @NotBlank(message = "Insira a descrição do conteúdo.")
    @Size(min = 10,  message = "A descrição deve possuir no mínimo 10 caracteres.")
    private String texto;


}