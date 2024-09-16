package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Giocatore;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class GiocatoreValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Giocatore.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Giocatore giocatore = (Giocatore) target;

        // Validazione per il nome
        if (giocatore.getNome() == null || giocatore.getNome().isEmpty()) {
            errors.rejectValue("nome", "nome.empty", "Il nome non può essere vuoto.");
        }

        // Validazione per il cognome
        if (giocatore.getCognome() == null || giocatore.getCognome().isEmpty()) {
            errors.rejectValue("cognome", "cognome.empty", "Il cognome non può essere vuoto.");
        }

        // Validazione per la data di nascita
        if (giocatore.getDataNascita() == null) {
            errors.rejectValue("dataNascita", "dataNascita.empty", "La data di nascita non può essere vuota.");
        } else if (giocatore.getDataNascita().isAfter(LocalDate.now().minusYears(17))) {
            errors.rejectValue("dataNascita", "dataNascita.invalid", "Il giocatore deve avere almeno 17 anni.");
        }

        // Validazione per il luogo di nascita
        if (giocatore.getLuogoNascita() == null || giocatore.getLuogoNascita().isEmpty()) {
            errors.rejectValue("luogoNascita", "luogoNascita.empty", "Il luogo di nascita non può essere vuoto.");
        }

        // Validazione per il ruolo
        if (giocatore.getRuolo() == null || giocatore.getRuolo().isEmpty()) {
            errors.rejectValue("ruolo", "ruolo.empty", "Il ruolo non può essere vuoto.");
        }
    }
}
