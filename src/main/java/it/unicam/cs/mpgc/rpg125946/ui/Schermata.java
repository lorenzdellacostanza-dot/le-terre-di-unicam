package it.unicam.cs.mpgc.rpg125946.ui;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

public interface Schermata {

    Parent radice();

    default void allaComparsa() {
    }

    default EventHandler<KeyEvent> gestoreTasti() {
        return null;
    }
}
