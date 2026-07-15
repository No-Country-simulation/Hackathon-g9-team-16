package br.com.techmind.database.repository;

import br.com.techmind.database.model.ConteudoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IConteudoRepository  extends JpaRepository<ConteudoEntity, Long>  {


}
