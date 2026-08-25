package it.unicam.cs.mpgc.rpg125946.ui.controller;

import it.unicam.cs.mpgc.rpg125946.core.engine.EsitoMovimento;
import it.unicam.cs.mpgc.rpg125946.core.engine.MotoreGioco;
import it.unicam.cs.mpgc.rpg125946.core.engine.StatoGioco;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Direzione;
import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.EccezionePersistenza;

import java.util.Objects;


public final class ControllerMondo {

    private final MotoreGioco motore;
    private final ArchivioGioco archivio;
    private final VistaMondo vista;

    public ControllerMondo(MotoreGioco motore, ArchivioGioco archivio, VistaMondo vista) {
        this.motore = Objects.requireNonNull(motore, "motore");
        this.archivio = Objects.requireNonNull(archivio, "archivio");
        this.vista = Objects.requireNonNull(vista, "vista");
    }

    /** Lo stato di gioco, che la vista legge per disegnarsi (in MVC la vista legge il modello). */
    public StatoGioco stato() {
        return motore.stato();
    }

    public MotoreGioco motore() {
        return motore;
    }

    /** L'utente chiede di muoversi: il modello decide, la vista ne rappresenta l'esito. */
    public void muovi(Direzione direzione) {
        EsitoMovimento esito = motore.muovi(direzione);
        switch (esito.tipo()) {
            case SPOSTATO, BLOCCATO -> vista.ridisegnaMondo();
            case INCONTRO -> vista.apriCombattimento(esito.nemico());
            case DIALOGO -> vista.mostraDialogo(esito.png());
        }
    }

    public void salva() {
        try {
            archivio.salva(motore.stato());
            vista.mostraMessaggioTemporaneo("Partita salvata.");
        } catch (EccezionePersistenza e) {
            vista.mostraErrore("Salvataggio non riuscito:\n" + e.getMessage());
        }
    }

    public void richiediRitornoAlMenu() {
        if (vista.chiediConferma("Tornare al menu principale? I progressi non salvati andranno persi.")) {
            vista.tornaAlMenu();
        }
    }
}
