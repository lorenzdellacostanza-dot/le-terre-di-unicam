package it.unicam.cs.mpgc.rpg125946.ui.screen;

import it.unicam.cs.mpgc.rpg125946.core.engine.MotoreGioco;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Png;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Direzione;
import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;
import it.unicam.cs.mpgc.rpg125946.ui.GestoreSchermate;
import it.unicam.cs.mpgc.rpg125946.ui.Schermata;
import it.unicam.cs.mpgc.rpg125946.ui.controller.ControllerMondo;
import it.unicam.cs.mpgc.rpg125946.ui.controller.VistaMondo;
import it.unicam.cs.mpgc.rpg125946.ui.hud.PannelloStatistiche;
import it.unicam.cs.mpgc.rpg125946.ui.render.DisegnatoreMondo;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;


public final class SchermataMondo implements Schermata, VistaMondo {

    private static final int TILE = 32;
    private static final int COLONNE = 20;
    private static final int RIGHE = 15;

    private final GestoreSchermate gestore;
    private final ArchivioGioco archivio;
    private final FabbricaMappe fabbricaMappe;
    private final ControllerMondo controller;
    private final DisegnatoreMondo disegnatore = new DisegnatoreMondo();

    private final BorderPane radice;
    private final Canvas tela;
    private final Label messaggio = new Label();

    // Stato puramente visivo del riquadro di dialogo
    private final VBox riquadroDialogo;
    private final Label nomeInterlocutore = new Label();
    private final Label testoDialogo = new Label();
    private final Button bottoneDialogo = new Button();
    private List<String> battuteCorrenti = List.of();
    private int indiceBattuta;
    private boolean dialogoAttivo;

    public SchermataMondo(GestoreSchermate gestore, MotoreGioco motore,
                          ArchivioGioco archivio, FabbricaMappe fabbricaMappe) {
        this.gestore = gestore;
        this.archivio = archivio;
        this.fabbricaMappe = fabbricaMappe;
        this.controller = new ControllerMondo(motore, archivio, this);

        this.tela = new Canvas((double) COLONNE * TILE, (double) RIGHE * TILE);
        this.tela.setFocusTraversable(true);

        PannelloStatistiche pannello =
                new PannelloStatistiche(controller.stato().giocatore().statistiche());
        StackPane.setAlignment(pannello, Pos.TOP_LEFT);
        StackPane.setMargin(pannello, new Insets(8));

        messaggio.getStyleClass().add("message-label");
        messaggio.setVisible(false);
        StackPane.setAlignment(messaggio, Pos.BOTTOM_CENTER);
        StackPane.setMargin(messaggio, new Insets(0, 0, 14, 0));

        riquadroDialogo = creaRiquadroDialogo();
        riquadroDialogo.setVisible(false);
        StackPane.setAlignment(riquadroDialogo, Pos.BOTTOM_CENTER);
        StackPane.setMargin(riquadroDialogo, new Insets(0, 0, 12, 0));

        StackPane centro = new StackPane(tela, pannello, messaggio, riquadroDialogo);
        centro.getStyleClass().add("screen-root");

        radice = new BorderPane();
        radice.setCenter(centro);
        radice.setBottom(creaBarraComandi());

        ridisegnaMondo();
    }

    // --- Costruzione dell'interfaccia -------------------------------------

    private VBox creaRiquadroDialogo() {
        nomeInterlocutore.getStyleClass().add("dialogue-speaker");
        testoDialogo.getStyleClass().add("dialogue-text");
        testoDialogo.setWrapText(true);
        testoDialogo.setMaxWidth(520);

        bottoneDialogo.getStyleClass().add("control-button");
        bottoneDialogo.setFocusTraversable(false);
        bottoneDialogo.setOnAction(e -> avanzaDialogo());

        HBox riga = new HBox(bottoneDialogo);
        riga.setAlignment(Pos.CENTER_RIGHT);

        VBox riquadro = new VBox(8, nomeInterlocutore, testoDialogo, riga);
        riquadro.getStyleClass().add("dialogue-box");
        riquadro.setMaxWidth(560);
        riquadro.setMaxHeight(VBox.USE_PREF_SIZE);
        return riquadro;
    }

