package it.unicam.cs.mpgc.rpg125946.core.combat.behavior;

import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

/** Comportamento che attacca sempre, senza mai difendersi. */
public final class ComportamentoAggressivo implements ComportamentoNemico {

    @Override
    public MossaNemico scegliMossa(Statistiche proprie, Statistiche avversario) {
        return MossaNemico.ATTACCA;
    }
}
