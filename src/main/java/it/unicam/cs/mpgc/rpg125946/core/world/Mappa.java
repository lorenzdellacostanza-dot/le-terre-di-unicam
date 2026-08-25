package it.unicam.cs.mpgc.rpg125946.core.world;

import it.unicam.cs.mpgc.rpg125946.core.model.entity.Entita;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public final class Mappa {

    private final String id;
    private final TipoCasella[][] caselle;
    private final int larghezza;
    private final int altezza;
    private final Posizione puntoPartenza;
    private final List<Entita> entita = new ArrayList<>();

    public Mappa(String id, TipoCasella[][] caselle, Posizione puntoPartenza, List<Entita> entita) {
        this.id = Objects.requireNonNull(id, "id");
        this.caselle = Objects.requireNonNull(caselle, "caselle");
        this.altezza = caselle.length;
        this.larghezza = altezza > 0 ? caselle[0].length : 0;
        this.puntoPartenza = Objects.requireNonNull(puntoPartenza, "puntoPartenza");
        this.entita.addAll(entita);
    }

    public String id() {
        return id;
    }

    public int larghezza() {
        return larghezza;
    }

    public int altezza() {
        return altezza;
    }

    public Posizione puntoPartenza() {
        return puntoPartenza;
    }

    public boolean dentro(Posizione p) {
        return p.x() >= 0 && p.x() < larghezza && p.y() >= 0 && p.y() < altezza;
    }

    public TipoCasella casellaIn(Posizione p) {
        if (!dentro(p)) {
            throw new IndexOutOfBoundsException("Posizione fuori mappa: " + p);
        }
        return caselle[p.y()][p.x()];
    }

    /** @return true se il terreno della cella è calpestabile (ignora le entità). */
    public boolean terrenoCalpestabile(Posizione p) {
        return dentro(p) && caselle[p.y()][p.x()].calpestabile();
    }

    // --- Entità ------------------------------------------------------------

    public List<Entita> entita() {
        return Collections.unmodifiableList(entita);
    }

    public Optional<Entita> entitaIn(Posizione p) {
        for (Entita e : entita) {
            if (e.posizione().equals(p)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public void rimuoviEntita(Entita e) {
        entita.remove(e);
    }

    public void aggiungiEntita(Entita e) {
        entita.add(Objects.requireNonNull(e));
    }
}
