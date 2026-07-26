package br.com.techmind.service;

import br.com.techmind.database.model.ConteudoEntity;
import br.com.techmind.database.repository.IConteudoRepository;
import br.com.techmind.dto.request.ConteudoRequestDto;
import br.com.techmind.dto.response.ConteudoResponseDto;
import br.com.techmind.exception.ConteudoNotFoundException;
import br.com.techmind.exception.PythonIntegrationException;
import br.com.techmind.integration.client.PythonApiClient;
import br.com.techmind.integration.dto.request.PythonConteudoRequest;
import br.com.techmind.integration.dto.response.ClassificacaoResponse;
import br.com.techmind.integration.dto.response.ConteudoRelacionadoResponse;
import br.com.techmind.integration.dto.response.PythonConteudoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * Serviço responsável pelo gerenciamento dos conteúdos técnicos.
 *
 * <p>Essa classe concentra as regras de negócio relacionadas ao cadastro,
 * atualização, consulta e exclusão de conteúdos.</p>
 *
 * <p>Também realiza a integração com a API de Inteligência Artificial
 * responsável pela classificação dos conteúdos.</p>
 */
@Service
@RequiredArgsConstructor
public class ConteudoService {


    private final IConteudoRepository conteudoRepository;
    private final PythonApiClient pythonApiClient;


    /**
     * Converte uma entidade de conteúdo em um DTO de resposta.
     *
     * @param conteudoEntity entidade persistida no banco de dados
     * @param relacionados conteúdos relacionados retornados pela IA
     * @return objeto de resposta contendo os dados do conteúdo
     */
    private ConteudoResponseDto toResponse(
            ConteudoEntity conteudoEntity,
            List<ConteudoRelacionadoResponse> relacionados
    ) {

        return ConteudoResponseDto.builder()
                .id(conteudoEntity.getId())
                .titulo(conteudoEntity.getTitulo())
                .texto(conteudoEntity.getTexto())
                .dataCriacao(conteudoEntity.getDataCriacao())

                .areaPrincipal(conteudoEntity.getAreaPrincipal())
                .subarea(conteudoEntity.getSubarea())
                .confiancaArea(conteudoEntity.getConfiancaArea())
                .confiancaSubarea(conteudoEntity.getConfiancaSubarea())

                .palavrasChave(
                        converterPalavrasChave(conteudoEntity.getPalavrasChave())
                )

                .conteudosRelacionados(relacionados)

                .build();
    }


    /**
     * Converte as palavras-chave armazenadas como String no banco
     * para uma lista utilizada no retorno da API.
     *
     * @param palavrasChave palavras separadas por vírgula
     * @return lista de palavras-chave formatadas
     */
    private List<String> converterPalavrasChave(String palavrasChave) {

        if (palavrasChave == null || palavrasChave.isBlank()) {
            return List.of();
        }

        return Arrays.stream(palavrasChave.split(","))
                .map(String::trim)
                .filter(palavra -> !palavra.isBlank())
                .toList();
    }


    /**
     * Busca um conteúdo pelo identificador informado.
     *
     * @param id identificador do conteúdo
     * @return entidade encontrada
     * @throws ConteudoNotFoundException caso o conteúdo não exista
     */
    private ConteudoEntity buscarPorId(Long id) {

        return conteudoRepository.findById(id)
                .orElseThrow(() ->
                        new ConteudoNotFoundException(
                                "Conteúdo não encontrado com o ID "
                                        + id +
                                        ". Verifique a numeração informada."
                        )
                );
    }


    /**
     * Envia um conteúdo para a API Python responsável pelo processamento da IA.
     *
     * @param dto dados enviados pelo usuário
     * @return resultado da classificação realizada pela IA
     * @throws PythonIntegrationException caso ocorra falha na comunicação
     */
    private PythonConteudoResponse processarIA(
            ConteudoRequestDto dto
    ) {

        try {

            PythonConteudoRequest request =
                    PythonConteudoRequest.builder()
                            .titulo(dto.getTitulo())
                            .texto(dto.getTexto())
                            .build();


            PythonConteudoResponse resposta =
                    pythonApiClient.processarConteudo(request);


            if (resposta == null ||
                    resposta.getClassificacao() == null) {

                throw new PythonIntegrationException(
                        "A IA não retornou uma classificação válida."
                );
            }


            return resposta;


        } catch (Exception ex) {

            throw new PythonIntegrationException(
                    "Erro ao comunicar com a API Python.",
                    ex
            );
        }
    }


    /**
     * Preenche a entidade com os dados retornados pela Inteligência Artificial.
     *
     * @param conteudo entidade que receberá os dados classificados
     * @param respostaIA resposta retornada pelo serviço Python
     */
    private void preencherDadosIA(
            ConteudoEntity conteudo,
            PythonConteudoResponse respostaIA
    ) {


        ClassificacaoResponse classificacao =
                respostaIA.getClassificacao();


        conteudo.setAreaPrincipal(
                classificacao.getAreaPrincipal()
        );

        conteudo.setSubarea(
                classificacao.getSubarea()
        );

        conteudo.setConfiancaArea(
                classificacao.getConfiancaArea()
        );

        conteudo.setConfiancaSubarea(
                classificacao.getConfiancaSubarea()
        );


        List<String> palavras =
                respostaIA.getPalavrasChave();


        conteudo.setPalavrasChave(
                palavras == null || palavras.isEmpty()
                        ? null
                        : String.join(",", palavras)
        );
    }


