package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Squadra;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SquadraValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Squadra.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Squadra squadra = (Squadra) target;

        // Validazione del nome
        if (squadra.getNome() == null || squadra.getNome().trim().isEmpty()) {
            errors.rejectValue("nome", "required", "Il nome è obbligatorio");
        }

        // Validazione dell'anno di fondazione
        if (squadra.getAnnoFondazione() == null) {
            errors.rejectValue("annoFondazione", "required", "L'anno di fondazione è obbligatorio");
        } else if (squadra.getAnnoFondazione().isAfter(java.time.LocalDate.now())) {
            errors.rejectValue("annoFondazione", "invalid", "L'anno di fondazione non può essere nel futuro");
        }

        // Validazione dell'indirizzo della sede
        if (squadra.getIndirizzoSede() == null || squadra.getIndirizzoSede().trim().isEmpty()) {
            errors.rejectValue("indirizzoSede", "required", "L'indirizzo della sede è obbligatorio");
        }

        // Validazione del presidente
        if (squadra.getPresidente() == null) {
            errors.rejectValue("presidente", "required", "Il presidente è obbligatorio");
        }
    }
}
