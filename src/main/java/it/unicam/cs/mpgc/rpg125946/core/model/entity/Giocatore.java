package it.unicam.cs.mpgc.rpg125946.core.model.entity;

import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Arma;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

import java.util.Optional;


public final class Giocatore extends Personaggio {

    public static final String ID_GIOCATORE = "giocatore";

    private Arma armaEquipaggiata;

    public Giocatore(String nome, Posizione posizione, Statistiche statistiche) {
        super(ID_GIOCATORE, nome, posizione, statistiche);
    }

    public void equipaggia(Arma arma) {
        if (arma != null && !inventario().contiene(arma)) {
            inventario().aggiungi(arma);
        }
        this.armaEquipaggiata = arma;
    }

    public Optional<Arma> armaEquipaggiata() {
        return Optional.ofNullable(armaEquipaggiata);
    }

    @Override
    public int potenzaAttacco() {
        int bonus = armaEquipaggiata != null ? armaEquipaggiata.bonusAttacco() : 0;
        return statistiche().attacco() + bonus;
    }
}
