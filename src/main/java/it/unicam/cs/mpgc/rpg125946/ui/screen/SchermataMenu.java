package it.unicam.cs.mpgc.rpg125946.ui.screen;

import it.unicam.cs.mpgc.rpg125946.core.engine.MotoreGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;
import it.unicam.cs.mpgc.rpg125946.ui.GestoreSchermate;
import it.unicam.cs.mpgc.rpg125946.ui.Schermata;
import it.unicam.cs.mpgc.rpg125946.ui.controller.ControllerMenu;
import it.unicam.cs.mpgc.rpg125946.ui.controller.VistaMenu;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public final class SchermataMenu implements Schermata, VistaMenu {

    private final GestoreSchermate gestore;
    private final ArchivioGioco archivio;
    private final FabbricaMappe fabbricaMappe;
    private final ControllerMenu controller;

    private final VBox radice;
    private final TextField campoNome;

    public SchermataMenu(GestoreSchermate gestore, ArchivioGioco archivio, FabbricaMappe fabbricaMappe) {
        this.gestore = gestore;
        this.archivio = archivio;
        this.fabbricaMappe = fabbricaMappe;
        this.controller = new ControllerMenu(archivio, fabbricaMappe, this);

        Label titolo = new Label("Le Terre di Unicam");
        titolo.getStyleClass().add("title");
        Label sottotitolo = new Label("Un piccolo gioco di ruolo a turni");
        sottotitolo.getStyleClass().add("subtitle");

        Label etichettaNome = new Label("Nome dell'eroe:");
        etichettaNome.getStyleClass().add("caption");
        campoNome = new TextField("Eroe");
        campoNome.setMaxWidth(220);

        Button bottoneNuova = creaBottone("Nuova partita");
        bottoneNuova.setOnAction(e -> controller.nuovaPartita(campoNome.getText()));

        Button bottoneContinua = creaBottone("Continua");
        bottoneContinua.setDisable(!controller.esisteSalvataggio());
        bottoneContinua.setOnAction(e -> controller.continua());

        Button bottoneEsci = creaBottone("Esci");
        bottoneEsci.setOnAction(e -> Platform.exit());

        Label suggerimento = new Label("Muoviti con W A S D o le frecce.");
        suggerimento.getStyleClass().add("hint");

        radice = new VBox(14, titolo, sottotitolo, etichettaNome, campoNome,
                bottoneNuova, bottoneContinua, bottoneEsci, suggerimento);
        radice.setAlignment(Pos.CENTER);
        radice.setPadding(new Insets(24));
        radice.getStyleClass().add("screen-root");
    }

    private Button creaBottone(String testo) {
        Button bottone = new Button(testo);
        bottone.getStyleClass().add("menu-button");
        bottone.setPrefWidth(220);
        return bottone;
    }

    // --- Richieste del controllore (VistaMenu) ----------------------------

    @Override
    public void apriMondo(MotoreGioco motore) {
        gestore.mostra(new SchermataMondo(gestore, motore, archivio, fabbricaMappe));
    }

    @Override
    public void avvisa(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING, messaggio, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Attenzione");
        alert.showAndWait();
    }

    @Override
    public Parent radice() {
        return radice;
    }
}
