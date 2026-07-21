package br.com.techmind.service;


import br.com.techmind.dto.request.ConteudoRequest;
import br.com.techmind.dto.response.ConteudoResponse;
import br.com.techmind.model.entity.Conteudo;
import br.com.techmind.repository.ConteudoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class IAService {

    @Autowired
    private ConteudoRepository conteudoRepository;

    public ConteudoResponse classificar(ConteudoRequest request) {
        // ⚠️ AQUI VOCÊ AJUSTA O MOCK PARA SUA LÓGICA DE NEGÓCIO
        ConteudoResponse response = new ConteudoResponse();
        response.setCategoria("Backend"); // ← Altere para a categoria que desejar
        response.setProbabilidade(0.95);
        response.setPalavras_chave(Arrays.asList("Java", "Spring", "API")); // ← Altere aqui

        // Salva o histórico
        String palavras = response.getPalavras_chave().stream()
                .collect(Collectors.joining(", "));

        Conteudo conteudo = new Conteudo(
                request.getTitulo(),
                request.getTexto(),
                response.getCategoria(),
                response.getProbabilidade(),
                palavras
        );
        conteudoRepository.save(conteudo);

        return response;
    }
}
