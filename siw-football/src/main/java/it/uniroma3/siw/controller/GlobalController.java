package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

    @GetMapping("/")
    public String getIndex(Model model,
                           @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("authentication", user);
        return "/index";
    }

}
