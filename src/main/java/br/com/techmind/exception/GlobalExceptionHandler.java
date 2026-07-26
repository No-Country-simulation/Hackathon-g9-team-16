package br.com.techmind.exception;

import br.com.techmind.dto.response.ErroResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe responsável pelo tratamento global
 * das exceções lançadas pela aplicação.
 *
 * <p>
 * Centraliza os retornos de erro da API,
 * garantindo respostas padronizadas para o cliente.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Trata quando um conteúdo solicitado
     * não existe na base de dados.
     *
     * @param ex exceção de conteúdo não encontrado
     * @return resposta HTTP 404 com detalhes do erro
     */
    @ExceptionHandler(ConteudoNotFoundException.class)
    public ResponseEntity<ErroResponseDto> handlerConteudoNotFound(
            ConteudoNotFoundException ex) {


        ErroResponseDto resposta = ErroResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .mensagem("Conteúdo não encontrado.")
                .detalhes(List.of(ex.getMessage()))
                .build();


        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resposta);
    }



    /**
     * Trata falhas na comunicação
     * com o serviço externo de Inteligência Artificial.
     *
     * @param ex exceção gerada durante integração Python
     * @return resposta HTTP 502 informando indisponibilidade do serviço
     */
    @ExceptionHandler(PythonIntegrationException.class)
    public ResponseEntity<ErroResponseDto> handlePythonIntegrationException(
            PythonIntegrationException ex) {


        ErroResponseDto resposta = ErroResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_GATEWAY.value())
                .mensagem("Serviço de Inteligência Artificial indisponível.")
                .detalhes(List.of(
                        "Não foi possível processar o conteúdo no momento. Tente novamente mais tarde."
                ))
                .build();


        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(resposta);
    }



    /**
     * Trata erros de validação dos campos enviados pelo cliente.
     *
     * @param ex exceção gerada pelo Bean Validation
     * @return resposta HTTP 400 com os campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> handleValidationException(
            MethodArgumentNotValidException ex) {


        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();


        ErroResponseDto resposta = ErroResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem("Dados enviados são inválidos.")
                .detalhes(erros)
                .build();


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> handleGenericException(Exception ex){

        ErroResponseDto resposta = ErroResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .mensagem("Erro interno no servidor.")
                .detalhes(List.of(
                        "Ocorreu um erro inesperado. Tente novamente mais tarde."
                ))
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resposta);
    }

}