package br.com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição para análise de conteúdo técnico")
public class ConteudoRequest {

    @Schema(description = "Título do artigo, postagem ou documento", example = "Introdução ao Spring Boot e APIs RESTful")
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 200, message = "O título deve possuir entre 3 e 200 caracteres")
    private String titulo;

    @Schema(description = "Texto completo do conteúdo a ser categorizado e analisado", example = "Spring Boot facilita a criação de aplicações Spring baseadas em microsserviços prontas para produção...")
    @NotBlank(message = "O texto é obrigatório")
    @Size(min = 10, max = 50000, message = "O texto deve possuir entre 10 e 50000 caracteres")
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