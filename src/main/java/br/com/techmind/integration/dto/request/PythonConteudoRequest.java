package br.com.techmind.integration.dto.request;


import lombok.*;

/**
 * DTO responsável por representar os dados enviados
 * pela API Java para o serviço de Inteligência Artificial em Python.
 *
 * <p>
 * Contém as informações necessárias para que o modelo
 * realize a classificação do conteúdo técnico.
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PythonConteudoRequest {
    // O que o python espera receber
    private String titulo;
    private String texto;
}
