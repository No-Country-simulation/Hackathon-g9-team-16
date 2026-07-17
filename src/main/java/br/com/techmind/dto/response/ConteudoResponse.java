package br.com.techmind.dto.response;

import java.util.List;

public class ConteudoResponse {

    private String categoria;
    private Double probabilidade;
    private List<String> palavrasChave;
    private String resumo;

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