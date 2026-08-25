package it.unicam.cs.mpgc.rpg125946.core.engine;

import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.world.Mappa;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public final class StatoGioco {

    private final Giocatore giocatore;
    private Mappa mappaCorrente;
    private final Set<String> idNemiciSconfitti;

    public StatoGioco(Giocatore giocatore, Mappa mappaCorrente, Set<String> idNemiciSconfitti) {
        this.giocatore = Objects.requireNonNull(giocatore, "giocatore");
        this.mappaCorrente = Objects.requireNonNull(mappaCorrente, "mappaCorrente");
        this.idNemiciSconfitti = new HashSet<>(idNemiciSconfitti);
    }

    public Giocatore giocatore() {
        return giocatore;
    }

    public Mappa mappaCorrente() {
        return mappaCorrente;
    }

    public void impostaMappaCorrente(Mappa mappaCorrente) {
        this.mappaCorrente = Objects.requireNonNull(mappaCorrente, "mappaCorrente");
    }

    public Set<String> idNemiciSconfitti() {
        return Set.copyOf(idNemiciSconfitti);
    }

    public void segnaSconfitto(String idNemico) {
        idNemiciSconfitti.add(idNemico);
    }

    public boolean sconfitto(String idNemico) {
        return idNemiciSconfitti.contains(idNemico);
    }
}
