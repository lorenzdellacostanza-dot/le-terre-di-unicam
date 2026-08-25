package it.unicam.cs.mpgc.rpg125946.core.persistence;

import it.unicam.cs.mpgc.rpg125946.core.engine.StatoGioco;

import java.util.Optional;


public interface ArchivioGioco {

    void salva(StatoGioco stato);
    Optional<StatoGioco> carica();

    boolean esisteSalvataggio();

    void elimina();
}
