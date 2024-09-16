package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Credentials save(Credentials credentials) {
        credentials.setRole("ROLE_PRESIDENT");
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        return credentialsRepository.save(credentials);
    }

    public boolean existsByUsername(String username) {
        return credentialsRepository.existsByUsername(username);
    }

    public Credentials findByUsername(String username) {
        return credentialsRepository.findByUsername(username);
    }
}
