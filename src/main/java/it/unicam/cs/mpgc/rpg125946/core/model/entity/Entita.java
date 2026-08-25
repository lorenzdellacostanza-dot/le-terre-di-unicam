package it.unicam.cs.mpgc.rpg125946.core.model.entity;

import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;


public interface Entita {


    String id();

    String nome();

    Posizione posizione();

    void impostaPosizione(Posizione posizione);
}
