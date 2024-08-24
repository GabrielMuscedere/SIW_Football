package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Presidente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresidenteRepository extends CrudRepository<Presidente, String> {

}
