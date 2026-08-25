package it.unicam.cs.mpgc.rpg125946.ui;

import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.json.ArchivioGiocoJson;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;
import it.unicam.cs.mpgc.rpg125946.ui.screen.SchermataMenu;
import javafx.application.Application;
import javafx.stage.Stage;


public final class App extends Application {

    @Override
    public void start(Stage stagePrimario) {
        FabbricaMappe fabbricaMappe = new FabbricaMappe();
        ArchivioGioco archivio = new ArchivioGiocoJson(
                ArchivioGiocoJson.fileSalvataggioDefault(), fabbricaMappe);

        GestoreSchermate gestore = new GestoreSchermate(stagePrimario);
        gestore.mostra(new SchermataMenu(gestore, archivio, fabbricaMappe));

        stagePrimario.setTitle("RPG 125946 - Le Terre di Unicam");
        stagePrimario.setResizable(false);
        stagePrimario.show();
    }
}
