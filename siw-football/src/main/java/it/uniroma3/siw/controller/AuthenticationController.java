package it.uniroma3.siw.controller;

import it.uniroma3.siw.component.CustomUserDetails;
import it.uniroma3.siw.model.Credentials;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Credentials credentials = userDetails.getCredentials();
            String role = credentials.getRole();

            if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/indexAdmin";
            } else {
                return "redirect:/";
            }
        }

        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getCredentials().getRole().equals("ROLE_ADMIN") || userDetails.getCredentials().getRole().equals("ADMIN")) {
            model.addAttribute("authentication", userDetails);
            return "/admin/profile";
        }
        else if (userDetails.getCredentials().getRole().equals("ROLE_PRESIDENT") || userDetails.getCredentials().getRole().equals("PRESIDENT")) {
            model.addAttribute("authentication", userDetails);
            return "/president/profile";
        }

        return "redirect:/";

    }


}
