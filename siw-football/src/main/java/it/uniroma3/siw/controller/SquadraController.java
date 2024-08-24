package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.PresidenteRepository;
import it.uniroma3.siw.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
public class SquadraController {

    @Autowired
    private PresidenteRepository presidenteRepository;

    @Autowired
    private SquadraService squadraService;

    @GetMapping("/admin/formNewSquadra")
    public String formNewSquadra(Model model,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Iterable<Presidente> presidenti = presidenteRepository.findAll();

        List<Presidente> presidentiLista = new ArrayList<>();
        presidenti.forEach(presidentiLista::add);

        Iterator<Presidente> iterator = presidentiLista.iterator();
        while (iterator.hasNext()) {
            Presidente presidente = iterator.next();
            if (presidente.getSquadra() != null) {
                iterator.remove(); // Rimuove in modo sicuro l'elemento corrente
            }
        }

        model.addAttribute("squadra", new Squadra());
        model.addAttribute("authentication", customUserDetails);
        model.addAttribute("presidenti", presidentiLista);
        return "admin/formNewSquadra";
    }

    @PostMapping("/admin/saveSquadra")
    public String saveSquadra(@ModelAttribute("squadra") Squadra squadra){
        Presidente presidente = squadra.getPresidente();

        squadraService.save(squadra);

        presidente.setSquadra(squadra);
        presidenteRepository.save(presidente);

        return "redirect:/admin/indexAdmin";
    }


}
