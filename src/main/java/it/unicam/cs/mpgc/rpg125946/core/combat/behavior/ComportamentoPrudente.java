package it.unicam.cs.mpgc.rpg125946.core.combat.behavior;

import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

/**
 * Comportamento prudente: si difende quando i propri PV scendono sotto una soglia,
 * altrimenti attacca. Mostra come un diverso {@link ComportamentoNemico} cambi il gioco
 * senza modificare il nemico né il motore.
 */
public final class ComportamentoPrudente implements ComportamentoNemico {

    private static final double SOGLIA_DIFESA = 0.30;

    @Override
    public MossaNemico scegliMossa(Statistiche proprie, Statistiche avversario) {
        double rapportoPv = proprie.pvMassimi() == 0 ? 1.0 : (double) proprie.pv() / proprie.pvMassimi();
        return rapportoPv < SOGLIA_DIFESA ? MossaNemico.DIFENDI : MossaNemico.ATTACCA;
    }
}
