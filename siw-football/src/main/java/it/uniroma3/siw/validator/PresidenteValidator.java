package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.service.PresidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class PresidenteValidator implements Validator {

    @Autowired
    private PresidenteService presidenteService;

    public PresidenteValidator(PresidenteService presidenteService) {
        this.presidenteService = presidenteService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Presidente.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Presidente presidente = (Presidente) target;

        // Validate Codice Fiscale uniqueness
        if (presidenteService.findById(presidente.getCodiceFiscale()) != null) {
            errors.rejectValue("codiceFiscale", "presidente.codiceFiscale.exists", "Codice fiscale già esistente.");
        }

        // Validate that the birth date is not in the future
        if (presidente.getDataNascita() != null && presidente.getDataNascita().isAfter(LocalDate.now().minusYears(18))) {
            errors.rejectValue("dataNascita", "presidente.dataNascita.future", "Il presidente non può essere minorenne.");
        }
    }
}
