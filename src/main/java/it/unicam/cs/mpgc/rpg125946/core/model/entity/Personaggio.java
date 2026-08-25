package it.unicam.cs.mpgc.rpg125946.core.model.entity;

import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Inventario;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

import java.util.Objects;


public abstract class Personaggio implements Entita {

    private final String id;
    private final String nome;
    private final Statistiche statistiche;
    private final Inventario inventario;
    private Posizione posizione;

    protected Personaggio(String id, String nome, Posizione posizione, Statistiche statistiche) {
        this.id = Objects.requireNonNull(id, "id");
        this.nome = Objects.requireNonNull(nome, "nome");
        this.posizione = Objects.requireNonNull(posizione, "posizione");
        this.statistiche = Objects.requireNonNull(statistiche, "statistiche");
        this.inventario = new Inventario();
    }

    public Statistiche statistiche() {
        return statistiche;
    }

    public Inventario inventario() {
        return inventario;
    }


    public int potenzaAttacco() {
        return statistiche.attacco();
    }


    public int potenzaDifesa() {
        return statistiche.difesa();
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
