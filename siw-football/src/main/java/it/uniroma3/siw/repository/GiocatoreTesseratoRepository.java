package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.GiocatoreTesserato;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiocatoreTesseratoRepository extends CrudRepository<GiocatoreTesserato, Long> {

}