    private HBox creaBarraComandi() {
        Button bottoneSalva = new Button("Salva");
        bottoneSalva.getStyleClass().add("control-button");
        bottoneSalva.setFocusTraversable(false);
        bottoneSalva.setOnAction(e -> controller.salva());

        Button bottoneMenu = new Button("Menu principale");
        bottoneMenu.getStyleClass().add("control-button");
        bottoneMenu.setFocusTraversable(false);
        bottoneMenu.setOnAction(e -> controller.richiediRitornoAlMenu());

        HBox barra = new HBox(10, bottoneSalva, bottoneMenu);
        barra.setAlignment(Pos.CENTER);
        barra.setPadding(new Insets(10));
        barra.getStyleClass().add("control-bar");
        return barra;
    }

    // --- Input: dai tasti alle intenzioni ---------------------------------

    @Override
    public EventHandler<KeyEvent> gestoreTasti() {
        return this::gestisciTasto;
    }

    private void gestisciTasto(KeyEvent evento) {
        if (dialogoAttivo) {
            if (evento.getCode() == KeyCode.SPACE || evento.getCode() == KeyCode.ENTER) {
                avanzaDialogo();
            } else if (evento.getCode() == KeyCode.ESCAPE) {
                chiudiDialogo();
            }
            evento.consume();
            return;
        }
        Direzione direzione = direzionePer(evento.getCode());
        if (direzione != null) {
            controller.muovi(direzione);
            evento.consume();
        }
    }

    private Direzione direzionePer(KeyCode codice) {
        return switch (codice) {
            case W, UP -> Direzione.SU;
            case S, DOWN -> Direzione.GIU;
            case A, LEFT -> Direzione.SINISTRA;
            case D, RIGHT -> Direzione.DESTRA;
            default -> null;
        };
    }

    // --- Richieste del controllore (VistaMondo) ---------------------------

    @Override
    public void ridisegnaMondo() {
        disegnatore.disegna(tela.getGraphicsContext2D(), controller.stato().mappaCorrente(),
                controller.stato().giocatore(), COLONNE, RIGHE, TILE);
    }

    @Override
    public void mostraMessaggioTemporaneo(String testo) {
        messaggio.setText(testo);
        messaggio.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.6));
        pausa.setOnFinished(e -> messaggio.setVisible(false));
        pausa.play();
    }

    @Override
    public void mostraErrore(String testo) {
        Alert alert = new Alert(Alert.AlertType.ERROR, testo, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void mostraDialogo(Png png) {
        nomeInterlocutore.setText(png.nome());
        battuteCorrenti = png.battute();
        indiceBattuta = 0;
        dialogoAttivo = true;
        mostraBattutaCorrente();
        riquadroDialogo.setVisible(true);
    }

    @Override
    public void apriCombattimento(Nemico nemico) {
        gestore.mostra(new SchermataCombattimento(gestore, controller.motore(), nemico,
                this::ritornaAlMondo, archivio, fabbricaMappe));
    }

    @Override
    public boolean chiediConferma(String domanda) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, domanda, ButtonType.OK, ButtonType.CANCEL);
        conferma.setHeaderText(null);
        conferma.setTitle("Conferma");
        Optional<ButtonType> scelta = conferma.showAndWait();
        return scelta.isPresent() && scelta.get() == ButtonType.OK;
    }

    @Override
    public void tornaAlMenu() {
        gestore.mostra(new SchermataMenu(gestore, archivio, fabbricaMappe));
    }

    // --- Dialogo (stato di sola presentazione) ----------------------------

    private void mostraBattutaCorrente() {
        testoDialogo.setText(battuteCorrenti.get(indiceBattuta));
        boolean ultima = indiceBattuta == battuteCorrenti.size() - 1;
        bottoneDialogo.setText(ultima ? "Chiudi" : "Avanti");
    }

    private void avanzaDialogo() {
        if (indiceBattuta < battuteCorrenti.size() - 1) {
            indiceBattuta++;
            mostraBattutaCorrente();
        } else {
            chiudiDialogo();
        }
    }

    private void chiudiDialogo() {
        dialogoAttivo = false;
        riquadroDialogo.setVisible(false);
        tela.requestFocus();
    }

    /** Richiamata dalla schermata di combattimento al termine del duello. */
    private void ritornaAlMondo() {
        gestore.mostra(this);
        ridisegnaMondo();
    }

    // --- Ciclo di vita -----------------------------------------------------

    @Override
    public void allaComparsa() {
        tela.requestFocus();
    }

    @Override
    public Parent radice() {
        return radice;
    }
}
