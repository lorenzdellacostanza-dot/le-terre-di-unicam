package it.unicam.cs.mpgc.rpg125946.core.engine;

import it.unicam.cs.mpgc.rpg125946.core.model.item.Oggetto;

import java.util.Optional;


public record RicompensaVittoria(int esperienza, int oro, Optional<Oggetto> bottino,
                                 boolean salitoLivello, int nuovoLivello) {
}
