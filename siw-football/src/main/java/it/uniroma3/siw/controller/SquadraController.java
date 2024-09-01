package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.PresidenteRepository;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
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
                              @RequestParam("file") MultipartFile file){

        this.squadraValidator.validate(squadra, bindingResult);

        if (bindingResult.hasErrors()) {

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
                e.printStackTrace();
                bindingResult.rejectValue("file", "upload.failed", "Failed to upload file: " + e.getMessage());
            }
        } else {
            bindingResult.rejectValue("nome", "pizza.duplicate");
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

        return "president/squadra";
    }

    @GetMapping("/squadra/{idSquadra}")
    public String squadra(Model model,
                          @PathVariable Long idSquadra,
                          @AuthenticationPrincipal CustomUserDetails userDetails){

        model.addAttribute("authentication", userDetails);
        model.addAttribute("squadra", squadraService.findById(idSquadra));

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
                                @RequestParam MultipartFile file,
                                RedirectAttributes redirectAttributes,
                                BindingResult bindingResult){

        Squadra existSquadra = squadraService.findById(idSquadra);

        if(existSquadra.getPresidente() != null){
            Presidente presidenteVecchio = existSquadra.getPresidente();
            presidenteVecchio.setSquadra(null);
            presidenteService.save(presidenteVecchio);
        }

        Presidente presidenteNuovo = presidenteService.findById(updatedSquadra.getPresidente().getCodiceFiscale());
        existSquadra.setPresidente(presidenteNuovo);

        System.out.println("SONO APPENA FUORI L'IF E IL PRESIDENTE CAMBIATO VALE:" + updatedSquadra.getPresidente().getNome());

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

                this.squadraService.save(existSquadra);

                presidenteNuovo.setSquadra(existSquadra);

                System.out.println("DATI DELL'EXIST SQUADRA");
                System.out.println(existSquadra.getNome());
                System.out.println(existSquadra.getPresidente().getNome());
                System.out.println(existSquadra.getIndirizzoSede());

                redirectAttributes.addFlashAttribute("successMessage", "Squadra e immagine aggiornati con successo.");
                return "redirect:/admin/squadre";

            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("file", "upload.failed", "Failed to upload file: " + e.getMessage());
            }
        }

        this.squadraService.save(existSquadra);
        presidenteNuovo.setSquadra(existSquadra);
        redirectAttributes.addFlashAttribute("successMessage", "Squadra aggiornata con successo.");

        return "redirect:admin/squadre";
    }

}
