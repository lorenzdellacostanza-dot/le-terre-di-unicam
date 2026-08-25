package it.unicam.cs.mpgc.rpg125946.core.persistence.dto;

import java.util.List;


public final class DatiSalvataggio {

    public String idMappa;
    public DatiGiocatore giocatore;
    public List<String> idNemiciSconfitti;

    public DatiSalvataggio() {
    }

    public DatiSalvataggio(String idMappa, DatiGiocatore giocatore, List<String> idNemiciSconfitti) {
        this.idMappa = idMappa;
        this.giocatore = giocatore;
        this.idNemiciSconfitti = idNemiciSconfitti;
    }
}
