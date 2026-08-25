package it.unicam.cs.mpgc.rpg125946.ui.screen;

import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;
import it.unicam.cs.mpgc.rpg125946.ui.GestoreSchermate;
import it.unicam.cs.mpgc.rpg125946.ui.Schermata;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class SchermataFineGioco implements Schermata {

    private final VBox radice;

    public SchermataFineGioco(GestoreSchermate gestore, ArchivioGioco archivio, FabbricaMappe fabbricaMappe) {
        Label titolo = new Label("Sconfitta");
        titolo.getStyleClass().add("game-over-title");
        Label sottotitolo = new Label("Le Terre di Unicam ti aspettano ancora...");
        sottotitolo.getStyleClass().add("subtitle");

        Button bottoneMenu = new Button("Torna al menu");
        bottoneMenu.getStyleClass().add("menu-button");
        bottoneMenu.setPrefWidth(220);
        bottoneMenu.setOnAction(e -> gestore.mostra(new SchermataMenu(gestore, archivio, fabbricaMappe)));

        Button bottoneEsci = new Button("Esci");
        bottoneEsci.getStyleClass().add("menu-button");
        bottoneEsci.setPrefWidth(220);
        bottoneEsci.setOnAction(e -> Platform.exit());

        radice = new VBox(16, titolo, sottotitolo, bottoneMenu, bottoneEsci);
        radice.setAlignment(Pos.CENTER);
        radice.setPadding(new Insets(24));
        radice.getStyleClass().add("screen-root");
    }

    @Override
    public Parent radice() {
        return radice;
    }
}
