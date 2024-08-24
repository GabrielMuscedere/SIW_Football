package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.GiocatoreTesseratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Controller
public class GiocatoreTesseratoController {


    @Autowired
    private GiocatoreService giocatoreService;

    @Autowired
    private GiocatoreTesseratoService giocatoreTesseratoService;

    public GiocatoreTesseratoController(GiocatoreService giocatoreService) {
        this.giocatoreService = giocatoreService;
    }

    @GetMapping("president/tesseraGiocatore/{idGiocatore}")
    public String tesseramento(@PathVariable Long idGiocatore,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {

        Giocatore giocatore = giocatoreService.findById(idGiocatore);
        GiocatoreTesserato giocatoreTesserato = new GiocatoreTesserato();
        giocatoreTesserato.setGiocatore(giocatore);
        giocatoreTesserato.setInizioTesseramento(LocalDate.now());
        giocatoreTesserato.setFineTesseramento(LocalDate.now().plusYears(2));
        giocatoreTesserato.setSquadra(userDetails.getPresidente().getSquadra());
        giocatoreTesseratoService.save(giocatoreTesserato);

        return "redirect:/president/squadra";
    }


}
