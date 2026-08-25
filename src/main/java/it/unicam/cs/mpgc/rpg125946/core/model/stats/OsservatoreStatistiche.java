package it.unicam.cs.mpgc.rpg125946.core.model.stats;


public interface OsservatoreStatistiche {


    void statisticheCambiate(Statistiche statistiche);

    default void salitaLivello(Statistiche statistiche, int nuovoLivello) {
        statisticheCambiate(statistiche);
    }
}
