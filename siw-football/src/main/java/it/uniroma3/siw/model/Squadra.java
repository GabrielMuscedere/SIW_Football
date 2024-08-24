package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private LocalDate annoFondazione;
    private String indirizzoSede;

    @OneToMany
    private List<GiocatoreTesserato> giocatori;

    @OneToOne
    private Presidente presidente;

    public Squadra() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getAnnoFondazione() {
        return annoFondazione;
    }

    public void setAnnoFondazione(LocalDate annoFondazione) {
        this.annoFondazione = annoFondazione;
    }

    public String getIndirizzoSede() {
        return indirizzoSede;
    }

    public void setIndirizzoSede(String indirizzoSede) {
        this.indirizzoSede = indirizzoSede;
    }

    public List<GiocatoreTesserato> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<GiocatoreTesserato> giocatori) {
        this.giocatori = giocatori;
    }

    public Presidente getPresidente() {
        return presidente;
    }

    public void setPresidente(Presidente presidente) {
        this.presidente = presidente;
    }

}
