package it.unicam.cs.mpgc.rpg125946.core.world;

import it.unicam.cs.mpgc.rpg125946.core.combat.behavior.ComportamentoAggressivo;
import it.unicam.cs.mpgc.rpg125946.core.combat.behavior.ComportamentoPrudente;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Entita;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Png;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Pozione;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.ProgressioneStandard;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Progressione;
import it.unicam.cs.mpgc.rpg125946.core.model.stats.Statistiche;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Costruisce le mappe di gioco a partire da una "pianta" testuale, in cui ogni carattere
 * rappresenta un terreno o un punto di generazione di un'entità.
 * <p>
 * Questo approccio dichiarativo rende banale creare o modificare livelli (basta cambiare le
 * stringhe) e tiene i dati del mondo separati dal codice. Le mappe disponibili sono raccolte in
 * un registro {@code id -> costruttore}: aggiungere un nuovo livello significa registrarne uno,
 * senza modificare i metodi esistenti (Open/Closed).
 */
public final class FabbricaMappe {

    /** Identificatore della mappa iniziale. */
    public static final String VILLAGGIO = "villaggio";

    private static final Progressione PROGRESSIONE_NEMICI = new ProgressioneStandard();

    private final Map<String, Supplier<Mappa>> registro = Map.of(
            VILLAGGIO, FabbricaMappe::costruisciVillaggio
    );

    /** @return la mappa associata all'identificatore indicato. */
    public Mappa crea(String idMappa) {
        Supplier<Mappa> fornitore = registro.get(idMappa);
        if (fornitore == null) {
            throw new NoSuchElementException("Mappa sconosciuta: " + idMappa);
        }
        return fornitore.get();
    }

    /** @return l'identificatore della mappa da cui inizia una nuova partita. */
    public String idMappaIniziale() {
        return VILLAGGIO;
    }

    // --- Definizione dei livelli ------------------------------------------

    /*
     * Legenda:
     *   T albero   H casa   # staccionata   W acqua   B ponte
     *   G erba     . sentiero   f fiori
     *   P punto di partenza del giocatore
     *   S slime (aggressivo)   K goblin (prudente)   N personaggio non giocante
     */
    private static Mappa costruisciVillaggio() {
        String[] pianta = {
                "TTTTTTTTTTTTTTTTTTTTTTTT",
                "THHHGGGGG......GGGGGGG#T",
                "THHHGGGGG..GG..GGGGGGG#T",
                "TGGGGGGGG..GG..GGGSGGGGT",
                "TGGfGGGGG..GG..GGGGGGGGT",
                "TGGGGG.....G...GGGGHHHGT",
                "TGGGGG.PG..GG..GGGGHHHGT",
                "TGG....GG..GG.....GHHHGT",
                "TGGGGGGGG..NG..GGGGGGGGT",
                "TWWWWBGGG..GG..GGGGGGfGT",
                "TWWWWBGGG..GG.....GGGGGT",
                "TWWWWBGGG..GGGGGGGGGGGGT",
                "TGGGGGGGG..GGGGGGGKGGGGT",
                "TGfGGGGGG..GGGGGGGGGGG#T",
                "T#GGGGGGGGGGGGGGGGGGGG#T",
                "TTTTTTTTTTTTTTTTTTTTTTTT"
        };
        return analizza(VILLAGGIO, pianta);
    }

    // --- Analisi della pianta ---------------------------------------------

    private static Mappa analizza(String id, String[] pianta) {
        int altezza = pianta.length;
        int larghezza = pianta[0].length();
        TipoCasella[][] caselle = new TipoCasella[altezza][larghezza];
        List<Entita> entita = new ArrayList<>();
        Posizione partenza = null;
        int contatoreNemici = 0;

        for (int y = 0; y < altezza; y++) {
            String riga = pianta[y];
            if (riga.length() != larghezza) {
                throw new IllegalArgumentException(
                        "Riga " + y + " di lunghezza " + riga.length() + ", attesa " + larghezza);
            }
            for (int x = 0; x < larghezza; x++) {
                char simbolo = riga.charAt(x);
                Posizione pos = new Posizione(x, y);
                caselle[y][x] = terrenoPer(simbolo);
                switch (simbolo) {
                    case 'P' -> partenza = pos;
                    case 'S' -> entita.add(slime("nemico-" + (contatoreNemici++), pos));
                    case 'K' -> entita.add(goblin("nemico-" + (contatoreNemici++), pos));
                    case 'N' -> entita.add(anziano(pos));
                    default -> { /* solo terreno */ }
                }
            }
        }
        if (partenza == null) {
            throw new IllegalArgumentException("La mappa '" + id + "' non ha un punto di partenza (P).");
        }
        return new Mappa(id, caselle, partenza, entita);
    }

    private static TipoCasella terrenoPer(char simbolo) {
        return switch (simbolo) {
            case 'T' -> TipoCasella.ALBERO;
            case 'H' -> TipoCasella.CASA;
            case '#' -> TipoCasella.STACCIONATA;
            case 'W' -> TipoCasella.ACQUA;
            case 'B' -> TipoCasella.PONTE;
            case '.' -> TipoCasella.SENTIERO;
            case 'f' -> TipoCasella.FIORI;
            // 'G' e i simboli di entità (P/S/K/N) poggiano su erba calpestabile
            default -> TipoCasella.ERBA;
        };
    }

    // --- Archetipi di entità ----------------------------------------------

    private static Nemico slime(String id, Posizione pos) {
        Statistiche statistiche = new Statistiche(20, 0, 6, 2, 0, PROGRESSIONE_NEMICI);
        return new Nemico(id, "Slime", pos, statistiche, new ComportamentoAggressivo(), 15, 5, null);
    }

    private static Nemico goblin(String id, Posizione pos) {
        Statistiche statistiche = new Statistiche(28, 0, 8, 3, 0, PROGRESSIONE_NEMICI);
        Pozione bottino = new Pozione("Pozione", "Ripristina 25 PV.", 25);
        return new Nemico(id, "Goblin", pos, statistiche, new ComportamentoPrudente(), 25, 10, bottino);
    }

    private static Png anziano(Posizione pos) {
        return new Png("png-anziano", "Anziano del villaggio", pos, List.of(
                "Benvenuto, viaggiatore.",
                "Nei dintorni si aggirano slime e goblin: sii prudente.",
                "Usa le pozioni quando i punti vita scarseggiano."
        ));
    }
}
