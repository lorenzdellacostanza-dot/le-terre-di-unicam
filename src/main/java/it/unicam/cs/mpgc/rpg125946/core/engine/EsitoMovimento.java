package it.unicam.cs.mpgc.rpg125946.core.engine;

import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Png;


public record EsitoMovimento(Tipo tipo, Nemico nemico, Png png) {

    public enum Tipo {
        SPOSTATO,
        BLOCCATO,
        INCONTRO,
        DIALOGO
    }

    public static EsitoMovimento spostato() {
        return new EsitoMovimento(Tipo.SPOSTATO, null, null);
    }

    public static EsitoMovimento bloccato() {
        return new EsitoMovimento(Tipo.BLOCCATO, null, null);
    }

    public static EsitoMovimento incontro(Nemico nemico) {
        return new EsitoMovimento(Tipo.INCONTRO, nemico, null);
    }

    public static EsitoMovimento dialogo(Png png) {
        return new EsitoMovimento(Tipo.DIALOGO, null, png);
    }
}
