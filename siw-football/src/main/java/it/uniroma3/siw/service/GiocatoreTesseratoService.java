package it.uniroma3.siw.service;

import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.repository.GiocatoreTesseratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiocatoreTesseratoService {

    @Autowired
    private GiocatoreTesseratoRepository giocatoreTesseratoRepository;

    public GiocatoreTesserato save(GiocatoreTesserato giocatoreTesserato) {
        return giocatoreTesseratoRepository.save(giocatoreTesserato);
    }
}