    /**
     * Adiciona uma classificação textual para a similaridade
     * dos conteúdos relacionados.
     *
     * @param relacionados lista de conteúdos relacionados retornados pela IA
     * @return lista contendo o nível de similaridade calculado
     */
    private List<ConteudoRelacionadoResponse> preencherNivelSimilaridade(
            List<ConteudoRelacionadoResponse> relacionados
    ) {

        if (relacionados == null) {
            return List.of();
        }


        return relacionados.stream()
                .map(relacionado -> {

                    relacionado.setNivelSimilaridade(
                            classificarSimilaridade(
                                    relacionado.getSimilaridade()
                            )
                    );

                    return relacionado;

                })
                .toList();
    }


    /**
     * Define o nível de similaridade baseado no percentual retornado pela IA.
     *
     * @param valor percentual de similaridade
     * @return classificação textual da similaridade
     */
    private String classificarSimilaridade(Double valor) {

        if (valor == null) {
            return "NÃO CLASSIFICADO";
        }


        if (valor >= 0.70) {
            return "ALTA";
        }


        if (valor >= 0.40) {
            return "MEDIA";
        }


        return "BAIXA";
    }

    /**
     * Salva um novo conteúdo após realizar a classificação pela Inteligência Artificial.
     *
     * <p>O fluxo realizado é:</p>
     * <ul>
     *     <li>Envia o conteúdo para a API Python;</li>
     *     <li>Recebe a classificação da IA;</li>
     *     <li>Persiste o conteúdo classificado;</li>
     *     <li>Retorna os dados juntamente com conteúdos relacionados.</li>
     * </ul>
     *
     * @param conteudoRequestDto dados enviados pelo usuário
     * @return conteúdo salvo com as informações classificadas
     */
    public ConteudoResponseDto save(
            ConteudoRequestDto conteudoRequestDto
    ) {

        PythonConteudoResponse respostaIA =
                processarIA(conteudoRequestDto);


        ConteudoEntity conteudoEntity =
                ConteudoEntity.builder()
                        .titulo(conteudoRequestDto.getTitulo())
                        .texto(conteudoRequestDto.getTexto())
                        .dataCriacao(LocalDateTime.now())
                        .build();


        preencherDadosIA(
                conteudoEntity,
                respostaIA
        );


        ConteudoEntity conteudoSalvo =
                conteudoRepository.save(conteudoEntity);


        return toResponse(
                conteudoSalvo,
                preencherNivelSimilaridade(
                        respostaIA.getConteudosRelacionados()
                )
        );
    }



    /**
     * Retorna todos os conteúdos cadastrados no sistema.
     *
     * <p>
     * Os conteúdos relacionados não são retornados nesta consulta,
     * pois eles são gerados dinamicamente pela IA e não ficam armazenados.
     * </p>
     *
     * @return lista contendo todos os conteúdos cadastrados
     */
    public List<ConteudoResponseDto> findAll() {

        List<ConteudoEntity> conteudos =
                conteudoRepository.findAll();


        return conteudos.stream()
                .map(conteudo ->
                        toResponse(
                                conteudo,
                                List.of()
                        )
                )
                .toList();
    }



    /**
     * Busca um conteúdo específico pelo identificador.
     *
     * @param id identificador do conteúdo
     * @return conteúdo encontrado
     */
    public ConteudoResponseDto findById(
            Long id
    ) {

        ConteudoEntity conteudo =
                buscarPorId(id);


        return toResponse(
                conteudo,
                List.of()
        );
    }



    /**
     * Remove um conteúdo existente.
     *
     * @param id identificador do conteúdo que será removido
     */
    public void delete(
            Long id
    ) {

        ConteudoEntity conteudo =
                buscarPorId(id);


        conteudoRepository.delete(conteudo);
    }



    /**
     * Atualiza um conteúdo existente e realiza uma nova classificação pela IA.
     *
     * <p>
     * Como o texto pode ter sido alterado, uma nova análise é realizada
     * para atualizar categoria, subcategoria, confiança e palavras-chave.
     * </p>
     *
     * @param id identificador do conteúdo
     * @param conteudoRequestDto novos dados do conteúdo
     * @return conteúdo atualizado
     */
    public ConteudoResponseDto update(
            Long id,
            ConteudoRequestDto conteudoRequestDto
    ) {


        ConteudoEntity conteudoEntity =
                buscarPorId(id);


        conteudoEntity.setTitulo(
                conteudoRequestDto.getTitulo()
        );

        conteudoEntity.setTexto(
                conteudoRequestDto.getTexto()
        );


        PythonConteudoResponse respostaIA =
                processarIA(conteudoRequestDto);


        preencherDadosIA(
                conteudoEntity,
                respostaIA
        );


        ConteudoEntity conteudoAtualizado =
                conteudoRepository.save(conteudoEntity);



        return toResponse(
                conteudoAtualizado,
                preencherNivelSimilaridade(
                        respostaIA.getConteudosRelacionados()
                )
        );
    }

}