package it.unicam.cs.mpgc.rpg125946.core.persistence.dto;


public final class DatiOggetto {

    public String tipo;
    public String nome;
    public String descrizione;
    public int valore;

    public DatiOggetto() {
    }

    public DatiOggetto(String tipo, String nome, String descrizione, int valore) {
        this.tipo = tipo;
        this.nome = nome;
        this.descrizione = descrizione;
        this.valore = valore;
    }
}
