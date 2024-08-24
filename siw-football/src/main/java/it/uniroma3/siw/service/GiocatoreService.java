package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.repository.GiocatoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiocatoreService {

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    public Iterable<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).get();
    }
}
