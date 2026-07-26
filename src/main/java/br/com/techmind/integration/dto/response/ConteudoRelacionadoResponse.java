package br.com.techmind.integration.dto.response;


import lombok.*;

/**
 * Representa um conteúdo relacionado identificado
 * pela Inteligência Artificial.
 *
 * <p>
 * O relacionamento é baseado no cálculo de similaridade
 * realizado pelo modelo de IA.
 * </p>
 *
 * <p>
 * Esses dados são retornados apenas na resposta da API
 * e não são persistidos no banco de dados.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConteudoRelacionadoResponse {

    private String titulo;

    private String area;

    private String subarea;

    private Double similaridade;

    private String nivelSimilaridade;
}
