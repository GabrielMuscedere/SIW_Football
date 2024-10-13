package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.model.Squadra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquadraRepository extends CrudRepository<Squadra, Long> {

    @Query("SELECT g FROM GiocatoreTesserato g WHERE g.giocatore.nome LIKE CONCAT('%', :nome, '%')  AND g.squadra.id = :idSquadra")
    List<GiocatoreTesserato> findByNome(@Param("nome") String nome, @Param("idSquadra") Long idSquadra);
}
