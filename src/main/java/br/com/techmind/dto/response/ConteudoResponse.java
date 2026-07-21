package br.com.techmind.dto.response;

import java.util.List;

public class ConteudoResponse {
    private String categoria;
    private Double probabilidade;
    private List<String> palavras_chave;

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Double getProbabilidade() { return probabilidade; }
    public void setProbabilidade(Double probabilidade) { this.probabilidade = probabilidade; }
    public List<String> getPalavras_chave() { return palavras_chave; }
    public void setPalavras_chave(List<String> palavras_chave) { this.palavras_chave = palavras_chave; }
}