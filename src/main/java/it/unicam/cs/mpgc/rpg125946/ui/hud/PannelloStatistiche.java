package it.unicam.cs.mpgc.rpg125946.ui.hud;

import it.unicam.cs.mpgc.rpg125946.core.model.stats.OsservatoreStatistiche;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;


public final class PannelloStatistiche extends VBox implements OsservatoreStatistiche {

    private final Statistiche statistiche;

    private final Label etichettaLivello = new Label();
    private final Label etichettaPv = new Label();
    private final Label etichettaPm = new Label();
    private final Label etichettaPe = new Label();
    private final Label etichettaOro = new Label();
    private final ProgressBar barraPv = new ProgressBar(0);
    private final ProgressBar barraPm = new ProgressBar(0);
    private final ProgressBar barraPe = new ProgressBar(0);

    public PannelloStatistiche(Statistiche statistiche) {
        this.statistiche = statistiche;

        getStyleClass().add("panel");
        setSpacing(3);
        setPadding(new Insets(10));
        setPrefWidth(210);
        setMaxWidth(210);
        setMaxHeight(VBox.USE_PREF_SIZE);

        Label titolo = new Label("Eroe");
        titolo.getStyleClass().add("panel-title");

        etichettaLivello.getStyleClass().add("caption");
        etichettaPv.getStyleClass().add("caption");
        etichettaPm.getStyleClass().add("caption");
        etichettaPe.getStyleClass().add("caption");
        etichettaOro.getStyleClass().add("caption");

        configuraBarra(barraPv, "hp-bar");
        configuraBarra(barraPm, "mp-bar");
        configuraBarra(barraPe, "xp-bar");

        getChildren().addAll(titolo, etichettaLivello,
                etichettaPv, barraPv, etichettaPm, barraPm, etichettaPe, barraPe, etichettaOro);

        statistiche.aggiungiOsservatore(this);
        aggiorna();
    }

    private void configuraBarra(ProgressBar barra, String classe) {
        barra.getStyleClass().add(classe);
        barra.setPrefWidth(188);
        barra.setMaxWidth(188);
    }

    @Override
    public void statisticheCambiate(Statistiche s) {
        if (Platform.isFxApplicationThread()) {
            aggiorna();
        } else {
            Platform.runLater(this::aggiorna);
        }
    }

    private void aggiorna() {
        etichettaLivello.setText("Livello " + statistiche.livello());
        etichettaPv.setText("PV: " + statistiche.pv() + " / " + statistiche.pvMassimi());
        etichettaPm.setText("PM: " + statistiche.pm() + " / " + statistiche.pmMassimi());
        etichettaPe.setText("PE: " + statistiche.esperienza() + " / " + statistiche.esperienzaProssimoLivello());
        etichettaOro.setText("Oro: " + statistiche.oro());
        barraPv.setProgress(rapporto(statistiche.pv(), statistiche.pvMassimi()));
        barraPm.setProgress(rapporto(statistiche.pm(), statistiche.pmMassimi()));
        barraPe.setProgress(rapporto(statistiche.esperienza(), statistiche.esperienzaProssimoLivello()));
    }

    private double rapporto(int valore, int massimo) {
        return massimo <= 0 ? 0 : Math.min(1.0, (double) valore / massimo);
    }
}
