package it.unicam.cs.mpgc.rpg125946.core.persistence;

import it.unicam.cs.mpgc.rpg125946.core.engine.StatoGioco;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Entita;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Oggetto;
import it.unicam.cs.mpgc.rpg125946.core.model.item.TipoOggetto;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Arma;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.ProgressioneStandard;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Progressione;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;
import it.unicam.cs.mpgc.rpg125946.core.persistence.dto.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg125946.core.persistence.dto.DatiOggetto;
import it.unicam.cs.mpgc.rpg125946.core.persistence.dto.DatiGiocatore;
import it.unicam.cs.mpgc.rpg125946.core.persistence.dto.DatiPosizione;
import it.unicam.cs.mpgc.rpg125946.core.persistence.dto.DatiStatistiche;
import it.unicam.cs.mpgc.rpg125946.core.world.Mappa;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Traduce fra lo stato di gioco ricco di comportamento ({@link StatoGioco}) e la sua forma dati
 * ({@link DatiSalvataggio}), in entrambe le direzioni.
 * <p>
 * Isolare la mappatura in una classe dedicata mantiene i DTO "puri" e l'archivio concentrato
 * sull'I/O (Single Responsibility). Per ricostruire il mondo il mapper riusa la {@link FabbricaMappe},
 * che gli viene iniettata: la stessa mappa viene rigenerata dal suo identificatore e vengono poi
 * tolti i nemici già sconfitti, evitando di dover serializzare l'intera griglia.
 */
public final class MapperStatoGioco {

    private final FabbricaMappe fabbricaMappe;
    private final Progressione progressione = new ProgressioneStandard();

    public MapperStatoGioco(FabbricaMappe fabbricaMappe) {
        this.fabbricaMappe = Objects.requireNonNull(fabbricaMappe, "fabbricaMappe");
    }

    // --- Dominio -> DTO ----------------------------------------------------

    public DatiSalvataggio aDati(StatoGioco stato) {
        Giocatore giocatore = stato.giocatore();
        return new DatiSalvataggio(
                stato.mappaCorrente().id(),
                aDatiGiocatore(giocatore),
                new ArrayList<>(stato.idNemiciSconfitti()));
    }

    private DatiGiocatore aDatiGiocatore(Giocatore giocatore) {
        List<DatiOggetto> oggetti = new ArrayList<>();
        for (Oggetto oggetto : giocatore.inventario().oggetti()) {
            oggetti.add(aDatiOggetto(oggetto));
        }
        String equipaggiata = giocatore.armaEquipaggiata().map(Arma::nome).orElse(null);
        return new DatiGiocatore(giocatore.nome(), aDatiPosizione(giocatore.posizione()),
                aDatiStatistiche(giocatore.statistiche()), oggetti, equipaggiata);
    }

    private DatiStatistiche aDatiStatistiche(Statistiche s) {
        return new DatiStatistiche(s.livello(), s.esperienza(), s.oro(),
                s.pvMassimi(), s.pv(), s.pmMassimi(), s.pm(), s.attacco(), s.difesa());
    }

    private DatiPosizione aDatiPosizione(Posizione p) {
        return new DatiPosizione(p.x(), p.y());
    }

    private DatiOggetto aDatiOggetto(Oggetto oggetto) {
        int valore = switch (oggetto.tipo()) {
            case ARMA -> ((Arma) oggetto).bonusAttacco();
            case POZIONE -> ((Pozione) oggetto).quantitaCura();
        };
        return new DatiOggetto(oggetto.tipo().name(), oggetto.nome(), oggetto.descrizione(), valore);
    }

    // --- DTO -> Dominio ----------------------------------------------------

    public StatoGioco daDati(DatiSalvataggio dati) {
        Mappa mappa = fabbricaMappe.crea(dati.idMappa);
        Set<String> sconfitti = Set.copyOf(dati.idNemiciSconfitti);
        rimuoviNemiciSconfitti(mappa, sconfitti);

        Giocatore giocatore = aGiocatore(dati.giocatore);
        return new StatoGioco(giocatore, mappa, sconfitti);
    }

    private void rimuoviNemiciSconfitti(Mappa mappa, Set<String> sconfitti) {
        for (Entita entita : List.copyOf(mappa.entita())) {
            if (sconfitti.contains(entita.id())) {
                mappa.rimuoviEntita(entita);
            }
        }
    }

    private Giocatore aGiocatore(DatiGiocatore dati) {
        Statistiche statistiche = aStatistiche(dati.statistiche);
        Posizione posizione = new Posizione(dati.posizione.x, dati.posizione.y);
        Giocatore giocatore = new Giocatore(dati.nome, posizione, statistiche);

        Arma daEquipaggiare = null;
        for (DatiOggetto datiOggetto : dati.inventario) {
            Oggetto oggetto = aOggetto(datiOggetto);
            giocatore.inventario().aggiungi(oggetto);
            if (oggetto instanceof Arma arma && arma.nome().equals(dati.nomeArmaEquipaggiata)) {
                daEquipaggiare = arma;
            }
        }
        if (daEquipaggiare != null) {
            giocatore.equipaggia(daEquipaggiare);
        }
        return giocatore;
    }

    private Statistiche aStatistiche(DatiStatistiche d) {
        return Statistiche.ripristina(d.livello, d.esperienza, d.oro,
                d.pvMassimi, d.pv, d.pmMassimi, d.pm, d.attacco, d.difesa, progressione);
    }

    private Oggetto aOggetto(DatiOggetto d) {
        TipoOggetto tipo = TipoOggetto.valueOf(d.tipo);
        return switch (tipo) {
            case ARMA -> new Arma(d.nome, d.descrizione, d.valore);
            case POZIONE -> new Pozione(d.nome, d.descrizione, d.valore);
        };
    }
}
