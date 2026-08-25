package it.unicam.cs.mpgc.rpg125946.ui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;


public final class GestoreSchermate {

    public static final int LARGHEZZA = 640;
    public static final int ALTEZZA = 620;

    private final Stage stage;
    private final Scene scene;

    public GestoreSchermate(Stage stage) {
        this.stage = Objects.requireNonNull(stage, "stage");
        this.scene = new Scene(new StackPane(), LARGHEZZA, ALTEZZA);
        URL foglioStile = getClass().getResource("/styles.css");
        if (foglioStile != null) {
            scene.getStylesheets().add(foglioStile.toExternalForm());
        }
        stage.setScene(scene);
    }

    /** Mostra la schermata: ne imposta la radice, aggancia i tasti ed esegue l'aggancio di comparsa. */
    public void mostra(Schermata schermata) {
        Objects.requireNonNull(schermata, "schermata");
        scene.setRoot(schermata.radice());
        scene.setOnKeyPressed(schermata.gestoreTasti());
        schermata.allaComparsa();
    }

    public Stage stage() {
        return stage;
    }
}
