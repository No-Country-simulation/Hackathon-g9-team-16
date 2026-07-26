package br.com.techmind.integration.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Representa os dados de classificação retornados
 * pela Inteligência Artificial.
 *
 * <p>
 * Contém a área principal, subárea e os níveis
 * de confiança calculados pelo modelo de classificação.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassificacaoResponse {

    @JsonProperty("area_principal")
    private String areaPrincipal;

    private String subarea;

    @JsonProperty("confianca_area")
    private Double confiancaArea;

    @JsonProperty("confianca_subarea")
    private Double confiancaSubarea;
}
