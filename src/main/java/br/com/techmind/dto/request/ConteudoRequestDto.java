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
    @Size(max = 255)
    private String titulo;

    @NotBlank(message = "Insira a descrição do conteúdo.")
    @Size(min = 10)
    private String texto;


}