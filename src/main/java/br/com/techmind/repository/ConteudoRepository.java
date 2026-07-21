package br.com.techmind.repository;


import br.com.techmind.model.entity.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {
    List<Conteudo> findByCategoria(String categoria);
}