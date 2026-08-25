package it.unicam.cs.mpgc.rpg125946.core.combat.behavior;

import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

/**
 * Strategia decisionale di un nemico in combattimento (pattern <em>Strategy</em>).
 * <p>
 * La decisione dipende soltanto dalle statistiche in gioco e non dal motore di combattimento:
 * questo evita una dipendenza circolare fra i package e rende ogni comportamento verificabile
 * in isolamento con un semplice unit test.
 */
public interface ComportamentoNemico {

    /**
     * @param proprie    statistiche del nemico che deve agire
     * @param avversario statistiche dell'avversario (il giocatore)
     * @return la mossa scelta
     */
    MossaNemico scegliMossa(Statistiche proprie, Statistiche avversario);
}
