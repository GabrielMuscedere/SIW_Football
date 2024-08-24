package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Giocatore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {


}
