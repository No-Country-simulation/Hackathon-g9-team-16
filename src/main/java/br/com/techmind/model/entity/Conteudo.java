package br.com.techmind.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conteudos")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    private String categoria;

    private Double probabilidade;

    @Column(name = "palavras_chave")
    private String palavrasChave;

    @Column(name = "data_classificacao")
    private LocalDateTime dataClassificacao;

    public Conteudo() {}

    public Conteudo(String titulo, String texto, String categoria, Double probabilidade, String palavrasChave) {
        this.titulo = titulo;
        this.texto = texto;
        this.categoria = categoria;
        this.probabilidade = probabilidade;
        this.palavrasChave = palavrasChave;
        this.dataClassificacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Double getProbabilidade() { return probabilidade; }
    public void setProbabilidade(Double probabilidade) { this.probabilidade = probabilidade; }
    public String getPalavrasChave() { return palavrasChave; }
    public void setPalavrasChave(String palavrasChave) { this.palavrasChave = palavrasChave; }
    public LocalDateTime getDataClassificacao() { return dataClassificacao; }
    public void setDataClassificacao(LocalDateTime dataClassificacao) { this.dataClassificacao = dataClassificacao; }
}