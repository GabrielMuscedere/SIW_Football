package it.uniroma3.siw.component;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Presidente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Credentials credentials;
    private final Presidente presidente;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Credentials credentials, Presidente presidente, Collection<? extends GrantedAuthority> authorities) {
        this.credentials = credentials;
        this.presidente = presidente;
        this.authorities = authorities;
    }

    public Presidente getPresidente() {
        return presidente;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return credentials.getPassword();
    }

    @Override
    public String getUsername() {
        return credentials.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}