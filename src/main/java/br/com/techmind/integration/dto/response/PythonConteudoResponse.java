package br.com.techmind.integration.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * DTO responsável por representar a resposta retornada
 * pelo serviço de Inteligência Artificial.
 *
 * <p>
 * Contém a classificação do conteúdo,
 * palavras-chave extraídas e conteúdos relacionados
 * encontrados pelo modelo.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PythonConteudoResponse {

    private String titulo;

    private ClassificacaoResponse classificacao;

    @JsonProperty("palavras_chave")
    private List<String> palavrasChave;

    @JsonProperty("conteudos_relacionados")
    private List<ConteudoRelacionadoResponse> conteudosRelacionados;
}
