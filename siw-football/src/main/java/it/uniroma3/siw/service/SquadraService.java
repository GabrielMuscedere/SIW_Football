package it.uniroma3.siw.service;

import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.SquadraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SquadraService {

    @Autowired
    private SquadraRepository squadraRepository;

    public Squadra save(Squadra squadra) {
        return squadraRepository.save(squadra);
    }

    public Iterable<Squadra> findAll() {
        return squadraRepository.findAll();
    }

    public Squadra findById(Long id) {
        return squadraRepository.findById(id).get();
    }

    public List<GiocatoreTesserato> findByNome(String nome, Long idSquadra) {
        return squadraRepository.findByNome(nome, idSquadra);
    }
}
