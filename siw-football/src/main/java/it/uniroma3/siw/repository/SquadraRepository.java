package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Squadra;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadraRepository extends CrudRepository<Squadra, Long> {

}
