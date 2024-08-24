package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.service.GiocatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class GiocatoreController {

    @Autowired
    private GiocatoreService giocatoreService;

    @GetMapping("/admin/formNewGiocatore")
    public String formNewGiocatore(Model model,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("authentication", userDetails);
        model.addAttribute("giocatore", new Giocatore());
        return "admin/formNewGiocatore";
    }

    @PostMapping("/admin/saveGiocatore")
    public String saveGiocatore(@ModelAttribute Giocatore giocatore,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        giocatoreService.save(giocatore);
        return "redirect:/admin/giocatori";
    }

    @GetMapping("/admin/giocatori")
    public String giocatori(Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Iterable<Giocatore> giocatori = giocatoreService.findAll();

        List<Giocatore> giocatoriLista = new ArrayList<>();
        giocatori.forEach(giocatoriLista::add);

        model.addAttribute("authentication", userDetails);
        model.addAttribute("giocatori", giocatoriLista);

        return "admin/giocatori";
    }

    @GetMapping("/president/giocatoriSvincolati")
    public String giocatoriSvincolati(Model model,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){

        Iterable<Giocatore> giocatori = giocatoreService.findAll();

        List<Giocatore> giocatoriLista = new ArrayList<>();
        giocatori.forEach(giocatoriLista::add);

        List<Giocatore> giocatoriSvincolati = new ArrayList<>();

        for (Giocatore giocatore : giocatoriLista) {
            // Ordina i tesseramenti per data di fine tesseramento utilizzando un comparatore interno
            giocatore.getTesseramenti().sort((t1, t2) -> {
                if (t1.getFineTesseramento() == null && t2.getFineTesseramento() == null) {
                    return 0;
                }
                if (t1.getFineTesseramento() == null) {
                    return 1;
                }
                if (t2.getFineTesseramento() == null) {
                    return -1;
                }
                return t2.getFineTesseramento().compareTo(t1.getFineTesseramento());
            });

            // Ottieni il tesseramento più recente
            GiocatoreTesserato ultimoTesseramento = giocatore.getTesseramenti().isEmpty() ? null : giocatore.getTesseramenti().get(0);

            if (ultimoTesseramento == null || ultimoTesseramento.getFineTesseramento().isBefore(LocalDate.now())) {
                // Aggiungi il giocatore alla lista degli svincolati
                giocatoriSvincolati.add(giocatore);
            }
        }

        model.addAttribute("giocatoriSvincolati", giocatoriSvincolati);
        model.addAttribute("authentication", userDetails);

        return "/president/giocatoriSvincolati";
    }

}
