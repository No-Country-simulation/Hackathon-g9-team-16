package br.com.techmind.dto.response;

import br.com.techmind.integration.dto.response.ConteudoRelacionadoResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private String areaPrincipal;

    private String subarea;

    private Double confiancaArea;

    private Double confiancaSubarea;

    private List<String> palavrasChave;

    private List<ConteudoRelacionadoResponse> conteudosRelacionados;


}