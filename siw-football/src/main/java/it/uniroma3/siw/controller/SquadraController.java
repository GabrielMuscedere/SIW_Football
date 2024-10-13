package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.GiocatoreTesserato;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.PresidenteRepository;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.validator.ModificaSquadraValidator;
import it.uniroma3.siw.validator.SquadraValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    @Autowired
    private SquadraValidator squadraValidator;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private PresidenteService presidenteService;

    @Autowired
    private ModificaSquadraValidator modificaSquadraValidator;

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
    public String saveSquadra(@ModelAttribute("squadra") Squadra squadra,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("file") MultipartFile file){

        this.squadraValidator.validate(squadra, bindingResult);

        if (!bindingResult.hasErrors()) {
            try {
                if (!file.isEmpty()) {
                    Path dirPath = Paths.get(uploadDir);
                    if (!Files.exists(dirPath)) {
                        Files.createDirectories(dirPath);
                    }

                    String filename = file.getOriginalFilename();
                    Path filePath = dirPath.resolve(filename);
                    Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                    // Imposta il percorso dell'immagine come URL accessibile
                    String fileUrl = filename;
                    squadra.setImageUrl(fileUrl);
                }

                this.squadraService.save(squadra);

                Presidente presidente = squadra.getPresidente();
                presidente.setSquadra(squadra);
                presidenteRepository.save(presidente);

                return "redirect:/admin/indexAdmin";

            } catch (IOException e) {
               squadra.setImageUrl("");
            }
        } else {
            bindingResult.getFieldErrors().forEach(error ->
                    redirectAttributes.addFlashAttribute(error.getField() + "Error", error.getDefaultMessage())
            );
            return "redirect:/admin/formNewSquadra";
        }

        return "redirect:/admin/indexAdmin";
    }

    @GetMapping("/admin/squadre")
    public String squadre(Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        Iterable<Squadra> squadre = squadraService.findAll();

        List<Squadra> squadreLista = new ArrayList<>();
        squadre.forEach(squadreLista::add);

        model.addAttribute("authentication", userDetails);
        model.addAttribute("squadre", squadreLista);

        return "admin/squadre";
    }

    @GetMapping("/president/squadra")
    public String squadraPresidente(Model model,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){

        model.addAttribute("authentication", userDetails);
        model.addAttribute("numero_giocatori", userDetails.getPresidente().getSquadra().getGiocatori().size());

        return "president/squadra";
    }

    @GetMapping("/squadra/{idSquadra}")
    public String squadra(Model model,
                          @PathVariable Long idSquadra,
                          @AuthenticationPrincipal CustomUserDetails userDetails){

        Squadra squadra = squadraService.findById(idSquadra);
        int numero_giocatori = squadra.getGiocatori().size();

        model.addAttribute("authentication", userDetails);
        model.addAttribute("squadra", squadra);
        model.addAttribute("giocatori", squadra.getGiocatori());
        model.addAttribute("numero_giocatori", numero_giocatori);

        return "squadra";
    }


    @GetMapping("/admin/gestisciSquadra/{idSquadra}")
    public String manageTeam(@PathVariable Long idSquadra,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model){
        Squadra squadra = squadraService.findById(idSquadra);
        model.addAttribute("authentication", userDetails);
        model.addAttribute("squadra", squadra);

        Iterable<Presidente> presidenti = presidenteRepository.findAll();
        List<Presidente> presidentiLista = new ArrayList<>();
        presidenti.forEach(presidentiLista::add);

        List<Presidente> presidentiDisponibili = new ArrayList<>();
        for (Presidente presidente : presidentiLista) {
            if (presidente.getSquadra() == null) {
                presidentiDisponibili.add(presidente);
            }
        }

        model.addAttribute("presidenti", presidentiDisponibili);

        return "admin/gestisciSquadra";
    }

    @Transactional
    @PostMapping("/admin/salvaModifiche/{idSquadra}")
    public String saveModifiche(@PathVariable Long idSquadra,
                                @ModelAttribute("squadra") Squadra updatedSquadra,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @RequestParam MultipartFile file){

        updatedSquadra.setImageUrl(file.getOriginalFilename());
        this.modificaSquadraValidator.validate(updatedSquadra, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    redirectAttributes.addFlashAttribute(error.getField() + "Error", error.getDefaultMessage())
            );
            return "redirect:/admin/gestisciSquadra/" + idSquadra;
        }

        Squadra existSquadra = squadraService.findById(idSquadra);

        if (updatedSquadra.getPresidente() != null && !existSquadra.getPresidente().getCodiceFiscale().equals(updatedSquadra.getPresidente().getCodiceFiscale())){
            if (existSquadra.getPresidente() != null) {
                Presidente presidenteVecchio = existSquadra.getPresidente();
                presidenteVecchio.setSquadra(null);
                presidenteRepository.save(presidenteVecchio);
            }
            Presidente presidenteNuovo = updatedSquadra.getPresidente();
            existSquadra.setPresidente(presidenteNuovo);
            presidenteNuovo.setSquadra(existSquadra);
            presidenteService.save(presidenteNuovo);
        }

        if (updatedSquadra.getAnnoFondazione() != null) existSquadra.setAnnoFondazione(updatedSquadra.getAnnoFondazione());
        if (!updatedSquadra.getIndirizzoSede().equals(existSquadra.getIndirizzoSede())) existSquadra.setIndirizzoSede(updatedSquadra.getIndirizzoSede());
        if (!updatedSquadra.getNome().equals(existSquadra.getNome())) existSquadra.setNome(updatedSquadra.getNome());

        if (updatedSquadra.getImageUrl() != null) { //vedere perche con !bindingResult.hasError() da errore
            try {
                //gestione del file
                if(!file.isEmpty()){
                    Path dirPath = Paths.get(uploadDir);
                    if(!Files.exists(dirPath)){
                        Files.createDirectories(dirPath);
                    }

                    String filename = file.getOriginalFilename();
                    Path filePath = dirPath.resolve(filename);
                    Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                    // Imposta il percorso dell'immagine come URL accessibile
                    String fileUrl = filename;
                    existSquadra.setImageUrl(fileUrl);
                }

            } catch (IOException e) {
                existSquadra.setImageUrl("");
            }
        }

        squadraService.save(existSquadra);
        redirectAttributes.addFlashAttribute("successMessage", "Squadra aggiornata con successo.");
        return "redirect:/admin/squadre";
    }


    @PostMapping("/cercaPerNome/{idSquadra}")
    public String cercaPerNome(@PathVariable Long idSquadra,
                               Model model,
                               @RequestParam String nome,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Squadra squadra = squadraService.findById(idSquadra);
        int numero_giocatori = squadra.getGiocatori().size();

        List<GiocatoreTesserato> giocatori = squadraService.findByNome(nome, idSquadra);
        model.addAttribute("giocatori", giocatori);
        model.addAttribute("authentication", userDetails);
        model.addAttribute("squadra", squadra);
        model.addAttribute("numero_giocatori", numero_giocatori);


        return "squadra";
    }
}
