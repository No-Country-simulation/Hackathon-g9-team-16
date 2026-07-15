package br.com.techmind.exception;

public class ConteudoNotFoundException extends RuntimeException{

    public ConteudoNotFoundException(String mensagem){
        super(mensagem);
    }
}
