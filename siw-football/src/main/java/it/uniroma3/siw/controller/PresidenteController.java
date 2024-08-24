package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PresidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PresidenteController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private PresidenteService presidenteService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("presidente", new Presidente());
        model.addAttribute("credenziali", new Credentials());
        return "register";
    }

    @PostMapping("/register")
    public String registraPresidente(Model model,
                                     @ModelAttribute("presidente") Presidente presidente,
                                     @ModelAttribute("credenziali") Credentials credenziali) {

        credenziali.setPresidente(presidente);

        this.presidenteService.save(presidente);
        this.credentialsService.save(credenziali);

        return "redirect:/login";
    }
}
