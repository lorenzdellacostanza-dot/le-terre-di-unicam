package it.unicam.cs.mpgc.rpg125946.core.combat;

import it.unicam.cs.mpgc.rpg125946.core.model.item.Consumabile;

import java.util.Objects;

public final class AzioneUsaOggetto implements AzioneCombattimento {

    private final Consumabile oggetto;

    public AzioneUsaOggetto(Consumabile oggetto) {
        this.oggetto = Objects.requireNonNull(oggetto, "oggetto");
    }

    @Override
    public String esegui(Combattimento combattimento) {
        if (!combattimento.giocatore().inventario().contiene(oggetto)) {
            return "Oggetto non più disponibile";
        }
        String risultato = oggetto.applicaA(combattimento.giocatore().statistiche());
        combattimento.giocatore().inventario().rimuovi(oggetto);
        return combattimento.giocatore().nome() + " usa " + oggetto.nome() + ". " + risultato;
    }
}
