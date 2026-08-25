package it.unicam.cs.mpgc.rpg125946.core.model.entity;

import it.unicam.cs.mpgc.rpg125946.core.combat.behavior.ComportamentoNemico;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

import java.util.Objects;
import java.util.Optional;


public final class Nemico extends Personaggio {

    private final ComportamentoNemico comportamento;
    private final int ricompensaEsperienza;
    private final int ricompensaOro;
    private final Oggetto bottino;

    public Nemico(String id, String nome, Posizione posizione, Statistiche statistiche,
                  ComportamentoNemico comportamento, int ricompensaEsperienza, int ricompensaOro, Oggetto bottino) {
        super(id, nome, posizione, statistiche);
        this.comportamento = Objects.requireNonNull(comportamento, "comportamento");
        this.ricompensaEsperienza = ricompensaEsperienza;
        this.ricompensaOro = ricompensaOro;
        this.bottino = bottino;
    }

    public ComportamentoNemico comportamento() {
        return comportamento;
    }

    public int ricompensaEsperienza() {
        return ricompensaEsperienza;
    }

    public int ricompensaOro() {
        return ricompensaOro;
    }

    public Optional<Oggetto> bottino() {
        return Optional.ofNullable(bottino);
    }
}
