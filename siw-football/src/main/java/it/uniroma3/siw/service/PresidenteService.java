package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.repository.PresidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PresidenteService {

    @Autowired
    private PresidenteRepository presidenteRepository;

    public Presidente save(Presidente presidente) {
        return presidenteRepository.save(presidente);
    }

    public Presidente findById(String id) {
        Optional<Presidente> pres = presidenteRepository.findById(id);
        if (pres.isEmpty()){
            return null;
        }
        return pres.get();
    }

    public boolean existsByCodiceFiscale(String codiceFiscale){
        return presidenteRepository.existsById(codiceFiscale);
    }
}
