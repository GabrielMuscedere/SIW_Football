package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

    @Autowired
    private SquadraService squadraService;

    @GetMapping("/")
    public String getIndex(Model model,
                           @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("authentication", user);
        model.addAttribute("squadre", squadraService.findAll());
        return "/index";
    }

}
