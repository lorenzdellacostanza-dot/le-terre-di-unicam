package it.unicam.cs.mpgc.rpg125946.ui.screen;

import it.unicam.cs.mpgc.rpg125946.core.engine.MotoreGioco;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Consumabile;
import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;
import it.unicam.cs.mpgc.rpg125946.ui.GestoreSchermate;
import it.unicam.cs.mpgc.rpg125946.ui.Schermata;
import it.unicam.cs.mpgc.rpg125946.ui.controller.ControllerCombattimento;
import it.unicam.cs.mpgc.rpg125946.ui.controller.VistaCombattimento;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public final class SchermataCombattimento implements Schermata, VistaCombattimento {

    private final GestoreSchermate gestore;
    private final Runnable ritornoAlMondo;
    private final ArchivioGioco archivio;
    private final FabbricaMappe fabbricaMappe;
    private final ControllerCombattimento controller;

    private final Giocatore giocatore;
    private final Nemico nemico;

    private final BorderPane radice;
    private final TextArea registro = new TextArea();

    private final Label etichettaPvNemico = new Label();
    private final ProgressBar barraPvNemico = new ProgressBar(1);
    private final Label etichettaPvGiocatore = new Label();
    private final ProgressBar barraPvGiocatore = new ProgressBar(1);
    private final Label etichettaPmGiocatore = new Label();
    private final ProgressBar barraPmGiocatore = new ProgressBar(1);

    private final VBox zonaAzioni = new VBox(8);

    public SchermataCombattimento(GestoreSchermate gestore, MotoreGioco motore, Nemico nemico,
                                  Runnable ritornoAlMondo, ArchivioGioco archivio, FabbricaMappe fabbricaMappe) {
        this.gestore = gestore;
        this.ritornoAlMondo = ritornoAlMondo;
        this.archivio = archivio;
        this.fabbricaMappe = fabbricaMappe;
        this.controller = new ControllerCombattimento(motore, nemico, this);
        this.giocatore = controller.giocatore();
        this.nemico = controller.nemico();

        radice = new BorderPane();
        radice.getStyleClass().add("combat-root");
        radice.setPadding(new Insets(12));
        radice.setTop(creaPannelloNemico());
        radice.setCenter(creaRegistro());
        radice.setBottom(creaZonaInferiore());

        controller.avvia();
    }

    // --- Costruzione dell'interfaccia -------------------------------------

    private VBox creaPannelloNemico() {
        Label nomeNemico = new Label(nemico.nome());
        nomeNemico.getStyleClass().add("enemy-name");
        Label livello = new Label("Livello " + nemico.statistiche().livello());
        livello.getStyleClass().add("caption");

        etichettaPvNemico.getStyleClass().add("caption");
        barraPvNemico.getStyleClass().add("hp-bar");
        barraPvNemico.setPrefWidth(360);
        barraPvNemico.setMaxWidth(Double.MAX_VALUE);

        VBox pannello = new VBox(4, nomeNemico, livello, etichettaPvNemico, barraPvNemico);
        pannello.getStyleClass().add("panel");
        pannello.setPadding(new Insets(12));
        return pannello;
    }

    private TextArea creaRegistro() {
        registro.setEditable(false);
        registro.setWrapText(true);
        registro.setFocusTraversable(false);
        registro.getStyleClass().add("log-area");
        BorderPane.setMargin(registro, new Insets(12, 0, 12, 0));
        return registro;
    }

    private VBox creaZonaInferiore() {
        etichettaPvGiocatore.getStyleClass().add("caption");
        etichettaPmGiocatore.getStyleClass().add("caption");
        barraPvGiocatore.getStyleClass().add("hp-bar");
        barraPmGiocatore.getStyleClass().add("mp-bar");
        barraPvGiocatore.setMaxWidth(Double.MAX_VALUE);
        barraPmGiocatore.setMaxWidth(Double.MAX_VALUE);

        VBox statoGiocatore = new VBox(3,
                new Label(giocatore.nome()),
                etichettaPvGiocatore, barraPvGiocatore,
                etichettaPmGiocatore, barraPmGiocatore);
        statoGiocatore.getStyleClass().add("panel");
        statoGiocatore.setPadding(new Insets(10));

        return new VBox(10, statoGiocatore, zonaAzioni);
    }

    private Button creaBottoneAzione(String testo) {
        Button bottone = new Button(testo);
        bottone.getStyleClass().add("combat-button");
        bottone.setMaxWidth(Double.MAX_VALUE);
        bottone.setFocusTraversable(false);
        return bottone;
    }

    // --- Richieste del controllore (VistaCombattimento) -------------------

    @Override
    public void aggiungiAlRegistro(String testo) {
        registro.appendText(testo + "\n");
    }

    @Override
    public void aggiornaIndicatori() {
        etichettaPvNemico.setText("PV: " + nemico.statistiche().pv() + " / " + nemico.statistiche().pvMassimi());
        barraPvNemico.setProgress(rapporto(nemico.statistiche().pv(), nemico.statistiche().pvMassimi()));

        etichettaPvGiocatore.setText("PV: " + giocatore.statistiche().pv() + " / " + giocatore.statistiche().pvMassimi());
        barraPvGiocatore.setProgress(rapporto(giocatore.statistiche().pv(), giocatore.statistiche().pvMassimi()));
        etichettaPmGiocatore.setText("PM: " + giocatore.statistiche().pm() + " / " + giocatore.statistiche().pmMassimi());
        barraPmGiocatore.setProgress(rapporto(giocatore.statistiche().pm(), giocatore.statistiche().pmMassimi()));
    }

    @Override
    public void mostraAzioniPrincipali() {
        Button attacca = creaBottoneAzione("Attacca");
        attacca.setOnAction(e -> controller.attacca());

        Button difendi = creaBottoneAzione("Difendi");
        difendi.setOnAction(e -> controller.difendi());

        Button oggetto = creaBottoneAzione("Oggetto");
        oggetto.setDisable(controller.consumabili().isEmpty());
        oggetto.setOnAction(e -> mostraSottomenuOggetti());

        Button fuggi = creaBottoneAzione("Fuggi");
        fuggi.setOnAction(e -> controller.fuggi());

        HBox riga = new HBox(8, attacca, difendi, oggetto, fuggi);
        riga.setAlignment(Pos.CENTER);
        for (var nodo : riga.getChildren()) {
            HBox.setHgrow(nodo, Priority.ALWAYS);
        }
        zonaAzioni.getChildren().setAll(riga);
    }

    @Override
    public void mostraProseguimento(String etichetta) {
        Button continua = creaBottoneAzione(etichetta);
        continua.setOnAction(e -> controller.prosegui());
        HBox riga = new HBox(continua);
        riga.setAlignment(Pos.CENTER);
        HBox.setHgrow(continua, Priority.ALWAYS);
        zonaAzioni.getChildren().setAll(riga);
    }

    @Override
    public void tornaAlMondo() {
        ritornoAlMondo.run();
    }

    @Override
    public void mostraFineGioco() {
        gestore.mostra(new SchermataFineGioco(gestore, archivio, fabbricaMappe));
    }

    // --- Sottomenu degli oggetti (presentazione) --------------------------

    private void mostraSottomenuOggetti() {
        VBox elenco = new VBox(6);
        for (Map.Entry<String, List<Consumabile>> gruppo : consumabiliPerNome().entrySet()) {
            List<Consumabile> esemplari = gruppo.getValue();
            Button bottone = creaBottoneAzione(gruppo.getKey() + "  (x" + esemplari.size() + ")");
            Consumabile scelto = esemplari.get(0);
            bottone.setOnAction(e -> controller.usaOggetto(scelto));
            elenco.getChildren().add(bottone);
        }
        Button indietro = creaBottoneAzione("Indietro");
        indietro.setOnAction(e -> mostraAzioniPrincipali());
        elenco.getChildren().add(indietro);
        zonaAzioni.getChildren().setAll(elenco);
    }

    private Map<String, List<Consumabile>> consumabiliPerNome() {
        Map<String, List<Consumabile>> perNome = new LinkedHashMap<>();
        for (Consumabile consumabile : controller.consumabili()) {
            perNome.computeIfAbsent(consumabile.nome(), chiave -> new ArrayList<>()).add(consumabile);
        }
        return perNome;
    }

    private double rapporto(int valore, int massimo) {
        return massimo <= 0 ? 0 : Math.min(1.0, (double) valore / massimo);
    }

    @Override
    public Parent radice() {
        return radice;
    }
}
