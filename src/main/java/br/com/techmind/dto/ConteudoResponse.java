package br.com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Objeto de resposta com o resultado da análise e categorização do conteúdo")
public class ConteudoResponse {

    @Schema(description = "Categoria identificada do conteúdo técnico", example = "Backend")
    private String categoria;

    @Schema(description = "Grau de confiança/probabilidade da classificação (entre 0.0 e 1.0)", example = "0.95")
    private Double probabilidade;

    @Schema(description = "Lista de palavras-chave mais relevantes extraídas do texto", example = "[\"Java\", \"Spring Boot\", \"API REST\"]")
    private List<String> palavrasChave;

    @Schema(description = "Resumo executivo extraído do conteúdo enviado", example = "Introdução ao desenvolvimento de APIs REST utilizando Spring Boot.")
    private String resumo;

    public ConteudoResponse() {
    }

    public ConteudoResponse(String categoria, Double probabilidade, List<String> palavrasChave, String resumo) {
        this.categoria = categoria;
        this.probabilidade = probabilidade;
        this.palavrasChave = palavrasChave;
        this.resumo = resumo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getProbabilidade() {
        return probabilidade;
    }

    public void setProbabilidade(Double probabilidade) {
        this.probabilidade = probabilidade;
    }

    public List<String> getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(List<String> palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }
}