package it.unicam.cs.mpgc.rpg125946.core.model.item;

import java.util.Objects;


public final class Arma implements Oggetto {

    private final String nome;
    private final String descrizione;
    private final int bonusAttacco;

    public Arma(String nome, String descrizione, int bonusAttacco) {
        this.nome = Objects.requireNonNull(nome, "nome");
        this.descrizione = Objects.requireNonNull(descrizione, "descrizione");
        this.bonusAttacco = bonusAttacco;
    }

    public int bonusAttacco() {
        return bonusAttacco;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public String descrizione() {
        return descrizione;
    }

    @Override
    public TipoOggetto tipo() {
        return TipoOggetto.ARMA;
    }
}
