package it.uniroma3.siw.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class GiocatoreValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
