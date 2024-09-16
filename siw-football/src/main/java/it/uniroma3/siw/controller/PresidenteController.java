package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.validator.CredentialsValidator;
import it.uniroma3.siw.validator.PresidenteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PresidenteController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private PresidenteService presidenteService;

    @Autowired
    private PresidenteValidator presidenteValidator;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("presidente", new Presidente());
        model.addAttribute("credenziali", new Credentials());
        return "register";
    }

    @PostMapping("/register")
    public String registraPresidente(Model model,
                                     @ModelAttribute("presidente") Presidente presidente,
                                     BindingResult presidentResult,
                                     @ModelAttribute("credenziali") Credentials credenziali,
                                     BindingResult credentialsResult,
                                     RedirectAttributes redirectAttributes) {


        presidenteValidator.validate(presidente, presidentResult);
        if (presidentResult.hasErrors()) {
            // Add Presidente validation errors to flash attributes
            presidentResult.getFieldErrors().forEach(error ->
                    redirectAttributes.addFlashAttribute(error.getField() + "Error", error.getDefaultMessage())
            );
            return "redirect:/register";
        }

        credentialsValidator.validate(credenziali, credentialsResult);
        if (credentialsResult.hasErrors()) {
            // Add Presidente validation errors to flash attributes
            credentialsResult.getFieldErrors().forEach(error ->
                    redirectAttributes.addFlashAttribute(error.getField() + "Error", error.getDefaultMessage())
            );
            return "redirect:/register";

        }

        credenziali.setPresidente(presidente);

        this.presidenteService.save(presidente);
        this.credentialsService.save(credenziali);

        return "redirect:/login";
    }
}
