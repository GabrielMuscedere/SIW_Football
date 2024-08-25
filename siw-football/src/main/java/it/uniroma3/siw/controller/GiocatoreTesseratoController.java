package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.GiocatoreTesseratoService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class GiocatoreTesseratoController {


    @Autowired
    private GiocatoreService giocatoreService;

    @Autowired
    private GiocatoreTesseratoService giocatoreTesseratoService;

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private PresidenteService presidenteService;

    @PostMapping("/president/tesseraGiocatore/{idGiocatore}/{codFiscale}/{idSquadra}")
    public String tesseramento(@PathVariable Long idGiocatore,
                               @PathVariable String codFiscale,
                               @PathVariable Long idSquadra,
                               Model model) {

        System.out.println("SONO ENTRATO NEL CONTROLLER 1");
        Giocatore giocatore = giocatoreService.findById(idGiocatore);
        Presidente presidente = presidenteService.findById(codFiscale);
        Squadra squadra = squadraService.findById(idSquadra);

        GiocatoreTesserato giocatoreTesserato = new GiocatoreTesserato();
        giocatoreTesserato.setGiocatore(giocatore);
        giocatoreTesserato.setInizioTesseramento(LocalDate.now());
        giocatoreTesserato.setFineTesseramento(LocalDate.now().plusYears(2));
        giocatoreTesserato.setSquadra(squadra);
        giocatoreTesseratoService.save(giocatoreTesserato);

        squadra.getGiocatori().add(giocatoreTesserato);
        squadraService.save(squadra);

        giocatore.getTesseramenti().add(giocatoreTesserato);
        giocatoreService.save(giocatore);


        System.out.println("SONO ENTRATO NEL CONTROLLER");
        return "redirect:/president/squadra";
    }

    @GetMapping("/president/terminaTesseramento/{idTesseramento}")
    public String terminaTesseramento(@PathVariable Long idTesseramento){
        GiocatoreTesserato tesseramento = giocatoreTesseratoService.findById(idTesseramento);

        tesseramento.setInizioTesseramento(LocalDate.now().minusDays(1));
        tesseramento.setFineTesseramento(LocalDate.now().minusDays(1));
        giocatoreTesseratoService.save(tesseramento);

        tesseramento.getSquadra().getGiocatori().remove(tesseramento);
        squadraService.save(tesseramento.getSquadra());

        return "redirect:/president/squadra";
    }


}
