package br.com.techmind.exception;

import br.com.techmind.dto.response.ErroResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Falha de validação na requisição {}: {}", request.getRequestURI(), ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = createErrorBody(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida",
                "Um ou mais campos contêm erros de validação.",
                request.getRequestURI()
        );
        body.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(PythonIntegrationException.class)
    public ResponseEntity<ErroResponseDto> handlePythonIntegrationException(
            PythonIntegrationException ex) {

        log.error("Falha na integração Python: ", ex);

        ErroResponseDto resposta = new ErroResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_GATEWAY.value(),
                "Serviço de Inteligência Artificial indisponível.",
                List.of("Não foi possível processar o conteúdo no momento. Tente novamente mais tarde.")
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resposta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarJsonMalformatado(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("Corpo da requisição JSON malformado: {}", ex.getMessage());

        Map<String, Object> body = createErrorBody(
                HttpStatus.BAD_REQUEST.value(),
                "JSON Malformado",
                "O corpo da requisição JSON está malformado ou ausente.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> tratarMetodoNaoSuportado(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        Map<String, Object> body = createErrorBody(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Método Não Permitido",
                "O método HTTP " + ex.getMethod() + " não é suportado para esta rota.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroGenerico(
            Exception ex, HttpServletRequest request) {

        log.error("Erro interno não tratado na rota {}: ", request.getRequestURI(), ex);

        Map<String, Object> body = createErrorBody(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, Object> createErrorBody(int status, String error, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}