package it.unicam.cs.mpgc.rpg125946.ui.controller;

import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Png;

/**
 * Ciò che il {@link ControllerMondo} si aspetta dalla propria vista (ruolo <em>View</em> di MVC).
 * Sono tutte richieste di <em>presentazione</em>: nessuna di esse contiene regole di gioco.
 */
public interface VistaMondo {

    /** Ridisegna la porzione di mondo visibile. */
    void ridisegnaMondo();

    /** Mostra un messaggio che scompare da solo dopo poco. */
    void mostraMessaggioTemporaneo(String testo);

    /** Mostra un errore bloccante. */
    void mostraErrore(String messaggio);

    /** Apre il riquadro di dialogo con il personaggio non giocante. */
    void mostraDialogo(Png png);

    /** Passa alla schermata di combattimento contro il nemico incontrato. */
    void apriCombattimento(Nemico nemico);

    /** Chiede conferma all'utente. @return {@code true} se ha confermato. */
    boolean chiediConferma(String domanda);

    /** Torna al menu principale. */
    void tornaAlMenu();
}
