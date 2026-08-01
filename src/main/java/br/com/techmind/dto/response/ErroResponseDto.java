package br.com.techmind.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class ErroResponseDto {

    private LocalDateTime timestamp;
    private Integer status;
    private String mensagem;
    private List<String> detalhes;

}