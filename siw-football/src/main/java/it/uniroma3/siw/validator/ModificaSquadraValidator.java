package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Squadra;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class ModificaSquadraValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Squadra squadra = (Squadra) target;

        // Validazione per il nome della squadra
        if (squadra.getNome() == null || squadra.getNome().isEmpty()) {
            errors.rejectValue("nome", "nome.empty", "Il nome della squadra non può essere vuoto.");
        }

        if (squadra.getAnnoFondazione() != null && squadra.getAnnoFondazione().isAfter(LocalDate.now())) {
            errors.rejectValue("annoFondazione", "annoFondazione.invalid", "L'anno di fondazione non può essere nel futuro.");
        }

        // Validazione per l'indirizzo della sede
        if (squadra.getIndirizzoSede() == null || squadra.getIndirizzoSede().isEmpty()) {
            errors.rejectValue("indirizzoSede", "indirizzoSede.empty", "L'indirizzo della sede non può essere vuoto.");
        }


    }
}
