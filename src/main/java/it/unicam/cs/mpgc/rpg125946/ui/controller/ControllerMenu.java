package it.unicam.cs.mpgc.rpg125946.ui.controller;

import it.unicam.cs.mpgc.rpg125946.core.engine.MotoreGioco;
import it.unicam.cs.mpgc.rpg125946.core.engine.StatoGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.EccezionePersistenza;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;

import java.util.Objects;
import java.util.Optional;


public final class ControllerMenu {

    private final ArchivioGioco archivio;
    private final FabbricaMappe fabbricaMappe;
    private final VistaMenu vista;

    public ControllerMenu(ArchivioGioco archivio, FabbricaMappe fabbricaMappe, VistaMenu vista) {
        this.archivio = Objects.requireNonNull(archivio, "archivio");
        this.fabbricaMappe = Objects.requireNonNull(fabbricaMappe, "fabbricaMappe");
        this.vista = Objects.requireNonNull(vista, "vista");
    }

    /** @return {@code true} se esiste un salvataggio riprendibile. */
    public boolean esisteSalvataggio() {
        return archivio.esisteSalvataggio();
    }

    public void nuovaPartita(String nomeEroe) {
        String nome = (nomeEroe == null || nomeEroe.isBlank()) ? "Eroe" : nomeEroe.trim();
        vista.apriMondo(MotoreGioco.nuovaPartita(nome, fabbricaMappe));
    }

    public void continua() {
        try {
            Optional<StatoGioco> stato = archivio.carica();
            if (stato.isPresent()) {
                vista.apriMondo(new MotoreGioco(stato.get()));
            } else {
                vista.avvisa("Nessun salvataggio disponibile.");
            }
        } catch (EccezionePersistenza e) {
            vista.avvisa("Impossibile caricare la partita:\n" + e.getMessage());
        }
    }
}
