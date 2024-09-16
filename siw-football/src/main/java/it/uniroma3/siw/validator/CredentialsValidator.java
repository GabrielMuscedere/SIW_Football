package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CredentialsValidator implements Validator {

    @Autowired
    private CredentialsService credentialsService;

    public CredentialsValidator(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credenziali = (Credentials) target;

        // Validate Username uniqueness
        if (credentialsService.findByUsername(credenziali.getUsername()) != null) {
            errors.rejectValue("username", "credentials.username.exists", "Username già esistente.");
        }

        // Validate password length (8-20 characters)
        String password = credenziali.getPassword();
        if (password.length() < 8 || password.length() > 20) {
            errors.rejectValue("password", "credentials.password.length", "La password deve avere almeno 8 caratteri e massimo 20.");
        }
    }
}
