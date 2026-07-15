package br.com.techmind.service;

import br.com.techmind.database.model.ConteudoEntity;
import br.com.techmind.database.repository.IConteudoRepository;
import br.com.techmind.dto.request.ConteudoRequestDto;
import br.com.techmind.dto.response.ConteudoResponseDto;
import br.com.techmind.exception.ConteudoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ConteudoService {

    private final IConteudoRepository conteudoRepository;

    //metodo para conversão de Entity para DTO
    private ConteudoResponseDto toResponse(ConteudoEntity conteudoEntity){
        return ConteudoResponseDto.builder()
                .id(conteudoEntity.getId())
                .titulo(conteudoEntity.getTitulo())
                .texto(conteudoEntity.getTexto())
                .dataCriacao(conteudoEntity.getDataCriacao())
                .build();
    }

    //save
    public ConteudoResponseDto save(ConteudoRequestDto conteudoRequestDto) {

        ConteudoEntity conteudoEntity = ConteudoEntity.builder()
                .titulo(conteudoRequestDto.getTitulo())
                .texto(conteudoRequestDto.getTexto())
                .dataCriacao(LocalDateTime.now())
                .build();

        ConteudoEntity conteudoSalvo = conteudoRepository.save(conteudoEntity);

        return toResponse(conteudoSalvo);
    }

    //findAll
    public List<ConteudoResponseDto> findAll (){

        List<ConteudoEntity> listaConteudosEntity  = conteudoRepository.findAll();

        return listaConteudosEntity.stream()
                .map(this::toResponse)
                .toList();
    }

    //findById
    public ConteudoResponseDto findById(Long id){

        ConteudoEntity conteudoEntity = conteudoRepository
                .findById(id)
                .orElseThrow(()->new ConteudoNotFoundException("Conteúdo com ID " + id + " não encontrado."));

        return toResponse(conteudoEntity);
    }

    //delete
    public void delete(Long id){

        ConteudoEntity conteudoEntity = conteudoRepository
                .findById(id)
                .orElseThrow(()->new ConteudoNotFoundException("Conteúdo com ID " + id + " não encontrado."));

        conteudoRepository.delete(conteudoEntity);
    }

    //update
    public ConteudoResponseDto update(Long id, ConteudoRequestDto conteudoRequestDto) {

        ConteudoEntity conteudoEntity = conteudoRepository
                .findById(id)
                .orElseThrow(()->new ConteudoNotFoundException("Conteúdo com ID " + id + " não encontrado."));

        conteudoEntity.setTitulo(conteudoRequestDto.getTitulo());
        conteudoEntity.setTexto(conteudoRequestDto.getTexto());

        ConteudoEntity conteudoAtualizado = conteudoRepository.save(conteudoEntity);

        return toResponse(conteudoAtualizado);
    }
}