package it.unicam.cs.mpgc.rpg125946.core.engine;

import it.unicam.cs.mpgc.rpg125946.core.model.entity.Entita;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Png;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Direzione;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Arma;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.ProgressioneStandard;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;
import it.unicam.cs.mpgc.rpg125946.core.world.Mappa;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;


public final class MotoreGioco {

    private final StatoGioco stato;

    public MotoreGioco(StatoGioco stato) {
        this.stato = Objects.requireNonNull(stato, "stato");
    }


    public static MotoreGioco nuovaPartita(String nomeGiocatore, FabbricaMappe fabbricaMappe) {
        Mappa mappa = fabbricaMappe.crea(fabbricaMappe.idMappaIniziale());
        Statistiche statistiche = new Statistiche(50, 50, 10, 5, 20, new ProgressioneStandard());
        Giocatore giocatore = new Giocatore(nomeGiocatore, mappa.puntoPartenza(), statistiche);
        giocatore.equipaggia(new Arma("Spada corta", "Una lama affidabile.", 3));
        giocatore.inventario().aggiungi(new Pozione("ozione", "Ripristina 25 PV.", 25));
        giocatore.inventario().aggiungi(new Pozione("Pozione", "Ripristina 25 PV.", 25));
        return new MotoreGioco(new StatoGioco(giocatore, mappa, new HashSet<>()));
    }

    public StatoGioco stato() {
        return stato;
    }


    public EsitoMovimento muovi(Direzione direzione) {
        Objects.requireNonNull(direzione, "direzione");
        Giocatore giocatore = stato.giocatore();
        Mappa mappa = stato.mappaCorrente();
        Posizione destinazione = giocatore.posizione().spostata(direzione);

        if (!mappa.terrenoCalpestabile(destinazione)) {
            return EsitoMovimento.bloccato();
        }
        Optional<Entita> occupante = mappa.entitaIn(destinazione);
        if (occupante.isPresent()) {
            Entita entita = occupante.get();
            if (entita instanceof Nemico nemico) {
                return EsitoMovimento.incontro(nemico);
            }
            if (entita instanceof Png png) {
                return EsitoMovimento.dialogo(png);
            }
            return EsitoMovimento.bloccato();
        }
        giocatore.impostaPosizione(destinazione);
        return EsitoMovimento.spostato();
    }


    public RicompensaVittoria applicaVittoria(Nemico nemico) {
        Objects.requireNonNull(nemico, "nemico");
        Giocatore giocatore = stato.giocatore();
        int livelloPrima = giocatore.statistiche().livello();

        giocatore.statistiche().guadagnaEsperienza(nemico.ricompensaEsperienza());
        giocatore.statistiche().aggiungiOro(nemico.ricompensaOro());
        Optional<Oggetto> bottino = nemico.bottino();
        bottino.ifPresent(oggetto -> giocatore.inventario().aggiungi(oggetto));

        stato.segnaSconfitto(nemico.id());
        stato.mappaCorrente().rimuoviEntita(nemico);

        int livelloDopo = giocatore.statistiche().livello();
        return new RicompensaVittoria(nemico.ricompensaEsperienza(), nemico.ricompensaOro(), bottino,
                livelloDopo > livelloPrima, livelloDopo);
    }
}
