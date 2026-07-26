package br.com.techmind.controller;

import br.com.techmind.dto.request.ConteudoRequestDto;
import br.com.techmind.dto.response.ConteudoResponseDto;
import br.com.techmind.service.ConteudoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelos endpoints REST
 * relacionados aos conteúdos técnicos.
 *
 * <p>
 * Recebe as requisições HTTP, valida os dados enviados
 * e delega o processamento das regras de negócio
 * para a camada de serviço.
 * </p>
 */
@RestController
@RequestMapping("/v1/conteudos")
@RequiredArgsConstructor
public class ConteudoController {

    private final ConteudoService conteudoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConteudoResponseDto save(
            @Valid @RequestBody ConteudoRequestDto conteudoRequestDto) {

        return conteudoService.save(conteudoRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ConteudoResponseDto> findAll(){
    return conteudoService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConteudoResponseDto findById(@PathVariable Long id){

        return conteudoService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        conteudoService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConteudoResponseDto update(@PathVariable Long id, @Valid @RequestBody ConteudoRequestDto conteudoRequestDto){
        return conteudoService.update(id, conteudoRequestDto);
    }

}