package br.com.techmind.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ConteudoRequest {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "O texto é obrigatório")
    private String texto;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}