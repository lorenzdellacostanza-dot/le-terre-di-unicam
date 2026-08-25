package it.unicam.cs.mpgc.rpg125946.core.model.item;

import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;


public interface Consumabile extends Oggetto {

    /**
     * Applica l'effetto al bersaglio.
     *
     * @return breve descrizione dell'esito, utile per il registro di combattimento/interfaccia
     */
    String applicaA(Statistiche bersaglio);
}
