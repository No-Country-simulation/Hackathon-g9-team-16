package br.com.techmind.exception;

/**
 * Exceção lançada quando um conteúdo solicitado
 * não é encontrado na base de dados.
 */
public class ConteudoNotFoundException extends RuntimeException{

    public ConteudoNotFoundException(String mensagem){
        super(mensagem);
    }
}
