package it.unicam.cs.mpgc.rpg125946.core.model.item;

import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

import java.util.Objects;

public final class Pozione implements Consumabile {

    private final String nome;
    private final String descrizione;
    private final int quantitaCura;

    public Pozione(String nome, String descrizione, int quantitaCura) {
        this.nome = Objects.requireNonNull(nome, "nome");
        this.descrizione = Objects.requireNonNull(descrizione, "descrizione");
        this.quantitaCura = quantitaCura;
    }

    public int quantitaCura() {
        return quantitaCura;
    }

    @Override
    public String applicaA(Statistiche bersaglio) {
        int curati = bersaglio.cura(quantitaCura);
        return nome + ": recuperati " + curati + " PV.";
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
        return TipoOggetto.POZIONE;
    }
}
