package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class GiocatoreTesserato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate inizioTesseramento;
    private LocalDate fineTesseramento;

    @ManyToOne(cascade = CascadeType.ALL)
    private Squadra squadra;

    @ManyToOne
    private Giocatore giocatore;

    public GiocatoreTesserato() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getInizioTesseramento() {
        return inizioTesseramento;
    }

    public void setInizioTesseramento(LocalDate inizioTesseramento) {
        this.inizioTesseramento = inizioTesseramento;
    }

    public LocalDate getFineTesseramento() {
        return fineTesseramento;
    }

    public void setFineTesseramento(LocalDate fineTesseramento) {
        this.fineTesseramento = fineTesseramento;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }
}
