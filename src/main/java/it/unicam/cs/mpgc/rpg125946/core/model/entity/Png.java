package it.unicam.cs.mpgc.rpg125946.core.model.entity;

import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;

import java.util.List;
import java.util.Objects;

public final class Png implements Entita {

    private final String id;
    private final String nome;
    private final List<String> battute;
    private Posizione posizione;

    public Png(String id, String nome, Posizione posizione, List<String> battute) {
        this.id = Objects.requireNonNull(id, "id");
        this.nome = Objects.requireNonNull(nome, "nome");
        this.posizione = Objects.requireNonNull(posizione, "posizione");
        this.battute = List.copyOf(battute);
    }

    /** @return le battute di dialogo, in ordine. */
    public List<String> battute() {
        return battute;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public Posizione posizione() {
        return posizione;
    }

    @Override
    public void impostaPosizione(Posizione posizione) {
        this.posizione = Objects.requireNonNull(posizione, "posizione");
    }
}
