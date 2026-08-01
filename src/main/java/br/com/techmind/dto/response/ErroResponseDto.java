package br.com.techmind.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ErroResponseDto {

    private LocalDateTime timestamp;
    private Integer status;
    private String mensagem;
    private List<String> detalhes;

    public ErroResponseDto() {
    }

    public ErroResponseDto(LocalDateTime timestamp, Integer status, String mensagem, List<String> detalhes) {
        this.timestamp = timestamp;
        this.status = status;
        this.mensagem = mensagem;
        this.detalhes = detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<String> detalhes) {
        this.detalhes = detalhes;
    }
}